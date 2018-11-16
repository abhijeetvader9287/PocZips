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

package org.jssec.android.log.outputredirection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import android.app.Application;

public class OutputRedirectApplication extends Application {

	// PrintStream which is not output anywhere
	private final PrintStream emptyStream = new PrintStream(new OutputStream() {
		public void write(int oneByte) throws IOException {
			// do nothing
		}
	});

	@Override
	public void onCreate() {
		// Redirect System.out/err to PrintStream which doesn't output anywhere, when release build.

		// Save original stream of System.out/err
		PrintStream savedOut = System.out;
		PrintStream savedErr = System.err;

		// Once, redirect System.out/err to PrintStream which doesn't output anywhere
		System.setOut(emptyStream);
		System.setErr(emptyStream);

		// Restore the original stream only when debugging. (In release build, the following 1 line is deleted byProGuard.)
		resetStreams(savedOut, savedErr);
	}

	// All of the following methods are deleted byProGuard when release.
	private void resetStreams(PrintStream savedOut, PrintStream savedErr) {
		System.setOut(savedOut);
		System.setErr(savedErr);
	}
}
