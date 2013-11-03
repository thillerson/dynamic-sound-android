package com.tackmobile.androidpiano.view;

import android.content.Context;
import android.view.View;

public class Key extends View {

	private int midiNote;
	private boolean blackKey;

	public Key(Context context, int midiNote, boolean blackKey) {
		super(context);
		this.setMidiNote(midiNote);
		this.setBlackKey(blackKey);
	}

	public int getMidiNote() {
		return midiNote;
	}

	public void setMidiNote(int midiNote) {
		this.midiNote = midiNote;
	}

	public boolean isBlackKey() {
		return blackKey;
	}

	public void setBlackKey(boolean blackKey) {
		int colorResource = (blackKey) ? android.R.color.black : android.R.color.white;
		this.setBackgroundColor(getResources().getColor(colorResource));
		this.blackKey = blackKey;
	}

}
