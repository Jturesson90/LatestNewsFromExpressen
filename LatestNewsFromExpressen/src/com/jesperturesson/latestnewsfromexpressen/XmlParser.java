package com.jesperturesson.latestnewsfromexpressen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

import android.util.Log;
import android.util.Xml;

public class XmlParser extends Parser {
	private static final String TAG = "XMLPARSER";
	private static final String ns = null;

	@Override
	public ArrayList<Article> parse(InputStream in) throws XmlPullParserException,
			IOException {
		Log.d(TAG,"Parse Start");
        
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readChannel(parser);

		} finally {
			in.close();
		}

	}

	private ArrayList<Article> readChannel(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Log.d(TAG,"Read channel");
		parser.require(XmlPullParser.START_TAG, ns, "rss");
		ArrayList<Article> items = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("channel")) {
				Log.d(TAG,"Found Channel");
				items = readFeed(parser);
			} else {
				skip(parser);
			}

		}
		return items;
	}

	private ArrayList<Article> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Log.d(TAG,"Read feed");
		ArrayList<Article> items = new ArrayList<Article>();
		parser.require(XmlPullParser.START_TAG, ns, "channel");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			
			if (name.equals(Article.ARTICLE)) {
				Log.d(TAG,"Found Article");
				items.add(readArticle(parser));
			} else {
				skip(parser);
			}

		}
		return items;
	}

	private Article readArticle(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, Article.ARTICLE);
		String title = null;
		String author = null;
		String description = null;
		String pubDate = null;
		String link = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			Log.d("TEST", "Article: " + name);
			if (name.equals(Article.TITLE)) {
				title = readTitle(parser);
			} else if (name.equals(Article.AUTHOR)) {
				author = readAuthor(parser);
			} else if (name.equals(Article.DESCRIPTION)) {
				description = readDescription(parser);
			} else if (name.equals(Article.LINK)) {
				link = readLink(parser);
			} else if (name.equals(Article.PUBDATE)) {
				pubDate = readPubDate(parser);
			} else {
				skip(parser);
			}

		}
		Article article = new Article(link, title, author, description, pubDate);
		Log.d("SUCCESS", article.toString());
		return article;
	}

	private String readPubDate(XmlPullParser parser)throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, Article.PUBDATE);
		String pubDate = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, Article.PUBDATE);
		return pubDate;
	}

	private String readLink(XmlPullParser parser)throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, Article.LINK);
		String link = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, Article.LINK);
		return link;
	}

	private String readDescription(XmlPullParser parser)throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, Article.DESCRIPTION);
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, Article.DESCRIPTION);
		return description;
	}

	private String readAuthor(XmlPullParser parser)throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, Article.AUTHOR);
		String author = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, Article.AUTHOR);
		return author;
	}

	private String readTitle(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns, Article.TITLE);
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, Article.TITLE);
		return title;

	}

	private String readText(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}

		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}

		int depth = 1;

		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;

			}
		}

	}
}
