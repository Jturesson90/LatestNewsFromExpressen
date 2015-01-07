package com.jesperturesson.latestnewsfromexpressen.adapters;

import com.jesperturesson.latestnewsfromexpressen.R;

import com.jesperturesson.latestnewsfromexpressen.helpers.BitmapFunctions;
import com.jesperturesson.latestnewsfromexpressen.interfaces.CustomListener;
import com.jesperturesson.latestnewsfromexpressen.models.Article;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<Article> {
	private final LayoutInflater mLayoutInflater;
	public ViewGroup viewGroup;
	private final int mResourceId;
	View convertView;

	public NewsAdapter(Context context, LayoutInflater inflater, int newsListRow) {
		super(context, 0);
		mLayoutInflater = inflater;
		mResourceId = newsListRow;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Article article;
		viewGroup = parent;
		this.convertView = convertView;
		final View view;
		if (convertView == null) {
			view = mLayoutInflater.inflate(mResourceId, parent, false);
			// convertView = LayoutInflater.from(getContext()).inflate(
			// R.layout.news_list_row, parent, false);
			article = getItem(position);
			initObjects(article, view);

		} else {
			view = convertView;
			article = (Article) view.getTag();

		}

		return view;
	}

	private void initObjects(Article article, View view) {
		// Title TextView
		TextView title = (TextView) view.findViewById(R.id.news_row_title);
		// Time TextView
		TextView time = (TextView) view.findViewById(R.id.news_row_time);

		// Image
		ImageView imageView = (ImageView) view
				.findViewById(R.id.news_row_imageView);

		if (article.description.imageLink != null) {
			setImage(article.description.imageLink, imageView);

		} else {
			imageView.setVisibility(View.GONE);
		}
		time.setText(article.pubDate.time + " " + article.pubDate.month + " "
				+ article.pubDate.dayOfMonth);
		title.setText(article.title);
		view.setTag(article);

	}

	private void setImage(String imageLink, final ImageView imageView) {
		BitmapFunctions.getBitmapFromURL(imageLink,
				new CustomListener.ImageSetListener() {
					@Override
					public void onImageSet(Bitmap image) {
						if (image != null) {
							imageView.setImageBitmap(image);
							imageView.setVisibility(View.VISIBLE);
						}

					}
				});
	}

}
