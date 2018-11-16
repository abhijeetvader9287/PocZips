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

package org.jssec.android.permission.signcheckactivity;

import org.jssec.android.shared.PkgCert;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class SignatureCheckActivity extends Activity {
	// Self signed certificate hash value
	private static String sMyCertHash = null;
	private static String myCertHash(Context context) {
		if (sMyCertHash == null) {
			if (Utils.isDebuggable(context)) {
				// Certificate hash value of "androiddebugkey" of debug.
				sMyCertHash = "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255";
			} else {
				// Certificate hash value of "my company key" of keystore
				sMyCertHash = "D397D343 A5CBC10F 4EDDEB7C A10062DE 5690984F 1FB9E88B D7B3A7C2 42E142CA";
			}
		}
		return sMyCertHash;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// *** POINT 1 *** Verify that an application's certificate belongs to the developer before major processing is started
		if (!PkgCert.test(this, this.getPackageName(), myCertHash(this))) {
			Toast.makeText(this, "Self-sign match  NG", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		Toast.makeText(this, "Self-sign match  OK", Toast.LENGTH_LONG).show();
	}
}