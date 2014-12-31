package com.jesperturesson.latestnewsfromexpressen;

import java.util.ArrayList;

import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.BitmapFunctions;
import com.jesperturesson.latestnewsfromexpressen.models.NewsSite;
import com.jesperturesson.latestnewsfromexpressen.models.Sort;

import android.app.Activity;
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
	public static Activity mainActivity;
	ArrayList<Article> articles;
	ListView listView;
	NewsAdapter newsAdapter;
	SwipeRefreshLayout swipeRefreshLayout;
	NewsSite news;

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

				Toast.makeText(
						getApplicationContext(),
						"" + article.description.text + "\n\n"
								+ article.channelTitle + "\n" + article.author,
						Toast.LENGTH_LONG).show();

			}
		});

	}

	private void refreshNews() {
		articles = new ArrayList<Article>();
		new Thread(new Runnable() {
			@Override
			public void run() {

				ServerConnection connection = new ServerConnection();
				final XmlParser parser = new XmlParser();
				setArticles(connection, articles, parser);

				if (isConnectionFine()) {
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
							showErrorToast();

						}
					});
				}
			}
		}).start();
	}

	private boolean isConnectionFine() {
		boolean connectionIsFine = false;
		if (articles != null) {
			if (articles.size() > 0) {
				connectionIsFine = true;
				Log.d("SIZE", "Size: " + articles.size());
			}
		}
		return connectionIsFine;
	}

	private void setArticles(ServerConnection connection,
			ArrayList<Article> articles, XmlParser parser) {
		String[] urls = getResources().getStringArray(R.array.rss_urls);
		int len = urls.length;
		for (int i = 0; i < len; i++) {
			ArrayList<Article> newArticles = connection.get(urls[i], parser);
			if (newArticles != null) {
				for (int j = 0; j < newArticles.size(); j++) {
					Article newArticle = newArticles.get(j);
					if (!alreadyGotItem(articles, newArticle)) {
						articles.add(newArticle);
					}
				}

			}
		}

	}

	private void showErrorToast() {
		Toast.makeText(getApplicationContext(), R.string.network_failed,
				Toast.LENGTH_LONG).show();
	}

	private boolean alreadyGotItem(ArrayList<Article> articles,
			Article newArticle) {
		for (Article article : articles) {
			if (article.title.equals(newArticle.title)) {
				return true;
			}
		}
		return false;
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

}
