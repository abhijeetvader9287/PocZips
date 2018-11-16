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

package org.jssec.android.privacypolicynocomprehensive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtil {
	public static String getCookie(String _url, String sendCookie, String key)
			throws IOException {
		String cookie = null;
		HttpURLConnection urlConnection = null;

		try {
            // Connect server
			urlConnection = setupRequest(_url, sendCookie);
			urlConnection.connect();

            // Fetch Cookie
			cookie = getResponseCookie(urlConnection, key);
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return cookie;
	}

	public static String getJSON(String _url, String sendCookie, String key)
			throws IOException, JSONException {
		String data = null;
		HttpURLConnection urlConnection = null;

		try {
            // Connect server
            // Add settings for receiving JSON data.
			urlConnection = setupRequest(_url, sendCookie);
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.connect();

            // Get response BODY
			String jsondata = getResponseBody(urlConnection);

            // Parse JOSN data
			JSONObject rootObject = new JSONObject(jsondata);
			data = rootObject.getString(key);
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return data;
	}

	public static String sendJSON(String _url, String sendCookie,
			String jsondata) throws IOException {
		String data = null;
		HttpURLConnection urlConnection = null;

		try {
            // Connect server
            // Add settings for receiving JSON data.
			urlConnection = setupRequest(_url, sendCookie);
			urlConnection
					.setRequestProperty("Content-Type", "application/json");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

            // Set JOSN data to request body
			writeRequestBody(urlConnection, jsondata);

            // Get response BODY
			data = getResponseBody(urlConnection);
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return data;
	}

	private static HttpURLConnection setupRequest(String _url, String sendCookie)
			throws IOException {
		HttpURLConnection urlConnection = null;

		URL url = new URL(_url);
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Cookie", sendCookie);
		urlConnection.setDoInput(true);

		return urlConnection;
	}

	private static String getResponseBody(HttpURLConnection urlConnection)
			throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
            // Get response BODY
			br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line + "\n");
		} finally {
			if (br != null)
				br.close();
		}
		return sb.toString();
	}

	private static String getResponseCookie(HttpURLConnection urlConnection,
			String key) throws IOException {
        //  Get cookie
		List<String> cookieList = urlConnection.getHeaderFields().get(
				"Set-Cookie");
		if (cookieList != null && key != null) {
			for (String tmpCookie : cookieList) {
				if (tmpCookie.startsWith(key)) {
					return tmpCookie.split(";")[0];
				}
			}
		}
		return null;
	}

	private static void writeRequestBody(HttpURLConnection urlConnection,
			String data) throws IOException {
		OutputStream os = null;
		try {
            //  Set a request縲BODY data
			byte[] outputBytes = data.getBytes("UTF-8");
			os = urlConnection.getOutputStream();
			os.write(outputBytes);
			os.flush();
		} finally {
			if (os != null)
				os.close();
		}
	}
}
