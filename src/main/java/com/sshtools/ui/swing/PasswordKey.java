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
public class PasswordKey implements Comparable<PasswordKey> {
	private String protocol;
	private String scheme;
	private String host;
	private String username;
	private int port;

	public PasswordKey(String protocol, String scheme, String host, String username, int port) {
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

	public int compareTo(PasswordKey o) {
		PasswordKey key = (PasswordKey) o;
		int i = protocol.compareTo(key.getProtocol());
		if (i == 0) {
			i = scheme.compareTo(key.getScheme());
			if (i == 0) {
				i = host.compareTo(key.getHost());
				if (i == 0) {
					i = username.compareTo(key.getUsername());
					if (i == 0) {
						return new Integer(port).compareTo(new Integer(key.getPort()));
					}
				}
			}
		}
		return i;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordKey other = (PasswordKey) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		try {
			buf.append(URLEncoder.encode(protocol == null ? "" : protocol, "UTF-8"));
			buf.append("@");
			buf.append(URLEncoder.encode(scheme == null ? "" : scheme, "UTF-8"));
			buf.append("@");
			buf.append(URLEncoder.encode(host == null ? "" : host, "UTF-8"));
			buf.append("@");
			buf.append(URLEncoder.encode(username == null ? "" : username, "UTF-8"));
			buf.append("@");
			buf.append(port);
		} catch (Exception e) {
		}

		return buf.toString();
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public String getScheme() {
		return scheme;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}
}
