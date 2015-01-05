package com.jesperturesson.latestnewsfromexpressen.preferences;

import java.util.Comparator;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

public class CustomArticleComparator implements Comparator<Article> {

	@Override
	public int compare(Article lhs, Article rhs) {
		return rhs.pubDate.pubDate.compareTo(lhs.pubDate.pubDate);

	}
}