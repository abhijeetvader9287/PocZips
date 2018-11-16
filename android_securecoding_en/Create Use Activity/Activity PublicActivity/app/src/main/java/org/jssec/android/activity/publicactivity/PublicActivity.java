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

package org.jssec.android.activity.publicactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PublicActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // *** POINT 2 *** Handle the received intent carefully and securely.
        // Since this is a public activity, it is possible that the sending application may be malware.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
		String param = getIntent().getStringExtra("PARAM");
    	Toast.makeText(this, String.format("Received param: \"%s\"", param), Toast.LENGTH_LONG).show();
	}

	public void onReturnResultClick(View view) {
		
		// *** POINT 3 *** When returning a result, do not include sensitive information.
		// Since this is a public activity, it is possible that the receiving application may be malware.
		// If there is no problem if the data gets received by malware, then it can be returned as a result. 
		Intent intent = new Intent();
		intent.putExtra("RESULT", "Not Sensitive Info");
		setResult(RESULT_OK, intent);
		finish();
	}
}