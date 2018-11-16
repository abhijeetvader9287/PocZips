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

package org.jssec.android.log.proguard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ProGuardActivity extends Activity {

	final static String LOG_TAG = "ProGuardActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proguard);

		// *** POINT 1 *** Sensitive information must not be output by Log.e()/w()/i(), System.out/err.
		Log.e(LOG_TAG, "Not sensitive information (ERROR)");
		Log.w(LOG_TAG, "Not sensitive information (WARN)");
		Log.i(LOG_TAG, "Not sensitive information (INFO)");

		// *** POINT 2 *** Sensitive information should be output by Log.d()/v() in case of need.
		// *** POINT 3 *** The return value of Log.d()/v()should not be used (with the purpose of substitution or comparison).
		Log.d(LOG_TAG, "sensitive information (DEBUG)");
		Log.v(LOG_TAG, "sensitive information (VERBOSE)");
	}
}
