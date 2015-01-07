package com.jesperturesson.latestnewsfromexpressen.fragments;

import com.jesperturesson.latestnewsfromexpressen.R;
import com.jesperturesson.latestnewsfromexpressen.activities.MainActivity;
import com.jesperturesson.latestnewsfromexpressen.adapters.NewsAdapter;
import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.NewsSite;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class NewslistFragment extends Fragment {

	SwipeRefreshLayout swipeRefreshLayout;
	NewsAdapter newsAdapter;
	ListView listView;
	NewsSite news;
	LayoutInflater mInflater;

	public NewslistFragment(NewsSite news) {
		this.news = news;
	}

	public void updateNews(NewsSite news) {
		newsAdapter.clear();
		pushToAdapter(news);
		swipeRefreshLayout.setRefreshing(false);
	}

	public void errorWhenUpdate() {
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initAdapter(inflater);
		mInflater = inflater;
		return inflater.inflate(R.layout.news_list_view, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		init();
		pushToAdapter(news);

		super.onViewCreated(view, savedInstanceState);

	}

	private void pushToAdapter(NewsSite news) {

		for (Article article : news.articles) {
			newsAdapter.add(article);
		}
		newsAdapter.notifyDataSetChanged();

	}

	private void init() {
		initListView();
		initSwipeRefreshLayout();
	}

	private void initAdapter(LayoutInflater inflater) {
		newsAdapter = new NewsAdapter(getActivity().getApplicationContext(),
				inflater, R.layout.news_list_row);
	}

	private void initSwipeRefreshLayout() {
		swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(
				R.id.main_swipe_to_refresh);

		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						((MainActivity) getActivity()).refreshNews();

					}
				});
	}

	private void initListView() {

		listView = (ListView) getView().findViewById(R.id.main_listView);
		listView.setAdapter(newsAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Article article = newsAdapter.getItem(position);
				showArticleToast(article);

			}

		});

	}

	@SuppressWarnings("unused")
	private void showArticleToast(Article article) {
		// TODO Auto-generated method stub
		Toast.makeText(
				getActivity().getApplicationContext(),
				"" + article.description.text + "\n\n" + article.channelTitle
						+ "\n" + article.author + "\n"
						+ article.pubDate.dayOfWeek + "\n"
						+ article.pubDate.year, Toast.LENGTH_LONG).show();
	}
}
