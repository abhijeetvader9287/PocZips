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

package org.jssec.android.sqlite.task;

import org.jssec.android.sqlite.CommonData;
import org.jssec.android.sqlite.DataValidator;
import org.jssec.android.sqlite.MainActivity;
import org.jssec.android.sqlite.R;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class DataUpdateTask extends  AsyncTask<String, Void, Void>  {
    private SQLiteDatabase  mSampleDb;
    private MainActivity    mActivity;

    public DataUpdateTask(SQLiteDatabase db, MainActivity activity) {
        mSampleDb = db;
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        String  idno = params[0];
        String  name = params[1];
        String  info = params[2];

        //*** POINT 3 *** Validate the input value according the application requirements.
        if (!DataValidator.validateData(idno, name, info)) {
        	return null;
        }

        String whereArgs[] = {idno};
        ContentValues updateValues = new ContentValues();
        updateValues.put("idno", idno);
        updateValues.put("name", name);
        updateValues.put("info", info);

        try {
            //*** POINT 2 *** Use place holder.
            mSampleDb. update (CommonData.TABLE_NAME, updateValues, "idno = ?", whereArgs);
        } catch (SQLException e) {
            Log.e(DataUpdateTask.class.toString(), mActivity.getString(R.string.UPDATING_ERROR_MESSAGE));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        //Display database data
        mActivity.showDbData();
    }

}
