package com.jesperturesson.latestnewsfromexpressen;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

import android.content.Context;
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
					R.layout.news_list_row, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.news_row_title);
		TextView description = (TextView) convertView.findViewById(R.id.news_row_description);
		Article article = getItem(position);
		title.setText(article.title);
		description.setText(article.description.text);
		return convertView;
	}
}
