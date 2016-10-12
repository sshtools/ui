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

import java.util.StringTokenizer;

/**
 *
 */
public class SortCriteria {
	public final static int NO_SORT = 0;
	public final static int SORT_ASCENDING = 1;
	public final static int SORT_DESCENDING = 2;
	private int sortType;
	private int sortDirection;
	private boolean foldersFirst, caseSensitive;

	public SortCriteria() {
		this(0, SORT_ASCENDING, true, false);
	}

	public SortCriteria(int sortType, int sortDirection, boolean foldersFirst,
			boolean caseSensitive) {
		setSortType(sortType);
		setSortDirection(sortDirection);
		setFoldersFirst(foldersFirst);
		setCaseSensitive(caseSensitive);
	}

	public SortCriteria(String string) {
		this(0, 1, true, false);
		try {
			StringTokenizer t = new StringTokenizer(string, ",");
			sortType = Integer.parseInt(t.nextToken());
			sortDirection = Integer.parseInt(t.nextToken());
			foldersFirst = t.nextToken().equals("true");
			caseSensitive = t.nextToken().equals("true");
		} catch (Throwable t) {

		}
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public int getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	public void setFoldersFirst(boolean foldersFirst) {
		this.foldersFirst = foldersFirst;
	}

	public boolean isFoldersFirst() {
		return foldersFirst;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public String toString() {
		return sortType + "," + sortDirection + "," + foldersFirst + ","
				+ caseSensitive;
	}
}
