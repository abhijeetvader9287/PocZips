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

package org.jssec.android.provider.temporaryuser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TemporaryUserActivity extends Activity {

	// Information of the Content Provider's Activity to request temporary content provider access.
    private static final String TARGET_PACKAGE =  "org.jssec.android.provider.temporaryprovider";
    private static final String TARGET_ACTIVITY = "org.jssec.android.provider.temporaryprovider.TemporaryPassiveGrantActivity";

    // Target Content Provider Information
	private static final String AUTHORITY = "org.jssec.android.provider.temporaryprovider";
	private interface Address {
		public static final String PATH = "addresses";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	}

    private static final int REQUEST_CODE = 1;

	public void onQueryClick(View view) {

    	logLine("[Query]");

        Cursor cursor = null;
    	try {
	    	if (!providerExists(Address.CONTENT_URI)) {
	    		logLine("  Content Provider doesn't exist.");
	    		return;
	    	}

            // *** POINT 9 *** Do not send sensitive information.
            // If no problem when the information is taken by malware, it can be included in the request.
	        cursor = getContentResolver().query(Address.CONTENT_URI, null, null, null, null);

            // *** POINT 10 *** When receiving a result, handle the result data carefully and securely.
	        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
	        if (cursor == null) {
	            logLine("  null cursor");
	        } else {
	            boolean moved = cursor.moveToFirst();
	            while (moved) {
	            	logLine(String.format("  %d, %s", cursor.getInt(0), cursor.getString(1)));
	                moved = cursor.moveToNext();
	            }
	        }
    	} catch (SecurityException ex) {
    		logLine("  Exception:" + ex.getMessage());
    	}
        finally {
            if (cursor != null) cursor.close();
        }
	}

	// In the case that this application requests temporary access to the Content Provider
	// and the Content Provider passively grants temporary access permission to this application.
	public void onGrantRequestClick(View view) {
		Intent intent = new Intent();
		intent.setClassName(TARGET_PACKAGE, TARGET_ACTIVITY);
		try {
		    startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			logLine("Content Provider's Activity not found.");
		}
	}

    private boolean providerExists(Uri uri) {
    	ProviderInfo pi = getPackageManager().resolveContentProvider(uri.getAuthority(), 0);
    	return (pi != null);
    }

	private TextView mLogView;

	// In the case that the Content Provider application grants temporary access
	// to this application actively.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLogView = (TextView)findViewById(R.id.logview);
    }

	private void logLine(String line) {
		mLogView.append(line);
		mLogView.append("\n");
	}
}