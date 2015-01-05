package com.jesperturesson.latestnewsfromexpressen.helpers;

import java.util.ArrayList;
import java.util.Collections;
import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.preferences.CustomArticleComparator;

public class Sort {

	public static void onDate(ArrayList<Article> articles) {
		Collections.sort(articles, new CustomArticleComparator());
	}

}
