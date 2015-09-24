/*
 * Gruntspud
 * 
 * Copyright (C) 2002 Brett Smith.
 * 
 * Written by: Brett Smith <t_magicthize@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.sshtools.ui.swing;

/**
 * Exception used by the authenticator and the password manager
 */
public class PasswordException extends Exception {

	/**
	 * Construct a new exception
	 * 
	 * @param message message
	 */
	public PasswordException(String message) {
		super(message);
	}

	/**
	 * Construct a new exception
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public PasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construct a new exception
	 * 
	 * @param cause cause
	 */
	public PasswordException(Throwable cause) {
		super(cause);
	}
}