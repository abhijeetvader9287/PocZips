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

package org.jssec.android.shared;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class PkgCertWhitelists {
	private Map<String, String> mWhitelists = new HashMap<String, String>();
	
	public boolean add(String pkgname, String sha256) {
		if (pkgname == null) return false;
		if (sha256 == null) return false;
		
		sha256 = sha256.replaceAll(" ", "");
		if (sha256.length() != 64) return false;	// SHA-256 -> 32 bytes -> 64 chars
		sha256 = sha256.toUpperCase();
		if (sha256.replaceAll("[0-9A-F]+", "").length() != 0) return false;	// found non hex char
		
		mWhitelists.put(pkgname, sha256);
		return true;
	}
	
	public boolean test(Context ctx, String pkgname) {
		// Get the correct hash value which corresponds to pkgname.
		String correctHash = mWhitelists.get(pkgname);
		
		// Compare the actual hash value of pkgname with the correct hash value.
		return PkgCert.test(ctx, pkgname, correctHash);
	}
}
