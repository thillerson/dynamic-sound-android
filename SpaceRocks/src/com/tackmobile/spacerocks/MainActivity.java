package com.tackmobile.spacerocks;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;

public class MainActivity extends Activity {
	
	protected static final long FRAME_RATE = 20;
	
	GameBoard gameBoard;
	Handler handler = new Handler();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override protected void onResume() {
		super.onResume();
		gameBoard = (GameBoard)findViewById(R.id.game_board);
		handler.post(frame);
	}
	
	Runnable frame = new Runnable() {
		@Override public void run() {
			gameBoard.updatePositions();
			handler.postDelayed(frame, FRAME_RATE);
		}
	};

}
