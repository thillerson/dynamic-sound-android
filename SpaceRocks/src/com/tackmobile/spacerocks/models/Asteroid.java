package com.tackmobile.spacerocks.models;

import com.tackmobile.spacerocks.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Asteroid {
	
	public Asteroid(Context context) {
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid);
	}

	public Point point;
	public Bitmap bitmap;

}
