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
package com.sshtools.ui.swing;

import java.net.URLEncoder;

/**
 * Represents a unique record stored in the password manager. The password keys
 * uniqueness is derived from the protocol, scheme and host parts of password
 * request. The user may choose to store the password against this key.
 */
public class PasswordKey implements Comparable {
	private String protocol;
	private String scheme;
	private String host;
	private String username;
	private int port;

	/**
	 * Creates a new PasswordKey object.
	 * 
	 * @param protocol
	 *            protocol
	 * @param scheme
	 *            scheme
	 * @param host
	 *            hosts
	 * @param username
	 *            username if applicable (null if not)
	 * @param port
	 *            port if application (0 if not)
	 */
	public PasswordKey(String protocol, String scheme, String host,
			String username, int port) {
		if (protocol == null)
			throw new IllegalArgumentException("Protocol may not be null");
		if (scheme == null)
			throw new IllegalArgumentException("Scheme may not be null");
		if (host == null)
			throw new IllegalArgumentException("Host may not be null");
		this.protocol = protocol;
		this.scheme = scheme;
		this.host = host;
		this.username = username;
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		PasswordKey key = (PasswordKey) o;
		int i = protocol.compareTo(key.getProtocol());
		if (i == 0) {
			i = scheme.compareTo(key.getScheme());
			if (i == 0) {
				i = host.compareTo(key.getHost());
				if (i == 0) {
					i = username.compareTo(key.getUsername());
					if (i == 0) {
						return new Integer(port).compareTo(new Integer(key
								.getPort()));
					}
				}
			}
		}
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return compareTo(o) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();

		try {
			buf.append(URLEncoder.encode(protocol == null ? "" : protocol,
					"UTF-8"));
			buf.append("@");
			buf.append(URLEncoder.encode(scheme == null ? "" : scheme, "UTF-8"));
			buf.append("@");
			buf.append(URLEncoder.encode(host == null ? "" : host, "UTF-8"));
			buf.append("@");
			buf.append(URLEncoder.encode(username == null ? "" : username,
					"UTF-8"));
			buf.append("@");
			buf.append(port);
		} catch (Exception e) {
		}

		return buf.toString();
	}

	/**
	 * Get the protocol part of the key
	 * 
	 * @return protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Get the host part of the key
	 * 
	 * @return host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Get the scheme part of the key
	 * 
	 * @return scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * Get the port part of the key
	 * 
	 * @return port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get the username part of the key if applicable (null if not)
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
}
