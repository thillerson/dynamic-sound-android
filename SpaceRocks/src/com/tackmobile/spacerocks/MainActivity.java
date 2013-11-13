package com.tackmobile.spacerocks;

import org.puredata.android.io.PdAudio;

import com.tackmobile.spacerocks.audio.PdInterface;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;

public class MainActivity extends Activity {
	
	protected static final long FRAME_RATE = 20;
	
	GameBoard gameBoard;
	Handler handler = new Handler();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PdInterface.getInstance().initialize(this);
	}

	@Override protected void onStart() {
		super.onStart();
		PdAudio.startAudio(this);
	}

	@Override protected void onStop() {
		PdAudio.stopAudio();
		super.onStop();
	}

	@Override protected void onDestroy() {
		PdInterface.getInstance().destroy();
		super.onDestroy();
	}

	@Override protected void onResume() {
		super.onResume();
		gameBoard = (GameBoard)findViewById(R.id.game_board);
		handler.post(frame);
	}
	
	Runnable frame = new Runnable() {
		@Override public void run() {
			gameBoard.onFrame();
			handler.postDelayed(frame, FRAME_RATE);
		}
	};

}
