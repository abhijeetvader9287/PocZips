/*
 * Copyright (C) 2012-2017 Japan Smartphone Security Association
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jssec.android.broadcast.privatereceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrivateSenderActivity extends Activity {

	public void onSendNormalClick(View view) {
    	// *** POINT 4 *** Use the explicit Intent with class specified to call a receiver within the same application.
		Intent intent = new Intent(this, PrivateReceiver.class);

		// *** POINT 5 *** Sensitive information can be sent since the destination Receiver is within the same application.
		intent.putExtra("PARAM", "Sensitive Info from Sender");
		sendBroadcast(intent);
	}
	
	public void onSendOrderedClick(View view) {
    	// *** POINT 4 *** Use the explicit Intent with class specified to call a receiver within the same application.
		Intent intent = new Intent(this, PrivateReceiver.class);

		// *** POINT 5 *** Sensitive information can be sent since the destination Receiver is within the same application.
		intent.putExtra("PARAM", "Sensitive Info from Sender");
		sendOrderedBroadcast(intent, null, mResultReceiver, null, 0, null, null);
	}
	
	private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
	        // *** POINT 6 *** Handle the received result data carefully and securely,
			// even though the data came from the Receiver within the same application.
			// Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
			String data = getResultData();
			PrivateSenderActivity.this.logLine(
					String.format("Received result: \"%s\"", data));
		}
	};
	
	private TextView mLogView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }
	
	private void logLine(String line) {
		mLogView.append(line);
		mLogView.append("\n");
	}
}