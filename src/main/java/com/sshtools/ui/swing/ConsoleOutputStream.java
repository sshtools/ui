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