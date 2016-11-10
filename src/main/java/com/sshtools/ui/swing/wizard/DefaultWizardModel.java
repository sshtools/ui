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
package com.sshtools.ui.swing.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultWizardModel extends AbstractWizardModel {

	private List pages = new ArrayList();
	private int currentPage = -1;

	public void addPage(WizardPage page) {
		pages.add(page);
		firePageAdded(page);
		if (currentPage == -1) {
			currentPage = 0;
			firePageChanged(null, page);
		}
		page.added(this);
	}

	public WizardPage currentPage() {
		return currentPage == -1 ? null : (WizardPage) pages.get(currentPage);
	}

	public List getPages() {
		return pages;
	}

	public void gotoPage(WizardPage page) {
		int idx = pages.indexOf(page);
		if (idx == -1) {
			throw new IllegalArgumentException("No such page in model.");
		}
		if (idx != currentPage) {
			WizardPage oldCurrentPage = currentPage();
			currentPage = idx;
			firePageChanged(oldCurrentPage, page);
		}
	}

	public boolean hasNextPage() {
		return (currentPage + 1) < pages.size();
	}

	public boolean hasPreviousPage() {
		return currentPage > 0;
	}

	public WizardPage nextPage() throws WizardPageValidateException {
		if (!hasNextPage()) {
			throw new IllegalArgumentException("No next page in model.");
		}
		WizardPage oldCurrentPage = currentPage();
		oldCurrentPage.validatePage();
		currentPage++;
		WizardPage newPage = currentPage();
		firePageChanged(newPage, oldCurrentPage);
		return newPage;
	}

	public WizardPage previousPage() {
		if (!hasPreviousPage()) {
			throw new IllegalArgumentException("No previous page in model.");
		}
		WizardPage oldCurrentPage = currentPage();
		currentPage--;
		WizardPage newPage = currentPage();
		firePageChanged(newPage, oldCurrentPage);
		return newPage;
	}

	public WizardPage getPage(String name) {
		for (Iterator i = pages.iterator(); i.hasNext();) {
			WizardPage page = (WizardPage) i.next();
			if (page.getPageName().equals(name)) {
				return page;
			}
		}
		return null;
	}

}
