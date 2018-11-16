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
import android.widget.Toast;

public class PrivateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
        // *** POINT 2 *** Handle the received intent carefully and securely,
		// even though the intent was sent from within the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
		String param = intent.getStringExtra("PARAM");
    	Toast.makeText(context,
    			String.format("Received param: \"%s\"", param),
    			Toast.LENGTH_SHORT).show();
		
		// *** POINT 3 *** Sensitive information can be sent as the returned results since the requests come from within the same application.
		setResultCode(Activity.RESULT_OK);
		setResultData("Sensitive Info from Receiver");
		abortBroadcast();
	}
}
