package com.jesperturesson.latestnewsfromexpressen.activities;

import java.util.ArrayList;

import com.jesperturesson.latestnewsfromexpressen.R;
import com.jesperturesson.latestnewsfromexpressen.adapters.NewsAdapter;
import com.jesperturesson.latestnewsfromexpressen.helpers.Sort;
import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.NewsSite;
import com.jesperturesson.latestnewsfromexpressen.models.XmlParser;
import com.jesperturesson.latestnewsfromexpressen.sync.ServerConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	public static Activity mainActivity;

	ProgressDialog progressDialog;
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
		initProgressDialog();
		refreshNews();
	}

	private void initProgressDialog() {
		progressDialog = ProgressDialog.show(this,
				getString(R.string.progress_dialog_header_news),
				getString(R.string.progress_dialog_text_news), true);

	}

	private void initSwipeRefreshLayout() {
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_to_refresh);

		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						refreshNews();
					}
				});

	}

	private void initListView() {

		newsAdapter = new NewsAdapter(getApplicationContext());
		listView = (ListView) findViewById(R.id.main_listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Article article = (Article) parent.getItemAtPosition(position);

				Toast.makeText(
						getApplicationContext(),
						"" + article.description.text + "\n\n"
								+ article.channelTitle + "\n" + article.author
								+ "\n" + article.pubDate.dayOfWeek + "\n"
								+ article.pubDate.year, Toast.LENGTH_LONG)
						.show();

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
							handleArticles(articles);

							dismissProgressDialog();
							swipeRefreshLayout.setRefreshing(false);
						}

					});
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							swipeRefreshLayout.setRefreshing(false);
							dismissProgressDialog();
							showErrorToast();

						}

					});
				}
			}
		}).start();
	}

	private void dismissProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	private void handleArticles(ArrayList<Article> articles) {
		NewsSite news = new NewsSite(articles);
		newsAdapter.clear();
		Sort.onDate(news.articles);
		limitArticles(news.articles);
		pushToAdapter(news);
	}

	private void limitArticles(ArrayList<Article> articles) {

		int numberOfArticlesToShow = getResources().getInteger(
				R.integer.number_of_articles);

		while (articles.size() > numberOfArticlesToShow) {
			int lastIndex = articles.size() - 1;
			articles.remove(lastIndex);
		}

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

		// Get all feed links from resources.
		String[] urls = getResources().getStringArray(R.array.news_feeds);

		// Defines number of feed links
		int len = urls.length;

		// Loop through all feed links
		for (int i = 0; i < len; i++) {

			// Fetch one feed
			ArrayList<Article> newArticles = connection.get(urls[i], parser);

			// If it returns something
			if (newArticles != null) {

				// Loop through all the articles from feed
				for (int j = 0; j < newArticles.size(); j++) {

					// Get a single article
					Article newArticle = newArticles.get(j);

					// If article not already exists
					if (!alreadyGotItem(articles, newArticle)) {

						// Add the new article to the article array
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

}
