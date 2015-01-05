package com.jesperturesson.latestnewsfromexpressen.models;

import java.util.Date;
import com.jesperturesson.latestnewsfromexpressen.helpers.JsoupHelper;

public class Article {

	public final static String TAG = "Article";
	public String title;
	public String link;
	public ArticleTime pubDate;
	public String author;
	public Description description;
	public String channelTitle;
	public String channelLink;
	public String channelCopyright;
	public String channelDescription;
	public String channelManagingEditor;

	public Article(String link, String title, String author,
			String description, String pubDate, String channelTitle,
			String channelLink, String channelDescription,
			String channelCopyright, String channelManagingEditor) {
		this.title = title;
		this.link = link;
		this.author = author;
		this.description = new Description(description);
		this.pubDate = new ArticleTime(pubDate);
		this.channelTitle = channelTitle;
		this.channelLink = channelLink;
		this.channelCopyright = channelCopyright;
		this.channelDescription = channelDescription;
		this.channelManagingEditor = channelManagingEditor;
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
		public String month;
		public String dayOfMonth;
		public String time;

		public ArticleTime(String pubDate) {
			this.pubDate = stringToDate(pubDate);
			this.dayOfWeek = setDayOfWeek(pubDate);
			this.year = setYear(pubDate);
			this.month = setMonth(pubDate);
			this.dayOfMonth = setDayOfMonth(pubDate);
			this.time = setTime(pubDate);
		}

		@SuppressWarnings("deprecation")
		private Date stringToDate(String pubDate) {
			Date pubDate1 = new Date(pubDate);
			return pubDate1;
		}

		private String setTime(String pubDate) {
			String[] items = pubDate.split(" ");
			String time = items[4];
			String[] items2 = time.split(":");
			time = items2[0] + ":" + items2[1];
			return time;

		}

		private String setDayOfMonth(String pubDate) {
			String[] items = pubDate.split(" ");
			String dayOfMonth = items[1];
			return dayOfMonth;
		}

		private String setMonth(String pubDate) {
			String[] items = pubDate.split(" ");
			String month = items[2];
			return month;
		}

		private String setYear(String pubDate) {
			String[] items = pubDate.split(" ");
			String year = items[3];
			return year;
		}

		private String setDayOfWeek(String pubDate) {
			String[] items = pubDate.split(" ");
			String day = items[0];
			day = day.replace(",", "");
			return day;
		}
	}

	public class Description {

		public String imageLink;
		public String text;

		public Description(String desc) {
			this.text = parseBody(desc);
			this.imageLink = parsePicture(desc);
		}

		private String parsePicture(String desc) {
			return JsoupHelper.getImageUrl(desc);
		}

		private String parseBody(String desc) {
			return JsoupHelper.getBody(desc);

		}
	}
}
