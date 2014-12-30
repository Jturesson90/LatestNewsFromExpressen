package com.jesperturesson.latestnewsfromexpressen;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<Article> {

	public NewsAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.news_list_row, parent, false);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.news_row_title);
		TextView description = (TextView) convertView
				.findViewById(R.id.news_row_description);
		TextView time = (TextView) convertView.findViewById(R.id.news_row_time);
		time.setText("12.12");
		Article article = getItem(position);
		Log.d("NEWS", article.title);
		if (article.clicked) {
			Log.d("NEWS", "IS CLICKED at " + position);
			description.setVisibility(View.VISIBLE);
		} else {
			description.setVisibility(View.GONE);
		}
		title.setText(article.title);
		description.setText(article.description.text);
		return convertView;
	}
}
