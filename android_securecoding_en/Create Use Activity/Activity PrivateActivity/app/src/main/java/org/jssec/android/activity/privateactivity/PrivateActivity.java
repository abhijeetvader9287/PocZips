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

package org.jssec.android.activity.privateactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrivateActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_activity);

        // *** POINT 4 *** Handle the received Intent carefully and securely, even though the Intent was sent from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
		String param = getIntent().getStringExtra("PARAM");
    	Toast.makeText(this, String.format("Received param: \"%s\"", param), Toast.LENGTH_LONG).show();
	}

	public void onReturnResultClick(View view) {
		
		// *** POINT 5 *** Sensitive information can be sent since it is sending and receiving all within the same application.
		Intent intent = new Intent();
		intent.putExtra("RESULT", "Sensitive Info");
		setResult(RESULT_OK, intent);
		finish();
	}
}