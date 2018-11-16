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

package org.jssec.android.broadcast.publicreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PublicReceiver extends BroadcastReceiver {

	private static final String MY_BROADCAST_PUBLIC =
		"org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";

	public boolean isDynamic = false;
	private String getName() {
		return isDynamic ? "Public Dynamic Broadcast Receiver" : "Public Static Broadcast Receiver";
	}

	@Override
	public void onReceive(Context context, Intent intent) {

        // *** POINT 2 *** Handle the received Intent carefully and securely.
        // Since this is a public broadcast receiver, the requesting application may be malware.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
		if (MY_BROADCAST_PUBLIC.equals(intent.getAction())) {
			String param = intent.getStringExtra("PARAM");
	    	Toast.makeText(context,
	    			String.format("%s:\nReceived param: \"%s\"", getName(), param),
	    			Toast.LENGTH_SHORT).show();
		}

		// *** POINT 3 *** When returning a result, do not include sensitive information.
		// Since this is a public broadcast receiver, the requesting application may be malware.
		// If no problem when the information is taken by malware, it can be returned as result.
		setResultCode(Activity.RESULT_OK);
		setResultData(String.format("Not Sensitive Info from %s", getName()));
		abortBroadcast();
	}
}