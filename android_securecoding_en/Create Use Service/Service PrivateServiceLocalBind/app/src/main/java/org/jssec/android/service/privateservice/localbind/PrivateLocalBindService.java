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
package org.jssec.android.service.privateservice.localbind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class PrivateLocalBindService extends Service
implements IPrivateLocalBindService{   
    /**
     * Class to connect service
     */
    public class LocalBinder extends Binder {
        PrivateLocalBindService getService() {
            return PrivateLocalBindService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // *** POINT 3 *** Handle intent carefully and securely,
    	// even though the intent came from the same application.
        // Omitted, since this is a sample. Please refer to "3.2 Handling Input Data Carefully and Securely."
    	String param = intent.getStringExtra("PARAM");
    	Toast.makeText(this, String.format("Received parameter \"%s\".", param), Toast.LENGTH_LONG).show();
    	
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    // Interface
    @Override
    public String getInfo() {
		// *** POINT 5 *** Sensitive information can be returned since the requesting application is in the same application.    	
        return "Sensitive information (from Service)";
    }
}