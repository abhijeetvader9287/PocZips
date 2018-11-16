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

package org.jssec.android.broadcast.publicreceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class DynamicReceiverService extends Service {

	private static final String MY_BROADCAST_PUBLIC =
		"org.jssec.android.broadcast.MY_BROADCAST_PUBLIC";
	
	private PublicReceiver mReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		// Register Public Dynamic Broadcast Receiver.
		mReceiver = new PublicReceiver();
		mReceiver.isDynamic = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(MY_BROADCAST_PUBLIC);
		filter.setPriority(1);	// Prioritize Dynamic Broadcast Receiver, rather than Static Broadcast Receiver.
		registerReceiver(mReceiver, filter);
		Toast.makeText(this,
				"Registered Dynamic Broadcast Receiver.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Unregister Public Dynamic Broadcast Receiver.
		unregisterReceiver(mReceiver);
		mReceiver = null;
		Toast.makeText(this,
				"Unregistered Dynamic Broadcast Receiver.",
				Toast.LENGTH_SHORT).show();
	}
}
