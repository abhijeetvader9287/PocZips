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

package org.jssec.android.activity.partneractivity;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PartnerActivity extends Activity {
	
	// *** POINT 4 *** Verify the requesting application's certificate through a predefined whitelist.
	private static PkgCertWhitelists sWhitelists = null;
	private static void buildWhitelists(Context context) {
		boolean isdebug = Utils.isDebuggable(context);
		sWhitelists = new PkgCertWhitelists();
		
		// Register certificate hash value of partner application org.jssec.android.activity.partneruser.
		sWhitelists.add("org.jssec.android.activity.partneruser", isdebug ?
				// Certificate hash value of "androiddebugkey" in the debug.keystore.
    			"0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
				// Certificate hash value of "partner key" in the keystore.
    			"1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");
		
		// Register the other partner applications in the same way.
	}
	private static boolean checkPartner(Context context, String pkgname) {
		if (sWhitelists == null) buildWhitelists(context);
		return sWhitelists.test(context, pkgname);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        	
    	// *** POINT 4 *** Verify the requesting application's certificate through a predefined whitelist.
        if (!checkPartner(this, getCallingActivity().getPackageName())) {
        	Toast.makeText(this,
        			"Requesting application is not a partner application.",
        			Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }
        
        // *** POINT 5 *** Handle the received intent carefully and securely, even though the intent was sent from a partner application.
        // Omitted, since this is a sample. Refer to "3.2 Handling Input Data Carefully and Securely."
        Toast.makeText(this, "Accessed by Partner App", Toast.LENGTH_LONG).show();
    }
    
	public void onReturnResultClick(View view) {

		// *** POINT 6 *** Only return Information that is granted to be disclosed to a partner application.
		Intent intent = new Intent();
		intent.putExtra("RESULT", "Information for partner applications");
		setResult(RESULT_OK, intent);
		finish();
	}
}