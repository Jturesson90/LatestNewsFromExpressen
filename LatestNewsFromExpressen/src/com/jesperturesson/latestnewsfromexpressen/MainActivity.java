package com.jesperturesson.latestnewsfromexpressen;

import java.util.ArrayList;

import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.NewsSite;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	ListView listView;
	NewsAdapter newsAdapter;
	SwipeRefreshLayout swipeRefreshLayout;
	NewsSite news;
	protected boolean connectionIsFine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		initListView();
		initSwipeRefreshLayout();
		refreshNews();
	}

	private void initSwipeRefreshLayout() {
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						refreshNews();
					}
				});
	}

	private void initListView() {
		newsAdapter = new NewsAdapter(getApplication().getApplicationContext());
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Article article = (Article) parent.getItemAtPosition(position);

				TextView textView = (TextView) view
						.findViewById(R.id.news_row_description);
				if (textView.getVisibility() == View.VISIBLE) {
					Toast.makeText(getApplicationContext(), "2nd click =)",
							Toast.LENGTH_SHORT).show();
				} else {
					setDescriptionVisible(textView, article);
				}
			}
		});

	}

	private void setDescriptionVisible(TextView textView, Article article) {
		setAllDescriptionsToGone();
	
		textView.setVisibility(View.VISIBLE);
		article.clicked = true;
	}

	private void setAllDescriptionsToGone() {

		int len = listView.getChildCount();
		for (int i = 0; i < len; i++) {
			TextView textView = (TextView) listView.getChildAt(i).findViewById(
					R.id.news_row_description);
			textView.setVisibility(View.GONE);
		}
		len = listView.getCount();
		for (int i = 0; i < len; i++) {
			Article article = (Article) listView.getItemAtPosition(i);
			article.clicked = false;
		}

	}

	private void refreshNews() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connectionIsFine = false;
				ServerConnection connection = new ServerConnection();
				final XmlParser parser = new XmlParser();

				String[] urls = getResources().getStringArray(R.array.rss_urls);
				final ArrayList<Article> articles = connection.get(urls[2],
						parser);
				if (articles != null) {
					connectionIsFine = true;
				}
				if (connectionIsFine) {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							NewsSite news = new NewsSite(articles);
							newsAdapter.clear();
							pushToAdapter(news);
							swipeRefreshLayout.setRefreshing(false);
						}
					});
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							swipeRefreshLayout.setRefreshing(false);
							Toast.makeText(getApplicationContext(),
									R.string.network_failed, Toast.LENGTH_LONG)
									.show();
						}
					});
				}
			}
		}).start();
	}

	private void pushToAdapter(NewsSite news) {

		listView.setAdapter(newsAdapter);
		for (Article article : news.articles) {
			newsAdapter.add(article);
		}
		newsAdapter.notifyDataSetChanged();
	}

}
