package com.abhijeet.voice_mod;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.newventuresoftware.waveform.WaveformView;
public class RecordingDialog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_dialog);
        WaveformView     mRealtimeWaveformView = (WaveformView) findViewById(R.id.waveformView);
    }
}
