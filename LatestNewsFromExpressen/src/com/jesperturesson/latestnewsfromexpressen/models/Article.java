package com.jesperturesson.latestnewsfromexpressen.models;

import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Article {
	public final static int NUMBER_OF_ITEMS_TO_SHOW = 10;
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
			// TODO Auto-generated method stub
			return null;
		}

		private String setDayOfWeek(String pubDate) {
			// TODO Auto-generated method stub
			return null;
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
			String src = null;
			if (desc.contains("img")) {
				Document document = Jsoup.parse(desc);
				Element img = document.select("img").first();
				src = img.attr("src");
			}
			return src;
		}

		private String parseBody(String desc) {
			String text = null;
			if (desc.contains("p")) {
				Document document = Jsoup.parse(desc);
				text = document.body().text();
			}
			return text;

		}
	}
}
