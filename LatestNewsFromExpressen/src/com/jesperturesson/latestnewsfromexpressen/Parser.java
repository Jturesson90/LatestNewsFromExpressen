package com.jesperturesson.latestnewsfromexpressen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

public abstract class Parser {
	
	public abstract ArrayList<Article> parse(InputStream data) throws XmlPullParserException, IOException;
}
