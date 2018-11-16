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

package org.jssec.android.signsymmetricpresharedkey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class HmacPreSharedKey {

	// *** POINT 1 *** Explicitly specify the encryption mode and the padding.
	// *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
	// Parameters passed to the getInstance method of the Mac class: Authentication mode
	private static final String TRANSFORMATION = "HmacSHA256";

	// Encryption algorithm
	private static final String KEY_ALGORITHM = "HmacSHA256";

	// *** POINT 3 *** Use a key of length sufficient to guarantee the MAC strength.
	// Check the length of the key
	private static final int MIN_KEY_LENGTH_BYTES = 16;

	HmacPreSharedKey() {
	}

	public final byte[] sign(final byte[] plain, final byte[] keyData) {
		return calculate(plain, keyData);
	}

	public final byte[] calculate(final byte[] plain, final byte[] keyData) {
		byte[] hmac = null;

		try {
			// *** POINT 1 *** Explicitly specify the encryption mode and the padding.
			// *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
			Mac mac = Mac.getInstance(TRANSFORMATION);

			SecretKey secretKey = generateKey(keyData);
			if (secretKey != null) {
				mac.init(secretKey);

				hmac = mac.doFinal(plain);
			}
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		} finally {
		}

		return hmac;
	}

	public final boolean verify(final byte[] hmac, final byte[] plain, final byte[] keyData) {
		byte[] hmacForPlain = calculate(plain, keyData);

		if (hmacForPlain != null && Arrays.equals(hmac, hmacForPlain)) {
			return true;
		}

		return false;
	}

	private static final SecretKey generateKey(final byte[] keyData) {
		SecretKey secretKey = null;

		try {
			// *** POINT 3 *** Use a key of length sufficient to guarantee the MAC strength.
			if (keyData.length >= MIN_KEY_LENGTH_BYTES) {
				// *** POINT 2 *** Use strong encryption methods (specifically, technologies that meet the relevant criteria), including algorithms, block cipher modes, and padding modes.
				secretKey = new SecretKeySpec(keyData, KEY_ALGORITHM);
			}
		} catch (IllegalArgumentException e) {
		} finally {
		}

		return secretKey;
	}
}
