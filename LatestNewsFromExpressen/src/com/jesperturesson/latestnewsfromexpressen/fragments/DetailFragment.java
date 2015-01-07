package com.jesperturesson.latestnewsfromexpressen.fragments;

import com.jesperturesson.latestnewsfromexpressen.models.Article;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DetailFragment extends Fragment {

	private static final String TAG = "DetailFragment";

	private static final String ARG_RESOURCE_ID = "resource_id";
	private static final String ARG_TITLE = "title";
	private static final String ARG_X = "x";
	private static final String ARG_Y = "y";
	private static final String ARG_WIDTH = "width";
	private static final String ARG_HEIGHT = "height";
	//final Article article;

	public static DetailFragment newInstance(int resourceId, Article article,
			int x, int y, int width, int height) {
		DetailFragment fragment = new DetailFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_RESOURCE_ID, resourceId);
		// args.putString(ARG_TITLE, title);
		//this.article = article;
		args.putInt(ARG_X, x);
		args.putInt(ARG_Y, y);
		args.putInt(ARG_WIDTH, width);
		args.putInt(ARG_HEIGHT, height);
		fragment.setArguments(args);
		return fragment;
	}
}
