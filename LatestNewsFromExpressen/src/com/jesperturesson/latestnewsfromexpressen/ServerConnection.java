package com.jesperturesson.latestnewsfromexpressen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

import android.util.Log;

public class ServerConnection {

	public static final String TAG = "ServerConnection";
	HttpURLConnection connection;

	public ServerConnection() {

	}

	public ArrayList<Article> get(final String url, Parser parser) {
		ArrayList<Article> articles = new ArrayList<Article>();
		Log.d(TAG, "START");
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setReadTimeout(10000 /* milliseconds */);
			connection.setConnectTimeout(15000 /* milliseconds */);
			connection.setRequestMethod("GET");
			connection.setRequestProperty ("User-agent", "mozilla");
			connection.setDoInput(true);

			connection.connect();
			int statusCode = connection.getResponseCode();
			String message = connection.getResponseMessage();
			Log.d(TAG, "StatusCode: " + statusCode + "\t Message: " + message);
			if (statusCode == HttpURLConnection.HTTP_OK) {
				Log.d(TAG, "HTTP_OK");
				InputStream input = connection.getInputStream();
				articles = parser.parse(input);

			} else {
				articles = null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.d(TAG, "Error " + e.toString());
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			Log.d(TAG, "Error " + e.toString());

			return null;
		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error " + e.toString());

			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		if (articles != null) {
			Log.d(TAG, "Success! Size of articles: " + articles.size());
		}
		return articles;
	}

}