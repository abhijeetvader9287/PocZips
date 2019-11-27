package com.abhijeet.voice_mod;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.newventuresoftware.waveform.WaveformView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.functions.Consumer;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
public class MainActivity extends AppCompatActivity {
    TextView txtRecoder;
    TextView txtPlay;
    TextView txtStopRecording;
    File file;
    AudioTrack audioTrack;
    private Boolean recording;
    WaveformView mRealtimeWaveformView;
      WaveformView mPlaybackView;
    private PlaybackThread mPlaybackThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtRecoder = findViewById(R.id.txtRecoder);
        txtPlay = findViewById(R.id.txtPlay);
        txtStopRecording = findViewById(R.id.txtStopRecording);

          mPlaybackView = (WaveformView) findViewById(R.id.playbackWaveformView);
          mRealtimeWaveformView = (WaveformView) findViewById(R.id.waveformView);
        final RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        txtRecoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxPermissions.request(RECORD_AUDIO, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE).subscribe((new Consumer() {
                    public void accept(Object accept) {
                        accept((Boolean) accept);
                    }

                    final void accept(Boolean granted) {
                        if (granted) {
                           /* Intent intent = new Intent(MainActivity.this, RecordingDialog.class);
                            startActivity(intent);*/
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    recording = true;
                                    startRec();
                                }
                            }).start();
                        } else {
                            Toast.makeText(MainActivity.this, "not allowed", Toast.LENGTH_LONG).show();
                        }
                    }
                }));
            }
        });
        txtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file!=null) {
                    if (file.exists()) {
                        playRec();
                        if (!mPlaybackThread.playing()) {
                            mPlaybackThread.startPlayback();
                        } else {
                            mPlaybackThread.stopPlayback();
                        }
                    }
                }
            }
        });
        txtStopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recording = false;
                if (audioTrack != null) {
                    audioTrack.release();
                }
            }
        });
    }

    private void playRec() {
        int i = 0;
        String str = "Ghost";
        int shortSizeInByte = Short.SIZE / Byte.SIZE;
        int bufferSizeInBytes = (int) file.length() / shortSizeInByte;
        short[] audioData = new short[bufferSizeInBytes];
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
            int j = 0;
            try {
                while (dataInputStream.available() > 0) {
                    try {
                        audioData[j] = dataInputStream.readShort();
                        j++;
                    } catch (Exception ex) {
                    }
                }
                dataInputStream.close();

                //  50400, 50000, 48000,47250, 44100, 44056, 37800, 32000, 22050, 16000, 11025, 4800, 8000
                // slow motion=6050,
                // Robot=8500,
                // Normal=11025,
                // chipmunk=22050,
                // funny=41000,
                // elephnat=30000
                if (str.equals("Ghost")) {
                    i = 22050;
                }
               // audioTrack = new AudioTrack(3, i, 2, 2, bufferSizeInBytes, 1);
                //audioTrack.play();
               // audioTrack.write(audioData, 0, bufferSizeInBytes);
                mPlaybackThread = new PlaybackThread(i,audioData, new PlaybackListener() {
                    @Override
                    public void onProgress(int progress) {
                        mPlaybackView.setMarkerPosition(progress);
                    }
                    @Override
                    public void onCompletion() {
                        mPlaybackView.setMarkerPosition(mPlaybackView.getAudioLength());

                    }
                });
                mPlaybackView.setChannels(1);
                mPlaybackView.setSampleRate(i);
                mPlaybackView.setSamples(audioData);


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startRec() {
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.pcm");
        try {
            file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
            int minBufferSize = AudioTrack.getMinBufferSize(11025, 2, 2);
            short[] audioData = new short[minBufferSize];
            AudioRecord audioRecord = new AudioRecord(1, 11025, 2, 2, minBufferSize);
            audioRecord.startRecording();
            while (recording) {
                int numberOfShorts = audioRecord.read(audioData, 0, minBufferSize);
                for (int i = 0; i < numberOfShorts; i++) {
                    dataOutputStream.writeShort(audioData[i]);
                    mRealtimeWaveformView.setSamples(audioData);
                }
            }
            if (!recording.booleanValue()) {
                audioRecord.stop();
                dataOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recording = false;
        if (audioTrack != null) {
            audioTrack.release();
        }
    }
}
