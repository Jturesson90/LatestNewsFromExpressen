package com.jesperturesson.latestnewsfromexpressen.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;


public abstract class Parser {
	
	public abstract ArrayList<Article> parse(InputStream data) throws XmlPullParserException, IOException;
}
