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
/*
 */
package com.sshtools.util;

/**
 * @author magicthize
 *
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class StringComparator implements SortComparator {

	private static SortComparator instance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sshtools.util.SortComparator#sortCompare(java.lang.Object,
	 * java.lang.Object)
	 */
	public int sortCompare(Object o1, Object o2) {
		return (o1 == null && o2 != null) ? -1 : (o1 != null && o2 == null ? 1
				: String.valueOf(o1).compareTo(String.valueOf(o2)));
	}

	/**
	 * @return
	 */
	public static SortComparator getDefaultInstance() {
		if (instance == null) {
			instance = new StringComparator();
		}
		return instance;
	}

}
