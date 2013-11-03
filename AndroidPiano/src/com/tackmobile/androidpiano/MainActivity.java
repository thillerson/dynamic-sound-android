package com.tackmobile.androidpiano;

import org.puredata.android.io.PdAudio;

import android.app.Activity;
import android.os.Bundle;

import com.tackmobile.androidpiano.audio.PdInterface;

public class MainActivity extends Activity {

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

}
