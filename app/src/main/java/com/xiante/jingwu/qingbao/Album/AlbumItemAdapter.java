package com.xiante.jingwu.qingbao.Album;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiante.jingwu.qingbao.Activity.MainTab_ShouyeActivity;
import com.xiante.jingwu.qingbao.CustomView.CommonView.MyGridViewItem;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AlbumItemAdapter extends BaseAdapter {
    public static final int CODE_TAKE_PHOTO = 0x678;
    private List<PhotoUpImageItem> list;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private HashMap<Integer, Boolean> stateMap;
    private int maxSelect;
    private Context mcontext;
    public static final  String TEMPLE_IMAGE="TEMPLE_IMAGE";
    public AlbumItemAdapter(List<PhotoUpImageItem> list, Context context) {
        this.list = list;
        mcontext = context;
        stateMap = new HashMap<Integer, Boolean>();
        layoutInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        // 使用DisplayImageOption.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .bitmapConfig(Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build(); // 创建配置过的DisplayImageOption对象
        initState(list.size());
    }


    public void refreshAdapter() {
        initState(list.size());
        notifyDataSetChanged();
    }

    public boolean isOverSelect() {

        return getSelectedCount() >= maxSelect;
    }

    public void setMaxSelect(int max) {
        this.maxSelect = max;
    }

    public int getSelectedCount() {
        int c = 0;
        for (int i = 0; i < list.size(); i++) {
            if (stateMap.get(i)) {
                c++;
            }
        }
        return c;
    }

    private void initState(int count) {
        for (int i = 0; i < count; i++) {
            stateMap.put(i, false);
        }

    }


    public ArrayList<PhotoUpImageItem> getSelectImage() {
        ArrayList<PhotoUpImageItem> l = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (stateMap.get(i)) {
                l.add(list.get(i));
            }
        }
        return l;
    }


    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_album, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意
        //这里的uri地址，因为我们现在实现的是获取本地图片，所以使
        //用"file://"+图片的存储地址。如果要获取网络图片，
        //这里的uri就是图片的网络地址。
        viewHolder.checkBox.setVisibility(View.VISIBLE);
        viewHolder.checkBox.setOnCheckedChangeListener(null);
      //这个方法会触发OnCheckedChangeListener方法，所以要把holder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.itemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    takePhoto();
                }else {
                    int myposition=position;
                    myposition--;
                    if(stateMap.get(myposition)){
                        viewHolder.shadowCover.setVisibility(View.GONE);
                        stateMap.put(myposition,false);
                        viewHolder.checkBox.setChecked(false);
                    }else{
                        if (isOverSelect()) {
                            viewHolder.checkBox.setChecked(false);
                            Toast.makeText(mcontext, "您最多可选择" + maxSelect + "张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        viewHolder.shadowCover.setVisibility(View.VISIBLE);
                        stateMap.put(myposition,true);
                        viewHolder.checkBox.setChecked(true);
                    }
                }



            }
        });

        int imageposition=position;
        imageposition--;
        if(position==0){
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
            viewHolder.ivAlbum.setBackgroundResource(R.drawable.btn_pz);
             viewHolder.ivAlbum.setImageBitmap(null);
        }else {
            viewHolder.checkBox.setChecked(stateMap.get(imageposition));
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            imageLoader.displayImage("file://" + list.get(imageposition).getImagePath(), viewHolder.ivAlbum, options);
        }
        return convertView;
    }


    private void takePhoto() {

        AndPermission.with(mcontext)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoUri = getMediaFileUri(TEMPLE_IMAGE);
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        ((Activity)mcontext).startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                        Toast.makeText(mcontext, "没有同意相机权限，不能拍照", Toast.LENGTH_LONG).show();
                    }
                }).start();




    }


    public Uri getMediaFileUri(String fileDir){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/"+fileDir+"/");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filepath=mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        mcontext.getSharedPreferences(Global.CAMER_IMAGE_PATH,Context.MODE_PRIVATE).edit().putString(Global.CAMER_IMAGE_PATH,filepath).commit();
           Uri mOriginUri= Utils.getFileUri(mcontext,filepath,"com.esint.pahx.messenger.hHXMJFileProvider");
        return mOriginUri;
    }



    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_album.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.iv_album)
        ImageView ivAlbum;
        @InjectView(R.id.shadow_cover)
        View shadowCover;
        @InjectView(R.id.isselected)
        CheckBox checkBox;
        @InjectView(R.id.item_album)
        MyGridViewItem itemAlbum;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
