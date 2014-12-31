package com.jesperturesson.latestnewsfromexpressen;

import com.jesperturesson.latestnewsfromexpressen.models.Article;
import com.jesperturesson.latestnewsfromexpressen.models.BitmapFunctions;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<Article> {

	View view;;

	public NewsAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		view = convertView;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.news_list_row, parent, false);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.news_row_title);

		TextView time = (TextView) convertView.findViewById(R.id.news_row_time);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.news_row_imageView);

		Article article = getItem(position);
		if (article.description.imageLink != null) {
			setImage(article.description.imageLink, imageView);

		}
		time.setText(article.pubDate.time + " " + article.pubDate.dayOfMonth
				+ " " + article.pubDate.month);
		title.setText(article.title);

		return convertView;
	}

	private void setImage(String imageLink, final ImageView imageView) {
		BitmapFunctions.getBitmapFromURL(imageLink, new ImageSetListener() {

			@Override
			public void onImageSet(Bitmap image) {
				if (image != null) {
					imageView.setImageBitmap(image);
					imageView.setVisibility(View.VISIBLE);
				}

			}
		});

	}

	public interface ImageSetListener {
		public void onImageSet(Bitmap image);
	}
}
