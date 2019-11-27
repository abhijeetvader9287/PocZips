package com.profile.changer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ProfileChangerActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
	
	ToggleButton tbt;
	TextView txtview;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tbt = (ToggleButton)findViewById(R.id.togglebutton);
        txtview = (TextView) findViewById(R.id.textview);
        txtview.setText("Welcome to Profile Changer Application");
        final AudioManager mobilemode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        
        tbt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(tbt.getText().toString().equals("Switch to LOUD"))
				{
					mobilemode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					txtview.setText("SILENT profile activated !");
					Toast.makeText(getBaseContext(),"SILENT profile activated ",Toast.LENGTH_LONG).show();
				}
				else if(tbt.getText().toString().equals("Switch to SILENT"))
				{
					mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					txtview.setText("LOUD profile activated !");
					Toast.makeText(getBaseContext(),"LOUD profile activated !",Toast.LENGTH_LONG).show();
					
				}
				
			}
		});
    }
}