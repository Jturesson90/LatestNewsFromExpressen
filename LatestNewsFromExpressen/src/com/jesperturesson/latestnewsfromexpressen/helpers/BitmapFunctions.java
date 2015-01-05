package com.jesperturesson.latestnewsfromexpressen.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jesperturesson.latestnewsfromexpressen.activities.MainActivity;
import com.jesperturesson.latestnewsfromexpressen.interfaces.CustomInterfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapFunctions {
	private final static String TAG = "BitmapFunctions";

	public static void getBitmapFromURL(final String src,
			final CustomInterfaces.ImageSetListener imageSetListener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(src);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					final Bitmap myBitmap = BitmapFactory.decodeStream(input);
					MainActivity.mainActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							imageSetListener.onImageSet(myBitmap);
						}
					});
				} catch (IOException e) {
					Log.e(TAG,
							"Error when trying to get image: " + e.toString());
				}

			}

		}).start();

	}
}
