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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

public class SortableTableHeader extends JTableHeader {
	private int index;
	private int direction;
	private ChangeEvent changeEvent;
	private SortCriteria criteria;

	public SortableTableHeader(final JTable table, SortCriteria criteria) {
		super(table.getColumnModel());
		this.criteria = criteria;
		setDefaultRenderer(new SortableHeaderRenderer(table.getColumnModel(),
				true, criteria));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				int sel = columnAtPoint(evt.getPoint());
				direction = SortCriteria.NO_SORT;

				for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
					if (i == sel) {
						direction = ((SortableHeaderRenderer) getDefaultRenderer())
								.nextSort(i);
					} else {
						((SortableHeaderRenderer) getDefaultRenderer())
								.setSort(i, SortCriteria.NO_SORT);
					}
				}

				index = table.convertColumnIndexToModel(sel);
				if (SortableTableHeader.this.criteria != null) {
					SortableTableHeader.this.criteria.setSortType(index);
					SortableTableHeader.this.criteria
							.setSortDirection(direction);
				}
				fireStateChanged();
			}
		});
		setTable(table);
	}

	/**
	 * @param listing
	 */
	public SortableTableHeader(JTable table) {
		this(table, null);
	}

	public void setCriteria(SortCriteria criteria) {
		this.criteria = criteria;
		((SortableHeaderRenderer) getDefaultRenderer()).setCriteria(criteria);
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);

				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	public int getindex() {
		return index;
	}

	public int getdirection() {
		return direction;
	}
}
