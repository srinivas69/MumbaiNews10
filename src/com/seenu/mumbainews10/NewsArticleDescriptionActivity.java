package com.seenu.mumbainews10;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class NewsArticleDescriptionActivity extends SherlockActivity {

	private String url;
	private ProgressDialog pDiag;
	private StringRequest strReq;
	private Utils objUtils;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_description);

		pDiag = new ProgressDialog(this);
		objUtils = new Utils(this);

		url = getIntent().getExtras().getString("ARTICLE_URL");

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.google.com");
		System.out.println(url);

		// makeRequestToServer();

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	private void makeRequestToServer() {
		// TODO Auto-generated method stub

		pDiag.setTitle("Loading");
		pDiag.setMessage("please wait.....");
		pDiag.show();

		strReq = new StringRequest(url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub

				Document document = Jsoup.parse(response);
				String title = document.select("title").text();

				System.out.println(title);
				Element headEle = document.select("head").first();
				Element bodyEle = document.getElementsByTag("body").first();
				Elements metaEles = headEle.select("meta");

				for (Element metaEle : metaEles) {
					String metaEleName = metaEle.attr("name");
					if (metaEleName.equals("description")) {
						String description = metaEle.attr("content");
						System.out.println(description);
						break;
					} else
						continue;
				}

				// System.out.println(body);
				pDiag.dismiss();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

				NetworkResponse response = error.networkResponse;

				if (response == null) {
					pDiag.dismiss();
					Toast.makeText(getApplicationContext(),
							"Bad Internet connection", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		objUtils.addToRequestQueue(strReq);
	}
}
