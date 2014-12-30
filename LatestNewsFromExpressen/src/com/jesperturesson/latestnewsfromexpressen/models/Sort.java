package com.jesperturesson.latestnewsfromexpressen.models;

import java.util.Comparator;

import com.jesperturesson.latestnewsfromexpressen.NewsAdapter;

public class Sort {
	public static void onDate(NewsAdapter newsAdapter) {

		newsAdapter.sort(new Comparator<Article>() {

			@Override
			public int compare(Article lhs, Article rhs) {
				// TODO Auto-generated method stub
				return rhs.pubDate.pubDate.compareTo(lhs.pubDate.pubDate);
			}
		});
	}
}