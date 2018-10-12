package com.example.eallnetwork.workUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eallnetwork.R;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class VolleyHttpFactory extends NetworkFactory {

    private static VolleyHttpFactory volleySingleton;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;
	
    
    
    private VolleyHttpFactory(Context context) {

        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
        new ImageLoader.ImageCache(){
            private final LruCache<String,Bitmap> cache = new LruCache<String ,Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url){
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url,Bitmap bitmap){
                cache.put(url,bitmap);
            }
        });
        
        
    }
    
    
    public static synchronized VolleyHttpFactory getInstance(Context context){
           mContext=context;
        if(volleySingleton == null){
            volleySingleton = new VolleyHttpFactory(context);
        }
        return volleySingleton;
    }
    
	@Override
	public void start() {
		
		if(getMethod()==METHOD_GET){
			Log.i("VolleyHttp",getUrl()+"?"+getGetParams(getParams()));
			StringRequest stringRequest = new StringRequest(getUrl()+"?"+getGetParams(getParams()), new Listener<String>() {

				@Override
				public void onResponse(String arg0) {
					try {
						getSuccessfulCallback().success(arg0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
					getFailCallback().fail(arg0.getMessage());
					
				}
			});
			
	   VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
	
	   
		}else if(getMethod()==METHOD_POST){
			
			
			StringRequest stringRequest=new StringRequest(Method.POST, getUrl(), new Listener<String>() {

				@Override
				public void onResponse(String arg0) {

					try {
						getSuccessfulCallback().success(arg0);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
					getFailCallback().fail(arg0.getMessage());
				
				}
			}){
				
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
				
					return VolleyHttpFactory.this.getParams();

				}
			};
			
	VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
		}
		
		
	}
	
	
	
	 @Override
	public void start(int method, String url, final HashMap<String, String> params, final SuccessfulCallback successCall,
			final FailCallback failsCall) {
	
		 
		 if(method==METHOD_GET){
			 String newUrl="";
			 String p=getGetParams(params);
			 if(url.contains("?")){
				 newUrl=url + "&" + p;
			 }else{
				 newUrl=url + "?" + p;
			 }
				StringRequest stringRequest = new StringRequest(newUrl, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						try {
							successCall.success(arg0);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						
						failsCall.fail(arg0.getMessage());
						
					}
				});
				
		   VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
		
		   
			}else if(method==METHOD_POST){
				
				
				StringRequest stringRequest=new StringRequest(Method.POST, url, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

						try {
							successCall.success(arg0);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						
						failsCall.fail(arg0.getMessage());

					
					}
				}){
					
					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
					
						return params;

					}
				};
				
		VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest);
			}
		 
	}


	public RequestQueue getRequestQueue(){
	        if(mRequestQueue == null){
	            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
	        }
	        return mRequestQueue;
	    }
	
	 
	 public <T> void addToRequestQueue(Request<T> req){
	        getRequestQueue().add(req);
	    }
	   
	 
	 public ImageLoader getImageLoader() {
	        return mImageLoader;
}
	 
	 
	public void setImageBitmap(String url,final ImageView view){
		
		
		ImageRequest request=new ImageRequest(url, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap arg0) {
				view.setImageBitmap(arg0);
				
			}
		}, 0, 0, Config.ARGB_8888,new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				view.setImageResource(R.drawable.ic_launcher);
			}
		});
		
		VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(request);
		
	}
	public void setImageBitmapBackground(String url,final View view){


		ImageRequest request=new ImageRequest(url, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap arg0) {
				Drawable drawable =new BitmapDrawable(arg0);
				view.setBackgroundDrawable(drawable);

			}
		}, 0, 0, Config.ARGB_8888,new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});

		VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(request);

	}
	public void setImageNewBitmap(String url,final ImageView imageView, final int newWidth , final int newHeight){


		ImageRequest request=new ImageRequest(url, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap bm) {
				if(bm!=null){
					// 获得图片的宽高
					int width = bm.getWidth();
					int height = bm.getHeight();
					// 计算缩放比例
					float scaleWidth = ((float) newWidth) / width;
					float scaleHeight = ((float) newHeight) / height;
					// 取得想要缩放的matrix参数
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleHeight);
					// 得到新的图片
					Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
					if(newbm!=null){
						imageView.setImageBitmap(newbm);
					}
				}

			}
		}, 0, 0, Config.ARGB_8888,new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});

		VolleyHttpFactory.getInstance(mContext.getApplicationContext()).addToRequestQueue(request);

	}
	
	
	public void setImageByImageLoader(String url,final ImageView view){
		
		ImageLoader loader=getInstance(view.getContext()).getImageLoader();
		loader.get(url, loader.getImageListener(view, R.drawable.ic_launcher, R.drawable.ic_launcher), 0, 0);
		
	}
	 
	 
}
