package com.jesperturesson.latestnewsfromexpressen.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jesperturesson.latestnewsfromexpressen.NewsAdapter;

public class Sort {

	public static void onDate(ArrayList<Article> articles) {
		Collections.sort(articles, new CustomArticleComparator());
	}


}
