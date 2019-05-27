package com.example.myapplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cipherlab.barcode.GeneralString;
import com.cipherlab.barcode.ReaderManager;
public class MainActivity extends AppCompatActivity {
    Button button;
    IntentFilter intentFilter;
    ReaderManager readerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        readerManager = ReaderManager.InitInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(GeneralString.Intent_SOFTTRIGGER_DATA);
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GeneralString.Intent_SOFTTRIGGER_DATA)) {
                    String data = intent.getStringExtra(GeneralString.BcReaderData);
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readerManager != null) {
                    readerManager.SoftScanTrigger();
                }
            }
        });
    }
}
