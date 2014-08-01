package com.seenu.mumbainews10;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Utils {

	private Context context;

	public static String ur = "http://www.google.co.in/search?hl=en&gl=in&tbm=nws&authuser=0&q=mumbai&oq=mumbai&gs_l=news-cc.1.0.43j0l9j43i53.4059.5709.0.8296.6.3.0.3.3.0.215.465.0j2j1.3.0...0.0...1ac.1.Cm8MPV3wsZ4";

	public static final String TAG = Utils.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	public Utils(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	// Volley Library Methods

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(context);
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
