package com.sonicgladiators.listeners;

import android.media.MediaRecorder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.sonicgladiators.MainActivity;
import com.sonicgladiators.R;
import com.sonicgladiators.constants.ClientConstants;

import java.io.File;

public class RecordingListener implements View.OnTouchListener {

    private MediaRecorder mediaRecorder;
    private MainActivity mainActivity;
    private static final String TAG = "RecordingListener";

    private float firstX = -10.0f;
    private float firstY = -10.0f;
    private final static float MAX_DIFF = 75.0f;


    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(ClientConstants.TEMP_FILE_NAME);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setAudioEncodingBitRate(16);
        mediaRecorder.setAudioSamplingRate(44100);

        Button recordButton = (Button) mainActivity.findViewById(R.id.RecordButton);
        recordButton.setPressed(true);
        recordButton.setText("Recording...");

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            Log.i(TAG, "prepare() failed");
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException stopException) {
                //Log.i(TAG, "StopException thrown", stopException);
            }
            mediaRecorder.release();
            mediaRecorder = null;
        }

        Button recordButton = (Button) mainActivity.findViewById(R.id.RecordButton);
        recordButton.setPressed(false);
        recordButton.setText("Record");
    }

    private void saveRecording() {
        File file = new File(ClientConstants.TEMP_FILE_NAME);
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        File fileDest = new File(ClientConstants.AUDIO_FILE_NAME);
        boolean success = file.renameTo(fileDest);
        Log.i(TAG, "success: " + success);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //start recording
            Log.i(TAG, "Start Recording");
            startRecording();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //stop recording
            Log.i(TAG, "Stop Recording");
            stopRecording();
            Log.i(TAG, "Save recording");
            saveRecording();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
            // cancel the recording and use the old one (don't overwrite)
            Log.i(TAG, "Stop Recording and don't save");

            if(firstX == -10.0f && firstY == -10.0f) { //this is terrible
                firstX = motionEvent.getX();
                firstY = motionEvent.getY();
            }

            if(Math.abs(firstX - motionEvent.getX()) > MAX_DIFF || Math.abs(firstY - motionEvent.getY()) > MAX_DIFF) {
                stopRecording();
            }
        }
        return true;
    }
}
