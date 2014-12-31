package com.jesperturesson.latestnewsfromexpressen.models;

import java.util.Comparator;

public class CustomArticleComparator implements Comparator<Article> {

	@Override
	public int compare(Article lhs, Article rhs) {
		return rhs.pubDate.pubDate.compareTo(lhs.pubDate.pubDate);

	}
}