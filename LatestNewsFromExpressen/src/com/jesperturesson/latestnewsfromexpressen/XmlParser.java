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

	/**
	 * Constants
	 */
	public final static String ITEM = "item";
	public final static String TITLE = "title";
	public final static String AUTHOR = "author";
	public final static String DESCRIPTION = "description";
	public final static String PUBDATE = "pubDate";
	public final static String LINK = "link";
	public final static String COPYRIGHT = "copyright";
	public final static String MANAGING_EDITOR = "managingEditor";

	private static final String TAG = "XMLPARSER";
	private static final String ns = null;
	private String channelTitle;
	private String channelDescription;
	private String channelCopyright;
	private String channelLink;
	private String channelManagingEditor;

	@Override
	public ArrayList<Article> parse(InputStream in)
			throws XmlPullParserException, IOException {
		Log.d(TAG, "Parse Start");

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
		Log.d(TAG, "Read channel");
		parser.require(XmlPullParser.START_TAG, ns, "rss");
		ArrayList<Article> items = null;
		while (parser.next() != XmlPullParser.END_TAG) {

			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("channel")) {
				Log.d(TAG, "Found Channel");
				items = readFeed(parser);
			} else {
				skip(parser);
			}

		}
		return items;
	}

	private ArrayList<Article> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Log.d(TAG, "Read feed");
		ArrayList<Article> items = new ArrayList<Article>();
		parser.require(XmlPullParser.START_TAG, ns, "channel");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(XmlParser.TITLE)) {
				channelTitle = readTitle(parser);
			} else if (name.equals(XmlParser.ITEM)) {
				items.add(readArticle(parser));
			} else if (name.equals(XmlParser.COPYRIGHT)) {
				channelCopyright = readCopyright(parser);
			} else if (name.equals(XmlParser.MANAGING_EDITOR)) {
				channelManagingEditor = readManagingEditor(parser);
			} else if (name.equals(XmlParser.DESCRIPTION)) {
				channelDescription = readDescription(parser);
			} else {
				skip(parser);
			}

		}
		return items;
	}

	private Article readArticle(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, XmlParser.ITEM);
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
			if (name.equals(XmlParser.TITLE)) {
				title = readTitle(parser);
			} else if (name.equals(XmlParser.AUTHOR)) {
				author = readAuthor(parser);
			} else if (name.equals(XmlParser.DESCRIPTION)) {
				description = readDescription(parser);
			} else if (name.equals(XmlParser.LINK)) {
				link = readLink(parser);
			} else if (name.equals(XmlParser.PUBDATE)) {
				pubDate = readPubDate(parser);
			} else {
				skip(parser);
			}

		}
		Article article = new Article(link, title, author, description,
				pubDate, channelTitle, channelLink, channelDescription,
				channelCopyright, channelManagingEditor);
		Log.d("SUCCESS", article.toString());
		return article;
	}

	private String readPubDate(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, XmlParser.PUBDATE);
		String pubDate = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.PUBDATE);
		return pubDate;
	}

	private String readLink(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, XmlParser.LINK);
		String link = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.LINK);
		return link;
	}

	private String readDescription(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, XmlParser.DESCRIPTION);
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.DESCRIPTION);
		return description;
	}

	private String readAuthor(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, XmlParser.AUTHOR);
		String author = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.AUTHOR);
		return author;
	}

	private String readTitle(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns, XmlParser.TITLE);
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.TITLE);
		return title;

	}

	private String readCopyright(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns, XmlParser.COPYRIGHT);
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.COPYRIGHT);
		return title;

	}

	private String readManagingEditor(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns, XmlParser.MANAGING_EDITOR);
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, XmlParser.MANAGING_EDITOR);
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
