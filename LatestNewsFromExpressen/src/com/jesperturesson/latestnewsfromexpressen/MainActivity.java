package com.jesperturesson.latestnewsfromexpressen;

import java.util.ArrayList;

import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.NewsSite;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		
	}

	private void refreshNews() {
		 new Thread(new Runnable() {
	            @Override
	            public void run() {
	                connectionIsFine = false;
	                ServerConnection connection = new ServerConnection();
	                final XmlParser parser = new XmlParser();
	                
	                String[] urls = getResources().getStringArray(R.array.rss_urls);
	                final ArrayList<Article> articles = connection
	                        .get(urls[0], parser);
	                if (articles != null) {
	                    connectionIsFine = true;
	                }
	                if (connectionIsFine) {
	                   
	                    runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                        	NewsSite news = new NewsSite(articles);
	                        	pushToAdapter(news);
	                        	 Toast.makeText(getApplicationContext(), articles.get(0).toString() , Toast.LENGTH_LONG).show();
	                        	 swipeRefreshLayout.setRefreshing(false);
	                        }
	                    });
	                } else {
	                    runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                        	swipeRefreshLayout.setRefreshing(false);
	                            Toast.makeText(getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG).show();

	                        }
	                    });
	                }
	            }
	        }).start();
	}
	
	private void pushToAdapter(NewsSite news) {
		newsAdapter = new NewsAdapter(getApplication().getApplicationContext());
		listView.setAdapter(newsAdapter);
		for (Article article : news.articles) {
			newsAdapter.add(article);
		}
		newsAdapter.notifyDataSetChanged();
	}

}
