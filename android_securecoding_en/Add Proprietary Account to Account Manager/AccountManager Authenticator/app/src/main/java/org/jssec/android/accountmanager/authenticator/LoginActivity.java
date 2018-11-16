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

package org.jssec.android.accountmanager.authenticator;

import org.jssec.android.accountmanager.webservice.WebService;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class LoginActivity extends AccountAuthenticatorActivity {
	private static final String TAG = AccountAuthenticatorActivity.class.getSimpleName();
	private String mReAuthName = null;
	private EditText mNameEdit = null;
	private EditText mPassEdit = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		// Display alert icon
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.login_activity);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);

		// Find a widget in advance
		mNameEdit = (EditText) findViewById(R.id.username_edit);
		mPassEdit = (EditText) findViewById(R.id.password_edit);
		
		// *** POINT 3 *** The login screen activity must be made as a public activity, and suppose the attack access from other application.
		// Regarding external input, only RE_AUTH_NAME which is String type of Intent#extras, are handled.
		// This external input String is passed toextEdit#setText(), WebService#login(),new Account(),
		// as a parameter,it's verified that there's no problem if any character string is passed.
		mReAuthName = getIntent().getStringExtra(JssecAuthenticator.RE_AUTH_NAME);
		if (mReAuthName != null) {
			// Since LoginActivity is called with the specified user name, user name should not be editable.
			mNameEdit.setText(mReAuthName);
			mNameEdit.setInputType(InputType.TYPE_NULL);
			mNameEdit.setFocusable(false);
			mNameEdit.setEnabled(false);
		}
	}

	// It's executed when login button is pressed.
	public void handleLogin(View view) {
		String name = mNameEdit.getText().toString();
		String pass = mPassEdit.getText().toString();

		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
			// Process when the inputed value is incorrect
			setResult(RESULT_CANCELED);
			finish();
		}

		// Login to online service based on the inpputted account information.
		WebService web = new WebService();
		String authToken = web.login(name, pass);
		if (TextUtils.isEmpty(authToken)) {
			// Process when authentication failed
			setResult(RESULT_CANCELED);
			finish();
		}
		
		// Process when login was successful, is as per below.

		// *** POINT 5 *** Sensitive information (like account information or authentication token) must not be output to the log.
		Log.i(TAG, "WebService login succeeded");


		if (mReAuthName == null) {
			// Register accounts which logged in successfully, to aAccountManager
			// *** POINT 6 *** Password should not be saved in Account Manager.
			AccountManager am = AccountManager.get(this);
			Account account = new Account(name, JssecAuthenticator.JSSEC_ACCOUNT_TYPE);
			am.addAccountExplicitly(account, null, null);
			am.setAuthToken(account, JssecAuthenticator.JSSEC_AUTHTOKEN_TYPE, authToken);
			Intent intent = new Intent();
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, name);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
					JssecAuthenticator.JSSEC_ACCOUNT_TYPE);
			setAccountAuthenticatorResult(intent.getExtras());
			setResult(RESULT_OK, intent);
		} else {
			// Return authentication token
			Bundle bundle = new Bundle();
			bundle.putString(AccountManager.KEY_ACCOUNT_NAME, name);
			bundle.putString(AccountManager.KEY_ACCOUNT_TYPE,
					JssecAuthenticator.JSSEC_ACCOUNT_TYPE);
			bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			setAccountAuthenticatorResult(bundle);
			setResult(RESULT_OK);
		}
		finish();
	}
}