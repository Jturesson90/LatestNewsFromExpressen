package com.jesperturesson.latestnewsfromexpressen.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;

import android.util.Log;

import java.text.ParseException;

public class Article {
	public final static int	NUMBER_OF_ITEMS_TO_SHOW = 10;
	public final static String ARTICLE = "item";
	public final static String TITLE = "title";
	public final static String AUTHOR = "author";
	public final static String DESCRIPTION = "description";
	public final static String PUBDATE = "pubDate";
	public final static String LINK = "link";

	public String title;
	public String link;
	public ArticleTime pubDate;
	public String author;
	public Description description;
	public boolean clicked = false;

	public Article(String link, String title, String author,
			String description, String pubDate) {
		this.title = title;
		this.link = link;
		this.author = author;
		this.description = new Description(description);
		this.pubDate = new ArticleTime(pubDate);

	}


	@Override
	public String toString() {

		return "Title: " + title + "\nAuthor: " + author + "\nLink: " + link
				+ "\nDescription:" + description.text + "\n";
	}

	public class ArticleTime {
		public Date pubDate;
		public String dayOfWeek;
		public String year;
		public String date;
		public String dayOfMonth;
		public String time;

		public ArticleTime(String pubDate) {
			this.pubDate = stringToDate(pubDate);
			this.dayOfWeek = setDayOfWeek(pubDate);
			this.year = setYear(pubDate);
			this.date = setDate(pubDate);
			this.dayOfMonth = setDayOfMonth(pubDate);
			this.time = setTime(pubDate);
		}

		private Date stringToDate(String pubDate) {
			Date date = new Date(pubDate);

			return date;

		}

		private String setTime(String pubDate) {
			String[] items = pubDate.split(" ");
			String time = items[4];
			String[] items2 = time.split(":");
			time = items2[0] + ":" + items2[1];
			return time;

		}

		private String setDayOfMonth(String pubDate) {
			// TODO Auto-generated method stub
			return null;
		}

		private String setDate(String pubDate) {
			// TODO Auto-generated method stub
			return null;
		}

		private String setYear(String pubDate) {
			// TODO Auto-generated method stub
			return null;
		}

		private String setDayOfWeek(String pubDate) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public class Description {

		public Bitmap image;
		public String text;

		public Description(String desc) {
			text = desc;
		}
	}
}
