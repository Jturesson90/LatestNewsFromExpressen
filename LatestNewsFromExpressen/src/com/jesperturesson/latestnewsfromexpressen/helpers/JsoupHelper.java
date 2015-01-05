package com.jesperturesson.latestnewsfromexpressen.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class JsoupHelper {

	private final static String TAG = "JsoupHelper";

	public static String getBody(String desc) {
		String text = null;
		if (desc.contains("p")) {
			Document document = Jsoup.parse(desc);
			text = document.body().text();
		}
		return text;
	}

	public static String getImageUrl(String desc) {
		String imageUrl = null;
		if (desc.contains("img")) {
			Document document = Jsoup.parse(desc);
			Element img = document.select("img").first();
			if (desc.contains("src")) {
				imageUrl = img.attr("src");
			} else {
				Log.d(TAG, "No SRC at: " + desc);
			}
		}
		return imageUrl;
	}
}
