package com.seenu.mumbainews10;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.actionbarsherlock.app.SherlockActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.net.Uri;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {

	private ListView lv;
	private HeadLinesAdapter adapter;
	private ProgressDialog pDiag;
	private Utils objUtils;
	private StringRequest strReq;

	private ArrayList<String> headlineList;
	private ArrayList<String> urlsList;
	private ArrayList<String> imgUrlsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		objUtils = new Utils(this);
		pDiag = new ProgressDialog(this);

		headlineList = new ArrayList<String>();
		urlsList = new ArrayList<String>();
		imgUrlsList = new ArrayList<String>();

		lv = (ListView) findViewById(R.id.listView1);

		makeRequestToServer();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				String urlArt = (String) adapter.getItemAtPosition(pos);
				// System.out.println(urlArt);

				/*Intent decInt = new Intent(MainActivity.this,
						NewsArticleDescriptionActivity.class);
				decInt.putExtra("ARTICLE_URL", urlArt);
				startActivity(decInt);*/
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlArt));
				startActivity(browserIntent);
				

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void makeRequestToServer() {
		// TODO Auto-generated method stub

		pDiag.setTitle("Loading");
		pDiag.setMessage("please wait.....");
		pDiag.show();

		strReq = new StringRequest(Utils.url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub

				Document document = Jsoup.parse(response);
				Elements ol = document.select("ol");

				for (Element olEle : ol) {
					String idName = olEle.id();

					if (idName.equals("rso")) {
						System.out.println(idName);
						Elements li = olEle.select("li");
						// System.out.println("<li> : " + li.size());

						int i = 1;
						for (Element liEle : li) {
							Element aImgEle = liEle.select("img.th").first();
							Element aEle = liEle.select("a.l").first();
							String headline = aEle.text();
							String url = aEle.attr("href");

							headlineList.add(headline);
							urlsList.add(url);
							// System.out.println(i + ". " + headline);
							// System.out.println(url + "\n");
							if (aImgEle != null) {
								String imageUrl = aImgEle.attr("src");
								// System.out.println(imageUrl);
								imgUrlsList.add(imageUrl);
							} else {
								imgUrlsList.add("");
								// System.out.println(" ");
							}

							i++;
						}

						adapter = new HeadLinesAdapter(MainActivity.this,
								headlineList, urlsList, imgUrlsList);
						lv.setAdapter(adapter);
						break;
					} else
						continue;
				}
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

	private static class HeadLinesAdapter extends BaseAdapter {
		Context context;
		ArrayList<String> headlineList;
		ArrayList<String> urlsList;
		ArrayList<String> imgUrlsList;

		public HeadLinesAdapter(Context context,
				ArrayList<String> headlineList, ArrayList<String> urlsList,
				ArrayList<String> imgUrlsList) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.headlineList = headlineList;
			this.urlsList = urlsList;
			this.imgUrlsList = imgUrlsList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return urlsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return urlsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// getLayoutInflater();

			if (view == null) {
				view = inflater.inflate(R.layout.listview_row, null);
				holder = new ViewHolder();

				holder.imageViewAdpIv = (ImageView) view
						.findViewById(R.id.imageView1);
				holder.titleAdpTv = (TextView) view
						.findViewById(R.id.textView1);

				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.titleAdpTv.setText(headlineList.get(position));

			if (imgUrlsList.get(position).equals(""))
				holder.imageViewAdpIv.setVisibility(View.GONE);
			else {
				holder.imageViewAdpIv.setVisibility(View.VISIBLE);

				UrlImageViewHelper.setUrlDrawable(holder.imageViewAdpIv,
						imgUrlsList.get(position));
			}

			return view;
		}

		private static class ViewHolder {
			private ImageView imageViewAdpIv;
			private TextView titleAdpTv;
		}

	}

}
