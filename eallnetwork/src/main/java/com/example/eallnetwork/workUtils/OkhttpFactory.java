package com.example.eallnetwork.workUtils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.helper.ProgressHelper;
import com.example.eallnetwork.listener.DownProgressListener;
import com.example.eallnetwork.listener.ProgressListener;
import com.example.eallnetwork.listener.impl.UIProgressListener;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpFactory extends NetworkFactory {

	
	private OkHttpClient client;
	private static  Request.Builder builder;
	private HashMap<String, String> params;
	private String url;
	private static OkhttpFactory factory;
	private Handler handler;
    public static OkhttpFactory getInstance(){
	
    	if(factory==null){
    		factory=new OkhttpFactory();
    	}
    	builder=new Request.Builder();
		return factory;
		
	}

	public void addHead(String key,String value){
		builder.addHeader(key,value);
	}

	
    private OkhttpFactory() {
    	
		if(client==null){
			client=new OkHttpClient();
			OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
			mBuilder.sslSocketFactory(createSSLSocketFactory());
			mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
			mBuilder.connectTimeout(30, TimeUnit.SECONDS);//连接超时时间 connect timeout
			mBuilder.readTimeout(30,TimeUnit.SECONDS);// socket timeout
			client=mBuilder.build();
			handler=new Handler(Looper.getMainLooper());
		}
     		
	}
	/**
	 * 让okhttps信任所有的证书(针对https)
	 * 实现X509TrustManager接口
	 */
	private static class TrustAllCerts implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

		@Override
		public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
	}

	/**
	 * 实现HostnameVerifier接口
	 */
	private static class TrustAllHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	private static SSLSocketFactory createSSLSocketFactory() {
		SSLSocketFactory ssfFactory = null;

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

			ssfFactory = sc.getSocketFactory();
		} catch (Exception e) {
		}

		return ssfFactory;
	}


	public  void uploadImage(String url, final SuccessfulCallback successfulCallback, final FailCallback failCallback,String ImagePath,HashMap<String,String> p){

		MultipartBody.Builder body = new MultipartBody.Builder("AaB03x")
				.setType(MultipartBody.FORM);

		Set<String> keySet=p.keySet();
		for (String k:keySet
			 ) {
			body.addFormDataPart(k,p.get(k));

		}
				body.addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
						.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
								RequestBody.create(MediaType.parse("image/png"), new File(ImagePath)))
						.build());

		builder.url(url).post(body.build());
		Call call=client.newCall(builder.build());

		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Call call,final IOException e) {
				if(failCallback!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail("网络请求异常");
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successfulCallback!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});



					//	getSuccessfulCallback().success(response.body().byteStream(), response.body().contentLength());
				}
			}
		});


	}

	/**
	 * 带参数的文件上传,带进度条
	 * @param context
	 * @param url
	 * @param map
	 * @param file
	 * @param successfulCallback
	 * @param failCallback
	 */
	public void post_file(final Context context,final ProgressBar progressBar, final TextView textView,final String url, final Map<String, Object> map,
						  File file,final SuccessfulCallback successfulCallback, final FailCallback failCallback,String file_name) {

		//这个是ui线程回调，可直接操作UI
		final UIProgressListener uiProgressRequestListener = new UIProgressListener() {
			@Override
			public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
//				Log.e("TAG", "bytesWrite:" + bytesWrite);
//				Log.e("TAG", "contentLength" + contentLength);
//				Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
//				Log.e("TAG", "done:" + done);
//				Log.e("TAG", "================================");
				//ui层回调
				int percent= (int) ((100 * bytesWrite) / contentLength);
				textView.setText("上传进度("+percent+"%)");
				progressBar.setProgress((int) ((100 * bytesWrite) / contentLength));
				//Toast.makeText(getApplicationContext(), bytesWrite + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onUIStart(long bytesWrite, long contentLength, boolean done) {
				super.onUIStart(bytesWrite, contentLength, done);

			}

			@Override
			public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
				super.onUIFinish(bytesWrite, contentLength, done);
				textView.setText("上传完成");
			}
		};
		// form 表单形式上传
		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

		if (map != null) {
			// map 里面是请求中所需要的 key 和 value
			for (Map.Entry entry : map.entrySet()) {
				if(entry.getKey()!=null&&!entry.getKey().equals("")){
					requestBody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		if(file != null){
			// MediaType.parse() 里面是上传的文件类型。
			RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
			String filename = file.getName();
			// 参数分别为， 请求key ，文件名称 ， RequestBody
			requestBody.addFormDataPart(file_name, file.getName(), body);
		}

		Request request = new Request.Builder().url(url)
				.post(ProgressHelper.addProgressRequestListener(requestBody.build(), uiProgressRequestListener)).tag(context).build();
//		Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
		// readTimeout("请求超时时间" , 时间单位);
		client.newBuilder().hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;//强制返回true，屏蔽掉https证书检查
			}
		}).readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, final IOException e) {
				if (failCallback != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (successfulCallback != null) {

					final String result = response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

	}
	/**
	 * 带参数的文件上传
	 * @param context
	 * @param url
	 * @param map
	 * @param file
	 * @param successfulCallback
	 * @param failCallback
	 */
	public void post_file(final Context context,final String url, final Map<String, Object> map,
						  File file,final SuccessfulCallback successfulCallback, final FailCallback failCallback,String file_name) {
//
		// form 表单形式上传
		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

		if (map != null) {
			// map 里面是请求中所需要的 key 和 value
			for (Map.Entry entry : map.entrySet()) {
				if(entry.getKey()!=null&&!entry.getKey().equals("")){
					requestBody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		if(file != null){
			// MediaType.parse() 里面是上传的文件类型。
			RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
			String filename = file.getName();
			// 参数分别为， 请求key ，文件名称 ， RequestBody
			requestBody.addFormDataPart(file_name, file.getName(), body);
		}
		Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
		// readTimeout("请求超时时间" , 时间单位);
		client.newBuilder().hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;//强制返回true，屏蔽掉https证书检查
			}
		}).readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call,final IOException e) {
				if(failCallback!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successfulCallback!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

	}


	public void post_htmlfile(final Context context,final String url, final Map<String, Object> map,
						  File file,final SuccessfulCallback successfulCallback, final FailCallback failCallback,String file_name) {
//
		// form 表单形式上传
		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

		if (map != null) {
			// map 里面是请求中所需要的 key 和 value
			for (Map.Entry entry : map.entrySet()) {
				if(entry.getKey()!=null&&!entry.getKey().equals("")){
					requestBody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		if(file != null){
			// MediaType.parse() 里面是上传的文件类型。
			RequestBody body = RequestBody.create(MediaType.parse("text"), file);
			String filename = file.getName();
			// 参数分别为， 请求key ，文件名称 ， RequestBody
			requestBody.addFormDataPart(file_name, file.getName(), body);
		}
		Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
		// readTimeout("请求超时时间" , 时间单位);
		client.newBuilder().hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;//强制返回true，屏蔽掉https证书检查
			}
		}).readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call,final IOException e) {
				if(failCallback!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successfulCallback!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

	}

	/**
	 * 带参数的文件上传,多文件同时上传,单个File数组
	 * @param context
	 * @param url
	 * @param map
	 * @param
	 * @param successfulCallback
	 * @param failCallback
	 */
	public void post_files(final Context context,final String url, final Map<String, String> map,
						  File[] files,final SuccessfulCallback successfulCallback, final FailCallback failCallback,
						   String file_name) {
		// form 表单形式上传
		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

		if (map != null) {
			// map 里面是请求中所需要的 key 和 value
			for (Map.Entry entry : map.entrySet()) {
				if(entry.getKey()!=null&&!entry.getKey().equals("")){
					requestBody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		//多次上传
		if(files!=null&&files.length>0){
			for(int i=0;i<files.length;i++){
				if(files[i] != null){
					// MediaType.parse() 里面是上传的文件类型。
					RequestBody body = RequestBody.create(MediaType.parse("image/*"), files[i]);
					String filename = files[i].getName();
					// 参数分别为， 请求key ，文件名称 ， RequestBody
					requestBody.addFormDataPart(file_name+"[]", filename, body);
					//多张图片上传需要加上[],否则只能上传一张
				}
			}

		}

		Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
//		 readTimeout("请求超时时间" , 时间单位);
		client.newBuilder().hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;//强制返回true，屏蔽掉https证书检查
			}
		}).readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call,final IOException e) {
				if(failCallback!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successfulCallback!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

	}


	/**
	 * 带参数的文件上传,多文件同时上传,单个File数组
	 * @param context
	 * @param url
	 * @param map
	 * @param
	 * @param successfulCallback
	 * @param failCallback
	 */
	public String execute_files(final Context context,final String url,
						   List<String> pathlist,
						   String file_name) {
		// form 表单形式上传
		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
		//多次上传
		if(pathlist!=null&&pathlist.size()>0){
			for(int i=0;i<pathlist.size();i++){
					// MediaType.parse() 里面是上传的文件类型。
				File file=new File(pathlist.get(i));
				if(file.exists()){
					RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
					String filename = file.getName();
					// 参数分别为， 请求key ，文件名称 ， RequestBody
					requestBody.addFormDataPart(file_name, filename, body);
					//多张图片上传需要加上[],否则只能上传一张
				}

			}

		}

		Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
		String result="";
		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response!=null&&response.isSuccessful()) {
				result=new String(response.body().bytes());
			} else {

			}
		} catch (IOException e) {
			//	showErrorMessage(position);
			e.printStackTrace();
		}
		return result;
//		 readTimeout("请求超时时间" , 时间单位);
//		client.newBuilder().hostnameVerifier(new HostnameVerifier() {
//			@Override
//			public boolean verify(String hostname, SSLSession session) {
//				return true;//强制返回true，屏蔽掉https证书检查
//			}
//		}).readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call,final IOException e) {
//				if(failCallback!=null){
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							failCallback.fail(e.getMessage());
//						}
//					});
//				}
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				if(successfulCallback!=null){
//
//					final	String result=response.body().string();
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							try {
//								successfulCallback.success(result);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					});
//				}
//			}
//		});

	}

	public String executeFile(String url,String path,Context context){
		String result="";
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		Log.e("TAG", "url:" + url);
		File file=new File(path);
		MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
		bodyBuilder.addFormDataPart("inputfile", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
		MultipartBody build = bodyBuilder.build();

		Request request = new Request.Builder().url(url).post(build).tag(context).build();
		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response!=null&&response.isSuccessful()) {
				result=new String(response.body().bytes());
//				List<FileUploadBean> fileUploadBeans = JSON new String(response.body().bytes());
//				String strFileGUID = fileUploadBeans.get(0).getGuid();
//				String strFileName = fileUploadBeans.get(0).getName();
//				notifyUploadComplete(strFileGUID,strFileName,position);
			} else {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


return result;
	}


	/**
	 * 带参数的文件上传,多文件同时上传,多个File数组
	 * @param context
	 * @param url
	 * @param map
	 * @param
	 * @param successfulCallback
	 * @param failCallback
	 */
	public void post_mapfiles(final Context context,final String url, final Map<String, String> map,
							  Map<String,File[]>mapfiles,final SuccessfulCallback successfulCallback, final FailCallback failCallback) {
		// form 表单形式上传
		MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

		if (map != null) {
			// map 里面是请求中所需要的 key 和 value
			for (Map.Entry entry : map.entrySet()) {
				if(entry.getKey()!=null&&!entry.getKey().equals("")){
					requestBody.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
		if(mapfiles!=null){
			for (Map.Entry entry : mapfiles.entrySet()) {
				if(entry.getKey()!=null&&!entry.getKey().equals("")){
					File[] files= (File[]) entry.getValue();
					String file_key= (String) entry.getKey();
					//多次上传
					if(files!=null&&files.length>0){
						for(int i=0;i<files.length;i++){
							if(files[i] != null){
								// MediaType.parse() 里面是上传的文件类型。
								RequestBody body = RequestBody.create(MediaType.parse("image/*"), files[i]);
								String filename = files[i].getName();
								// 参数分别为， 请求key ，文件名称 ， RequestBody
								requestBody.addFormDataPart(file_key+"[]", filename, body);
								//多张图片上传需要加上[],否则只能上传一张
							}
						}

					}
				}
			}
		}
		Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
//		 readTimeout("请求超时时间" , 时间单位);
		client.newBuilder().hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;//强制返回true，屏蔽掉https证书检查
			}
		}).readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call,final IOException e) {
				if(failCallback!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successfulCallback!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

	}



	public static final MediaType JSON = MediaType.parse("application/json");
	public  void postjson(String url, String json, final SuccessfulCallback successfulCallback, final FailCallback failCallback) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		Call call=client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Call call,final IOException e) {
				if(failCallback!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failCallback.fail("网络请求异常");
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successfulCallback!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successfulCallback.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

	}

	/**
	 * 下载文件，带进度条
	 * @param context
	 * @param progressBar
	 * @param downloadUrl
	 */
	public void download(final Context context,final ProgressBar progressBar,String downloadUrl) {
		//这个是非ui线程回调，不可直接操作UI
		final ProgressListener progressResponseListener = new ProgressListener() {
			@Override
			public void onProgress(long bytesRead, long contentLength, boolean done) {
				Log.e("TAG", "bytesRead:" + bytesRead);
				Log.e("TAG", "contentLength:" + contentLength);
				Log.e("TAG", "done:" + done);
				if (contentLength != -1) {
					//长度未知的情况下回返回-1
					Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
				}
				Log.e("TAG", "================================");
			}
		};


		//这个是ui线程回调，可直接操作UI
		final UIProgressListener uiProgressResponseListener = new UIProgressListener() {
			@Override
			public void onUIProgress(long bytesRead, long contentLength, boolean done) {
				Log.e("TAG", "bytesRead:" + bytesRead);
				Log.e("TAG", "contentLength:" + contentLength);
				Log.e("TAG", "done:" + done);
				if (contentLength != -1) {
					//长度未知的情况下回返回-1
					Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
				}
				Log.e("TAG", "================================");
				//ui层回调
				Log.i("-----进度","+======"+(int) ((100 * bytesRead) / contentLength));
				progressBar.setProgress((int) ((100 * bytesRead) / contentLength));//进度条
				//Toast.makeText(getApplicationContext(), bytesRead + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onUIStart(long bytesRead, long contentLength, boolean done) {
				super.onUIStart(bytesRead, contentLength, done);
				Log.i("-----开始","+======"+contentLength);
			}

			@Override
			public void onUIFinish(long bytesRead, long contentLength, boolean done) {
				super.onUIFinish(bytesRead, contentLength, done);
			}
		};

		//构造请求
		final Request request1 = new Request.Builder()
				.url(downloadUrl)
				.build();

		//包装Response使其支持进度回调
		ProgressHelper.addProgressResponseListener(client, uiProgressResponseListener).newCall(request1).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

			}

		});
	}
	@Override
	public void start() {

		if(getMethod()==METHOD_GET){
			String p=getGetParams(getParams());
            if(getUrl().contains("?")){
				builder.url(getUrl()+"&"+p);
			}else {
				builder.url(getUrl()+"?"+p);
			}
		}else if(getMethod()==METHOD_POST){

			builder.url(getUrl()).post(getPostParams(getParams()));
		}
		
		
		Call call=client.newCall(builder.build());
		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Call call, final IOException e) {
				if (getFailCallback() != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							getFailCallback().fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (getSuccessfulCallback() != null) {

					final String result = response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								getSuccessfulCallback().success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});

//					getSuccessfulCallback().success(response.body().byteStream(), response.body().contentLength());
				}
			}
		});
		
	}

	@Override
	public void start(int method, String url, HashMap<String, String> params, final SuccessfulCallback successCall,
			final FailCallback failsCall) {

		if(method==METHOD_GET){
			String newUrl="";
			String p=getGetParams(params);
			if(url.contains("?")){
				newUrl=url + "&" + p;
			}else{
				newUrl=url + "?" + p;
			}
			builder.url(newUrl);
      Log.i("url----->",newUrl);
		}else if(method==METHOD_POST){
			for (String key : params.keySet()) {
				if(params.get(key)==null||params.get(key).equals("null")){
					params.put(key,"");
				}
			}
			builder.url(url).post(getPostParams(params));
			String newUrl="";
			String p=getGetParams(params);
			if(url.contains("?")){
				newUrl=url + "&" + p;
			}else{
				newUrl=url + "?" + p;
			}
			Log.i("url----->", newUrl);
		}

		Call call=client.newCall(builder.build());
		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Call call,final IOException e) {
				if(failsCall!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failsCall.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successCall!=null){

					final	String result=response.body().string();
					handler.post(new Runnable() {
						@Override
						public void run() {
							try {
								successCall.success(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});

					successCall.success(response.body().byteStream(), response.body().contentLength());
				}
			}
		});

//		call.enqueue(new Callback() {
//			@Override
//			public void onResponse(Response response) throws IOException {
//				if(successCall!=null){
//                   final  String result=response.body().string();
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							successCall.success(result);
//						}
//					});
//
//					//	getSuccessfulCallback().success(response.body().byteStream(), response.body().contentLength());
//
//				}
//			}
//
//			@Override
//			public void onFailure(Request response,final IOException arg1) {
//				  if(failsCall!=null){
//
//					  handler.post(new Runnable() {
//						  @Override
//						  public void run() {
//							  failsCall.fail(arg1.getMessage());
//						  }
//					  });
//				  }
//			}
//		});


	}

	/**
	 *下载文件
	 * @param url
	 * @param successCall
	 * @param failsCall
	 */
	public void downloadFile(String url, final SuccessfulCallback successCall,
							 final FailCallback failsCall){

		builder.url(url);
		Call call=client.newCall(builder.build());

		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Call call,final IOException e) {
				if(failsCall!=null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							failsCall.fail(e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(successCall!=null){

					if(response.body().byteStream()!=null){
						successCall.success(response.body().byteStream(), response.body().contentLength());
					}else {
						if(failsCall!=null){
							handler.post(new Runnable() {
								@Override
								public void run() {
									failsCall.fail("");
								}
							});
						}
					}


				}
			}
		});

	}
	public void downloadFile( InputStream ism ,  File file,  DownProgressListener listener){
		byte[] bucket=new byte[1024*10];
		int perRead=0;
		long prog=0;

		FileOutputStream ops=null;
		try {
			ops=new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((perRead=ism.read(bucket))>=0){
				ops.write(bucket,0,perRead);
				prog+=perRead;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ism.close();
			ops.flush();
			ops.close();
			listener.onProgress(1.0);
		}catch (IOException E){

		}


	}


	


	
}
