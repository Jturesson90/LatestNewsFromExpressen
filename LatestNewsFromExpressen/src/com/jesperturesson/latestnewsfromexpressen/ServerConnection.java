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
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            String message = connection.getResponseMessage();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                articles = parser.parse(input);
               
                
                
                
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return articles;
    }
   
}