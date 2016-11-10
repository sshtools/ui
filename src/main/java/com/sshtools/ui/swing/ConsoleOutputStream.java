/**
 * SSHTOOLS Limited licenses this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/* HEADER */
package com.sshtools.ui.swing;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 */
public class ConsoleOutputStream extends OutputStream {

	// Private instace variables
	private OutputStream oldSysOut;
	private DebugConsole console;
	private Color color;

	public ConsoleOutputStream(OutputStream oldSysOut, Color color,
			DebugConsole console) {
		this.oldSysOut = oldSysOut;
		this.console = console;
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		console.append(String.valueOf((char) b), color);
		if (oldSysOut != null) {
			oldSysOut.write(b);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte[] buf, int off, int len) throws IOException {
		console.append(new String(buf, off, len), color);
		if (oldSysOut != null) {
			oldSysOut.write(buf, off, len);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() throws IOException {
		super.flush();
		if (oldSysOut != null) {
			oldSysOut.flush();
		}
	}
}