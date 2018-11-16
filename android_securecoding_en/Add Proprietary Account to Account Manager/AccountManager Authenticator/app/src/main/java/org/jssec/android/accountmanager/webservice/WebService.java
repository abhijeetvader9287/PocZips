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

package org.jssec.android.accountmanager.webservice;

public class WebService {

	/**
	 * Suppose to access to account managemnet function of online service.
	 * 
	 * @param username Account name character string
	 * @param password password character string
	 * @return Return authentication token
	 */
	public String login(String username, String password) {
		// *** POINT 7 *** HTTPS should be used for communication between an authenticator and the online services.
		// Actually, communication process with servers is implemented here, but Omit here, since this is a sample.
		return getAuthToken(username, password);
	}

	private String getAuthToken(String username, String password) {
		// In fact, get the value which uniqueness and impossibility of speculation are guaranteed by the server,
		// but the fixed value is returned without communication here, since this is sample.
		return "c2f981bda5f34f90c0419e171f60f45c";
	}
}