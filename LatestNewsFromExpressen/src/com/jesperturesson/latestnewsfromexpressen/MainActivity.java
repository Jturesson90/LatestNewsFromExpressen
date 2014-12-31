package com.jesperturesson.latestnewsfromexpressen;

import java.util.ArrayList;

import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.BitmapFunctions;
import com.jesperturesson.latestnewsfromexpressen.models.NewsSite;
import com.jesperturesson.latestnewsfromexpressen.models.Sort;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	public static ActionBarActivity mainActivity;
	ArrayList<Article> articles;
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
		mainActivity = this;

	}

	private void init() {
		initListView();
		initSwipeRefreshLayout();
		refreshNews();
		swipeRefreshLayout.setRefreshing(true);
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

				Toast.makeText(getApplicationContext(),
						"" + article.description.text, Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

	private void setAllDescriptionsToGone() {

		int len = listView.getCount();
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
				articles = connection.get(urls[2], parser);
				if (articles != null) {
					connectionIsFine = true;
				}
				if (connectionIsFine) {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							NewsSite news = new NewsSite(articles);
							newsAdapter.clear();
							Sort.onDate2(news.articles);
							limitNumberOfArticles(news.articles,
									Article.NUMBER_OF_ITEMS_TO_SHOW);
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

	private void addPictures() {
		int len = newsAdapter.getCount();
		for (int i = 0; i < len; i++) {
			Article article = newsAdapter.getItem(i);
			String imageUrl = article.description.imageLink;
			if (imageUrl != null) {

			}
		}
	}

	private void pushToAdapter(NewsSite news) {

		listView.setAdapter(newsAdapter);
		for (Article article : news.articles) {
			newsAdapter.add(article);
		}

		newsAdapter.notifyDataSetChanged();
	}

	private void limitNumberOfArticles(ArrayList<Article> articles,
			int numberOfItemsToShow) {
		while (articles.size() > numberOfItemsToShow) {
			articles.remove(articles.size() - 1);
		}
	}

	private void showOnlyTenNews() {
		Log.d("HEJ", "Test");
		int len = newsAdapter.getCount();
		for (int i = Article.NUMBER_OF_ITEMS_TO_SHOW; i < len; i++) {
			Article article = newsAdapter
					.getItem(Article.NUMBER_OF_ITEMS_TO_SHOW);
			if (article != null) {
				newsAdapter.remove(article);

			}
		}
		newsAdapter.notifyDataSetChanged();
	}
}
