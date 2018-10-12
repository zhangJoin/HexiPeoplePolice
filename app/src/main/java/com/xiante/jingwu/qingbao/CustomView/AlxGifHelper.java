package com.xiante.jingwu.qingbao.CustomView;

import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by yangling on 2017/12/20.
 */

public class AlxGifHelper {

    public static class ProgressViews {
        public ProgressViews(WeakReference<GifImageView> gifImageViewWeakReference) {
            this.gifImageViewWeakReference = gifImageViewWeakReference;
        }

        /**
         * gif显示控件
         */
        public WeakReference<GifImageView> gifImageViewWeakReference;
        /**
         * imageView的控件宽度
         */
        public int displayWidth;
    }

    /**
     * 防止同一个gif文件建立多个下载线程,url和imageView是一对多的关系,如果一个imageView建立了一次下载，
     * 那么其他请求这个url的imageView不需要重新开启一次新的下载，这几个imageView同时回调
     */
    public static ConcurrentHashMap<String, ArrayList<ProgressViews>> memoryCache;

    /**
     * 通过本地缓存或联网加载一张GIF图片
     *
     * @param url
     * @param gifView
     */
    public static void displayImage(final String url, GifImageView gifView) {
        //首先查询一下这个gif是否已被缓存
        String md5Url = getMd5(url);
        //带.tmp后缀的是没有下载完成的，用于加载第一帧，不带tmp后缀是下载完成的，
        String path = gifView.getContext().getCacheDir().getAbsolutePath() + "/" + md5Url;
        //这样做的目的是为了防止一个图片正在下载的时候，另一个请求相同url的imageView使用未下载完毕的文件显示一半图像
        final File cacheFile = new File(path);
        //如果本地已经有了这个gif的缓存
        if (cacheFile.exists()) {
        }
        //为了防止activity被finish了但是还有很多gif还没有加载完成，导致activity没有及时被内存回收导致内存泄漏，这里使用弱引用
        final WeakReference<GifImageView> imageViewWait = new WeakReference<>(gifView);
        //设置没有下载完成前的默认图片
//        gifView.setImageResource(R.drawable.loading_index_product);
        //如果以前有别的imageView加载过
        if (memoryCache != null && memoryCache.get(url) != null) {

            //可以借用以前的下载进度，不需要新建一个下载线程了
            memoryCache.get(url).add(new ProgressViews(imageViewWait));
            return;
        }
        if (memoryCache == null) {
            memoryCache = new ConcurrentHashMap<>();
        }
        if (memoryCache.get(url) == null) {
            memoryCache.put(url, new ArrayList<ProgressViews>());
        }
        //将现在申请加载的这个imageView放到缓存里，防止重复加载
        memoryCache.get(url).add(new ProgressViews(imageViewWait));


        // 下载图片
        startDownLoad(url, new File(cacheFile.getAbsolutePath() + ".tmp"), new DownLoadTask() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long total, long current) {
                int progress = 0;
                //得到要下载文件的大小，是通过http报文的header的Content-Length获得的，如果获取不到就是-1
                if (total > 0) {
                    progress = (int) (current * 100 / total);
                }
                ArrayList<ProgressViews> viewses = memoryCache.get(url);
                if (viewses == null) {
                    return;
                }
                //遍历所有的进度条，修改同一个url请求的进度显示
                for (ProgressViews vs : viewses) {
                    //显示第一帧直到全部下载完之后开始动画
                    getFirstPicOfGIF(new File(cacheFile.getAbsolutePath() + ".tmp"), vs.gifImageViewWeakReference.get());
                }

            }

            @Override
            public void onSuccess(File file) {
                if (file == null) {
                    return;
                }
                String path = file.getAbsolutePath();
                if (path == null || path.length() < 5) {
                    return;
                }
                File downloadFile = new File(path);
                File renameFile = new File(path.substring(0, path.length() - 4));
                //将.tmp后缀去掉
                if (path.endsWith(".tmp")) {
                    downloadFile.renameTo(renameFile);
                }

                if (memoryCache == null) {
                    return;
                }
                ArrayList<ProgressViews> viewArr = memoryCache.get(url);
                if (viewArr == null || viewArr.size() == 0) {
                    return;
                }
                //遍历所有的进度条和imageView，同时修改所有请求同一个url的进度
                for (ProgressViews ws : viewArr) {
                    //显示imageView
                    GifImageView gifImageView = ws.gifImageViewWeakReference.get();
                    if (gifImageView != null) {
                        displayImage(renameFile, gifImageView, ws.displayWidth);
                    }
                }
                //这个url的全部关联imageView都已经显示完毕，清除缓存记录
                memoryCache.remove(url);
            }

            @Override
            public void onFailure(Throwable e) {
                if (memoryCache != null) {
                    //下载失败移除所有的弱引用
                    memoryCache.remove(url);
                }
            }
        });
    }

    /**
     * 通过本地文件显示GIF文件
     *
     * @param localFile    本地的文件指针
     * @param gifImageView displayWidth imageView控件的宽度，用于根据gif的实际高度重设控件的高度来保证完整显示，传0表示不缩放gif的大小，显示原始尺寸
     */
    public static boolean displayImage(File localFile, GifImageView gifImageView, int displayWidth) {
        if (localFile == null || gifImageView == null) {
            return false;
        }

        GifDrawable gifFrom;
        try {
            gifFrom = new GifDrawable(localFile);
            int raw_height = gifFrom.getIntrinsicHeight();
            int raw_width = gifFrom.getIntrinsicWidth();

            if (gifImageView.getScaleType() != ImageView.ScaleType.CENTER_CROP && gifImageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                //如果大小应该自适应的话进入该方法（也就是wrap content），不然高度不会自动变化
                if (raw_width < 1 || raw_height < 1) {
                    return false;
                }
                int imageViewWidth = displayWidth;
                if (imageViewWidth < 1) {
                    //当传来的控件宽度不大对的时候，就显示gif的原始大小
                    imageViewWidth = raw_width;
                }
                int imageViewHeight = imageViewWidth * raw_height / raw_width;
                ViewGroup.LayoutParams params = gifImageView.getLayoutParams();
                if (params != null) {
//                    params.height = imageViewHeight;
//                    params.width = imageViewWidth;
                    params.width = Utils.dp2px(gifImageView.getContext(), 140);
                    params.height =Utils.dp2px(gifImageView.getContext(), 90);
                }
            } else {

            }
            gifImageView.setImageDrawable(gifFrom);
            return true;
        } catch (IOException e) {

            return false;
        }
    }


    /**
     * 用于获取一个String的md5值
     *
     * @param str
     * @return
     */
    public static String getMd5(String str) {
        if (str == null || str.length() < 1) {
            return "no_image.gif";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes());
            StringBuilder sb = new StringBuilder(40);
            for (byte x : bs) {
                if ((x & 0xff) >> 4 == 0) {
                    sb.append("0").append(Integer.toHexString(x & 0xff));
                } else {
                    sb.append(Integer.toHexString(x & 0xff));
                }
            }
            if (sb.length() < 24) {
                return sb.toString();
            }
            //为了提高磁盘的查找文件速度，让文件名为16位
            return sb.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            Log.i("Alex", "MD5加密失败");
            return "no_image.gif";
        }
    }

    public static abstract class DownLoadTask {
        abstract void onStart();

        abstract void onLoading(long total, long current);

        abstract void onSuccess(File target);

        abstract void onFailure(Throwable e);

        boolean isCanceled;
    }

    /**
     * 开启下载任务到线程池里，防止多并发线程过多
     *
     * @param uri
     * @param targetFile
     * @param task
     */
    public static void startDownLoad(final String uri, final File targetFile, final DownLoadTask task) {
        final Handler handler = new Handler();
        new AlxMultiTask<Void, Void, Void>() {
            //开启一个多线程池，大小为cpu数量+1

            @Override
            protected Void doInBackground(Void... params) {
                task.onStart();
                downloadToStream(uri, targetFile, task, handler);
                return null;
            }
        }.executeDependSDK();
    }


    /**
     * 通过httpconnection下载一个文件，使用普通的IO接口进行读写
     *
     * @param uri
     * @param targetFile
     * @param task
     * @return
     */
    public static long downloadToStream(String uri, final File targetFile, final DownLoadTask task, Handler handler) {

        if (task == null || task.isCanceled) {
            return -1;
        }

        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bis = null;
        OutputStream outputStream = null;

        long result = -1;
        long fileLen = 0;
        long currCount = 0;
        try {

            try {
                final URL url = new URL(uri);
                outputStream = new FileOutputStream(targetFile);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setReadTimeout(10000);

                final int responseCode = httpURLConnection.getResponseCode();
                if (HttpURLConnection.HTTP_OK == responseCode) {
                    bis = new BufferedInputStream(httpURLConnection.getInputStream());
                    result = httpURLConnection.getExpiration();
                    result = result < System.currentTimeMillis() ? System.currentTimeMillis() + 40000 : result;
                    //这里通过http报文的header Content-Length来获取gif的总大小，需要服务器提前把header写好
                    fileLen = httpURLConnection.getContentLength();
                } else {
                    return -1;
                }
            } catch (final Exception ex) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        task.onFailure(ex);
                    }
                });
                return -1;
            }


            if (task.isCanceled) {
                return -1;
            }

            //每4k更新进度一次
            byte[] buffer = new byte[4096];
            int len = 0;
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                currCount += len;
                if (task.isCanceled) {
                    return -1;
                }
                final long finalFileLen = fileLen;
                final long finalCurrCount = currCount;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        task.onLoading(finalFileLen, finalCurrCount);
                    }
                });
            }
            out.flush();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    task.onSuccess(targetFile);
                }
            });
        } catch (Throwable e) {
            result = -1;
            task.onFailure(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (final Throwable e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            task.onFailure(e);
                        }
                    });
                }
            }
        }
        return result;
    }

    /**
     * 加载gif的第一帧图像，用于下载完成前占位
     *
     * @param gifFile
     * @param imageView
     */
    public static void getFirstPicOfGIF(File gifFile, GifImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (imageView.getTag(R.style.AppTheme) instanceof Integer) {
            return;//之前已经显示过第一帧了，就不用再显示了
        }
        try {
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            boolean canSeekForward = gifFromFile.canSeekForward();
            if (!canSeekForward) {
                return;
            }
            gifFromFile.seekToFrame(0);
            gifFromFile.pause();//静止在该帧
            imageView.setImageDrawable(gifFromFile);
            //标记该imageView已经显示过第一帧了
            imageView.setTag(R.style.AppTheme, 1);
        } catch (IOException e) {

        }
    }
}
