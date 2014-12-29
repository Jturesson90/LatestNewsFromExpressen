package com.jesperturesson.latestnewsfromexpressen.models;

import java.util.ArrayList;

import android.util.Log;

public class NewsSite {

	private static final String TAG = "NEWS";

	public ArrayList<Article> articles;
	public String title;
	public String link;
	public String description;
	public String copyright;
	public String managingEditor;
	public RssImage image;

	public NewsSite(ArrayList<Article> arrayList) {
		this.articles = arrayList;
	}
}

class RssImage {
	public String bitmapUrl;
	public String title;
	public String link;
}
