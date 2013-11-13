package com.tackmobile.spacerocks.audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import com.tackmobile.spacerocks.R;

import android.content.Context;

public class PdInterface {
  private static final int MIN_SAMPLE_RATE  = 44100;
  private static final String THRUSTER		= "thruster";
  private static final String MISSILE 		= "missile";

  private static PdInterface ourInstance = new PdInterface();
  private Context context;

  public static PdInterface getInstance() {
    return ourInstance;
  }

  private PdInterface() { }

  public void initialize(Context context) {
    this.context = context;
    try {
      initializePd();
    } catch (IOException e) {
      // Unable to initialize audio. Ignore silently as this is not fatal to the app
      e.printStackTrace();
    }
  }

  public void destroy() {
    cleanup();
  }
  
  public void fire() {
	  PdBase.sendList(MISSILE, 300, 400);
  }

  public void thrusterOn() {
	  PdBase.sendFloat(THRUSTER, 1);
  }
  
  public void thrusterOff() {
	  PdBase.sendFloat(THRUSTER, 0);
  }

  private void initializePd() throws IOException {
    AudioParameters.init(context);
    int sampleRate = Math.max(MIN_SAMPLE_RATE, AudioParameters.suggestSampleRate());
    int outChannels = AudioParameters.suggestOutputChannels();
    PdAudio.initAudio(sampleRate, 0, outChannels, 1, true);
    File dir = context.getFilesDir();
    File patchFile = new File(dir, "game_sounds.pd");
    InputStream patchStream = context.getResources().openRawResource(R.raw.patches);
    IoUtils.extractZipResource(patchStream, dir, true);

    PdBase.openPatch(patchFile.getAbsolutePath());
  }

  private void cleanup() {
    PdAudio.release();
    PdBase.release();
  }

}

