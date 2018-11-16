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
package org.jssec.android.service.privateservice;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class PrivateIntentService extends IntentService {
	
	// Default constructor must be implemented when the service extends IntentService class.
	public PrivateIntentService() {
		super("PrivateIntentService");
	}

    // The onCreate gets called only one time when the Service starts.
    @Override
    public void onCreate() {
    	super.onCreate();
        Toast.makeText(this, "PrivateIntentService - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
	// The onHandleIntent gets called each time after the startService gets called.
    @Override
    protected void onHandleIntent(Intent intent) {    	
        // *** POINT 2 *** Handle the received intent carefully and securely,
    	// even though the intent was sent from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
		String param = intent.getStringExtra("PARAM");
    	Toast.makeText(this,
    			String.format("PrivateIntentService\nReceived param: \"%s\"", param),
    			Toast.LENGTH_LONG).show();

    	// Long term operation is allowed here
        // since the onHandleIntent gets called from a worker thread.
        try {
            // sleep 5 seconds
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // The onDestroy gets called only one time when the service stops.
    @Override
    public void onDestroy() {
        Toast.makeText(this, "PrivateIntentService - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
}
