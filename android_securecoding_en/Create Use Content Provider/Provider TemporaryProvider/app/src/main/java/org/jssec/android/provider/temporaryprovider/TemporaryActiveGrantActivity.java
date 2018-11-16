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

package org.jssec.android.provider.temporaryprovider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TemporaryActiveGrantActivity extends Activity {

    // User Activity Information
    private static final String TARGET_PACKAGE =  "org.jssec.android.provider.temporaryuser";
    private static final String TARGET_ACTIVITY = "org.jssec.android.provider.temporaryuser.TemporaryUserActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_grant);
	}

	// In the case that Content Provider application grants access permission to other application actively.
	public void onSendClick(View view) {
		try {
			Intent intent = new Intent();

			// *** POINT 5 *** Specify URI for the intent to grant temporary access.
			intent.setData(TemporaryProvider.Address.CONTENT_URI);

			// *** POINT 6 *** Specify access rights for the intent to grant temporary access.
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			// *** POINT 7 *** Send the explicit intent to an application to grant temporary access.
			intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
			startActivity(intent);

		} catch (ActivityNotFoundException e) {
        	Toast.makeText(this, "User Activity not found.", Toast.LENGTH_LONG).show();
		}
	}
}
