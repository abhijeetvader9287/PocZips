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

package org.jssec.android.activity.publicuser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PublicUserActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onUseActivityClick(View view) {
    	
    	try {
    		// *** POINT 4 *** Do not send sensitive information.
	        Intent intent = new Intent("org.jssec.android.activity.MY_ACTION");
	        intent.putExtra("PARAM", "Not Sensitive Info");
	        startActivityForResult(intent, REQUEST_CODE);
    	} catch (ActivityNotFoundException e) {
        	Toast.makeText(this, "Target activity not found.", Toast.LENGTH_LONG).show();
    	}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // *** POINT 5 *** When receiving a result, handle the data carefully and securely.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
        if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case REQUEST_CODE:
			String result = data.getStringExtra("RESULT");
        	Toast.makeText(this, String.format("Received result: \"%s\"", result), Toast.LENGTH_LONG).show();
			break;
		}
	}
}