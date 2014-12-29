package com.jesperturesson.latestnewsfromexpressen.models;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import java.text.ParseException;


public class Article {
	public final static String ARTICLE = "item";
	public final static String TITLE = "title";
	public final static String AUTHOR = "author";
	public final static String DESCRIPTION = "description";
	public final static String PUBDATE = "pubDate";
	public final static String LINK = "link";

	
	
	public String title;
	public String link;
	public String author;
	public Description description;
	public Date pubDate;
	
	
	public Article(String link, String title, String author, String description, String pubDate){
		this.title = title;
		this.link = link;
		this.author = author;
		this.description = new Description(description);
	//	this.date = stringToDate(pubDate);
	}
	private Date stringToDate(String pubDate){
		Date date = null;
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
		SimpleDateFormat format = new SimpleDateFormat("E',' dd yyyy HH:mm:ss");
		try {  
		    date = format.parse(pubDate);
		    return date;
		} catch (ParseException e) {  
		    e.printStackTrace();  
		    return date;
		}
		
	}
	@Override
	public String toString() {
		
		return "Title: "+title+"\nAuthor: "+author+"\nLink: "+link+"\nDescription:"+description.text+"\n";
	}
	
	public class Description {
		
		public Bitmap image;
		public String text;	
		public Description(String desc){
			text = desc;
		}
	}
}


