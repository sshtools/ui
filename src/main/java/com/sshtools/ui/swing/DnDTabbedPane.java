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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.EventListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DnDTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int LINEWIDTH = 3;
	private static final String NAME = "test";

	private static Rectangle rBackward = new Rectangle();
	private static Rectangle rForward = new Rectangle();
	private static int rwh = 20;
	private static int buttonsize = 30; // xxx magic number of scroll button
										// size
	private final GhostGlassPane glassPane = new GhostGlassPane();
	private final Rectangle lineRect = new Rectangle();
	private final Color lineColor = null;
	private int dragTabIndex = -1;
	private DragGestureListener dgl;
	private boolean hasGhost = true;
	private boolean outOfWindow = false;
	private DragGestureRecognizer dragSource;
	private DragSourceContext context;

	public DnDTabbedPane(int tabPlacement) {
		super(tabPlacement);
		final DragSourceListener dsl = new DragSourceListener() {

			public void dragEnter(DragSourceDragEvent e) {
				context = e.getDragSourceContext();
				context.setCursor(DragSource.DefaultMoveDrop);
			}

			public void dragExit(DragSourceEvent e) {
				context = e.getDragSourceContext();
				lineRect.setRect(0, 0, 0, 0);
				glassPane.setPoint(new Point(-1000, -1000));
				glassPane.repaint();
			}

			public void dragOver(DragSourceDragEvent e) {
				context = e.getDragSourceContext();
				Point glassPt = e.getLocation();
				SwingUtilities.convertPointFromScreen(glassPt, glassPane);
				int targetIdx = getTargetTabIndex(glassPt);
				// if(getTabAreaBounds().contains(tabPt) && targetIdx>=0 &&
				if (isDroppable(targetIdx)) {
					e.getDragSourceContext().setCursor(
							DragSource.DefaultMoveDrop);
					glassPane.setCursor(DragSource.DefaultMoveDrop);
				} else {
					e.getDragSourceContext().setCursor(
							DragSource.DefaultMoveNoDrop);
					glassPane.setCursor(DragSource.DefaultMoveNoDrop);
				}
			}

			public void dragDropEnd(DragSourceDropEvent e) {
				context = e.getDragSourceContext();
				lineRect.setRect(0, 0, 0, 0);
				glassPane.setVisible(false);
				if (hasGhost()) {
					glassPane.setVisible(false);
					glassPane.setImage(null);
				}
				if (outOfWindow) {
					System.out.println("Out of window");
					fireTabDetached(dragTabIndex, e.getLocation());
				}
				dragTabIndex = -1;
				context = null;
			}

			public void dropActionChanged(DragSourceDragEvent e) {
			}
		};
		final Transferable t = new Transferable() {
			private final DataFlavor FLAVOR = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType, NAME);

			public Object getTransferData(DataFlavor flavor) {
				return flavor.equals(FLAVOR) ? DnDTabbedPane.this
						: getTitleAt(dragTabIndex);
			}

			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { FLAVOR };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return flavor.getHumanPresentableName().equals(NAME);
			}
		};
		dgl = new DragGestureListener() {
			public void dragGestureRecognized(DragGestureEvent ex) {
				if (getTabCount() <= 1)
					return;

				Point tabPt = ex.getDragOrigin();
				Component c = ex.getComponent();

				// If the originating component is not the tabbed pane itself,
				// translate the co-ordinates
				if (!c.equals(DnDTabbedPane.this)) {
					Point tabsLoc = c.getLocation();
					tabsLoc.translate(tabPt.x, tabPt.y);
					tabPt = tabsLoc;
					c = DnDTabbedPane.this;
				}
				dragTabIndex = indexAtLocation(tabPt.x, tabPt.y);
				// "disabled tab problem".
				if (dragTabIndex < 0 || !isEnabledAt(dragTabIndex))
					return;
				initGlassPane(c, tabPt);
				try {
					ex.startDrag(DragSource.DefaultMoveDrop, t, dsl);
				} catch (InvalidDnDOperationException idoe) {
					idoe.printStackTrace();
				}
			}
		};
		new DropTarget(glassPane, DnDConstants.ACTION_COPY_OR_MOVE,
				new CDropTargetListener(), true);
		dragSource = new DragSource().createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_COPY_OR_MOVE, dgl);
	}

	public DnDTabbedPane() {
		this(TOP);
	}

	public void addTabDraggedListener(TabDraggedListener listener) {
		listenerList.add(TabDraggedListener.class, listener);
	}

	public void removeTabDraggedListener(TabDraggedListener listener) {
		listenerList.remove(TabDraggedListener.class, listener);
	}

	protected void fireTabDragging(int oldIndex, int newIndex) {
		EventListener[] l = listenerList.getListeners(TabDraggedListener.class);
		for (int i = l.length - 1; i >= 0; i--) {
			((TabDraggedListener) l[i]).tabbedMoving(oldIndex, newIndex);
		}
	}

	protected void fireTabDragged(int oldIndex, int newIndex) {
		EventListener[] l = listenerList.getListeners(TabDraggedListener.class);
		for (int i = l.length - 1; i >= 0; i--) {
			((TabDraggedListener) l[i]).tabbedMoved(oldIndex, newIndex);
		}
	}

	protected void fireTabDetached(int index, Point point) {
		EventListener[] l = listenerList.getListeners(TabDraggedListener.class);
		for (int i = l.length - 1; i >= 0; i--) {
			((TabDraggedListener) l[i]).tabDetached(index, point);
		}
	}

	private void clickArrowButton(String actionKey) {
		ActionMap map = getActionMap();
		if (map != null) {
			Action action = map.get(actionKey);
			if (action != null && action.isEnabled()) {
				action.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, null, 0, 0));
			}
		}
	}

	private void autoScrollTest(Point glassPt) {
		Rectangle r = getTabAreaBounds();
		if (isVerticalTabs()) {
			rBackward.setBounds(r.x, r.y, rwh, r.height);
			rForward.setBounds(r.x + r.width - rwh - buttonsize, r.y, rwh
					+ buttonsize, r.height);
		} else {
			rBackward.setBounds(r.x, r.y, r.width, rwh);
			rForward.setBounds(r.x, r.y + r.height - rwh - buttonsize, r.width,
					rwh + buttonsize);
		}
		rBackward = SwingUtilities.convertRectangle(getParent(), rBackward,
				glassPane);
		rForward = SwingUtilities.convertRectangle(getParent(), rForward,
				glassPane);
		if (rBackward.contains(glassPt)) {
			// System.out.println(new java.util.Date() + "Backward");
			clickArrowButton("scrollTabsBackwardAction");
		} else if (rForward.contains(glassPt)) {
			// System.out.println(new java.util.Date() + "Forward");
			clickArrowButton("scrollTabsForwardAction");
		}
	}

	public void setPaintGhost(boolean flag) {
		hasGhost = flag;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public boolean hasGhost() {
		return hasGhost;
	}

	private boolean isPaintScrollArea = true;

	public void setPaintScrollArea(boolean flag) {
		isPaintScrollArea = flag;
	}

	public boolean isPaintScrollArea() {
		return isPaintScrollArea;
	}

	public void setTabComponentAt(int idx, Component component) {
		super.setTabComponentAt(idx, component);
		new DragSource().createDefaultDragGestureRecognizer(component,
				DnDConstants.ACTION_COPY_OR_MOVE, dgl);
	}

	private int getTargetTabIndex(Point glassPt) {
		Point tabPt = SwingUtilities.convertPoint(glassPane, glassPt,
				DnDTabbedPane.this);
		boolean isTB = isVerticalTabs();
		for (int i = 0; i < getTabCount(); i++) {
			Rectangle r = getBoundsAt(i);
			if (isTB)
				r.setRect(r.x - r.width / 2, r.y, r.width, r.height);
			else
				r.setRect(r.x, r.y - r.height / 2, r.width, r.height);
			if (r.contains(tabPt))
				return i;
		}
		Rectangle r = getBoundsAt(getTabCount() - 1);
		if (isTB)
			r.setRect(r.x + r.width / 2, r.y, r.width, r.height);
		else
			r.setRect(r.x, r.y + r.height / 2, r.width, r.height);
		return r.contains(tabPt) ? getTabCount() : -1;
	}

	private void convertTab(int prev, int next) {
		fireTabDragging(prev, next);
		if (next < 0 || prev == next) {
			return;
		}

		Component cmp = getComponentAt(prev);
		Component tab = getTabComponentAt(prev);
		String str = getTitleAt(prev);
		Icon icon = getIconAt(prev);
		String tip = getToolTipTextAt(prev);
		boolean flg = isEnabledAt(prev);
		int tgtindex = prev > next ? next : next - 1;

		removeTabAt(prev);
		insertTab(str, icon, cmp, tip, tgtindex);
		setEnabledAt(tgtindex, flg);
		// When you drag'n'drop a disabled tab, it finishes enabled and
		// selected.
		// pointed out by dlorde
		if (flg) {
			setSelectedIndex(tgtindex);
		}

		// I have a component in all tabs (jlabel with an X to close the tab)
		// and when i move a tab the component disappear.
		// pointed out by Daniel Dario Morales Salas
		super.setTabComponentAt(tgtindex, tab);
		fireTabDragged(prev, next);
	}

	private boolean isDroppable(int next) {
		return !((isVerticalTabs() && (next < 0 || dragTabIndex == next || next
				- dragTabIndex == 1)) || (isVerticalTabs() && (next < 0
				|| dragTabIndex == next || next - dragTabIndex == 1)));
	}

	private boolean isVerticalTabs() {
		return getTabPlacement() == JTabbedPane.TOP
				|| getTabPlacement() == JTabbedPane.BOTTOM;
	}

	private void initTargetLeftRightLine(int next) {
		if (next < 0 || dragTabIndex == next || next - dragTabIndex == 1) {
			lineRect.setRect(0, 0, 0, 0);
		} else if (next == 0) {
			Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(0),
					glassPane);
			lineRect.setRect(r.x - LINEWIDTH / 2, r.y, LINEWIDTH, r.height);
		} else {
			Rectangle r = SwingUtilities.convertRectangle(this,
					getBoundsAt(next - 1), glassPane);
			lineRect.setRect(r.x + r.width - LINEWIDTH / 2, r.y, LINEWIDTH,
					r.height);
		}
	}

	private void initTargetTopBottomLine(int next) {
		if (next < 0 || dragTabIndex == next || next - dragTabIndex == 1) {
			lineRect.setRect(0, 0, 0, 0);
		} else if (next == 0) {
			Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(0),
					glassPane);
			lineRect.setRect(r.x, r.y - LINEWIDTH / 2, r.width, LINEWIDTH);
		} else {
			Rectangle r = SwingUtilities.convertRectangle(this,
					getBoundsAt(next - 1), glassPane);
			lineRect.setRect(r.x, r.y + r.height - LINEWIDTH / 2, r.width,
					LINEWIDTH);
		}
	}

	private void initGlassPane(Component c, Point tabPt) {
		getRootPane().setGlassPane(glassPane);
		if (hasGhost()) {
			Rectangle rect = getBoundsAt(dragTabIndex);
			BufferedImage image = new BufferedImage(c.getWidth(),
					c.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			c.paint(g);
			rect.x = rect.x < 0 ? 0 : rect.x;
			rect.y = rect.y < 0 ? 0 : rect.y;
			image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
			glassPane.setImage(image);
		}
		Point glassPt = SwingUtilities.convertPoint(c, tabPt, glassPane);
		glassPane.setPoint(glassPt);
		glassPane.setVisible(true);
	}

	private Rectangle getTabAreaBounds() {
		Rectangle tabbedRect = getBounds();
		// pointed out by daryl. NullPointerException: i.e. addTab("Tab",null)
		// Rectangle compRect = getSelectedComponent().getBounds();
		Component comp = getSelectedComponent();
		int idx = 0;
		while (comp == null && idx < getTabCount())
			comp = getComponentAt(idx++);
		Rectangle compRect = (comp == null) ? new Rectangle() : comp
				.getBounds();
		int tabPlacement = getTabPlacement();
		if (tabPlacement == TOP) {
			tabbedRect.height = tabbedRect.height - compRect.height;
		} else if (tabPlacement == BOTTOM) {
			tabbedRect.y = tabbedRect.y + compRect.y + compRect.height;
			tabbedRect.height = tabbedRect.height - compRect.height;
		} else if (tabPlacement == LEFT) {
			tabbedRect.width = tabbedRect.width - compRect.width;
		} else if (tabPlacement == RIGHT) {
			tabbedRect.x = tabbedRect.x + compRect.x + compRect.width;
			tabbedRect.width = tabbedRect.width - compRect.width;
		}
		tabbedRect.grow(2, 2);
		return tabbedRect;
	}

	class CDropTargetListener implements DropTargetListener {
		public void dragEnter(DropTargetDragEvent e) {
			System.out.println("In  window");
			outOfWindow = false;
			if (isDragAcceptable(e))
				e.acceptDrag(e.getDropAction());
			else
				e.rejectDrag();
		}

		public void dragExit(DropTargetEvent e) {
			outOfWindow = true;
			if (context != null) {
				context.setCursor(DragSource.DefaultMoveDrop);
			}
			System.out.println("Out ofg window");
		}

		public void dropActionChanged(DropTargetDragEvent e) {
		}

		private Point _glassPt = new Point();

		public void dragOver(final DropTargetDragEvent e) {
			Point glassPt = e.getLocation();
			if (getTabPlacement() == JTabbedPane.TOP
					|| getTabPlacement() == JTabbedPane.BOTTOM) {
				initTargetLeftRightLine(getTargetTabIndex(glassPt));
			} else {
				initTargetTopBottomLine(getTargetTabIndex(glassPt));
			}
			if (hasGhost()) {
				glassPane.setPoint(glassPt);
			}
			if (!_glassPt.equals(glassPt))
				glassPane.repaint();
			_glassPt = glassPt;
			autoScrollTest(glassPt);
		}

		public void drop(DropTargetDropEvent e) {
			System.out.println(e.getLocation() + " out of window: "
					+ outOfWindow);
			if (!outOfWindow) {
				if (isDropAcceptable(e)) {
					convertTab(dragTabIndex, getTargetTabIndex(e.getLocation()));
					e.dropComplete(true);
				} else {
					e.dropComplete(false);
				}
			}
			repaint();
		}

		private boolean isDragAcceptable(DropTargetDragEvent e) {
			Transferable t = e.getTransferable();
			if (t == null)
				return false;
			DataFlavor[] f = e.getCurrentDataFlavors();
			if (t.isDataFlavorSupported(f[0]) && dragTabIndex >= 0) {
				return true;
			}
			return false;
		}

		private boolean isDropAcceptable(DropTargetDropEvent e) {
			Transferable t = e.getTransferable();
			if (t == null)
				return false;
			DataFlavor[] f = t.getTransferDataFlavors();
			if (t.isDataFlavorSupported(f[0]) && dragTabIndex >= 0) {
				return true;
			}
			return false;
		}
	}

	class GhostGlassPane extends JPanel {
		private final AlphaComposite composite;
		private Point location = new Point(0, 0);
		private BufferedImage draggingGhost = null;

		public GhostGlassPane() {
			setOpaque(false);
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f);
			// http://bugs.sun.com/view_bug.do?bug_id=6700748
			// setCursor(null);
		}

		public void setImage(BufferedImage draggingGhost) {
			this.draggingGhost = draggingGhost;
		}

		public void setPoint(Point location) {
			this.location = location;
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(composite);
			if (isPaintScrollArea()
					&& getTabLayoutPolicy() == SCROLL_TAB_LAYOUT) {
				g2.setPaint(lineColor == null ? UIManager
						.getColor("ToolBar.dockingForeground") : lineColor);
				g2.fill(rBackward);
				g2.fill(rForward);
			}
			if (draggingGhost != null) {
				double xx = location.getX()
						- (draggingGhost.getWidth(this) / 2d);
				double yy = location.getY()
						- (draggingGhost.getHeight(this) / 2d);
				g2.drawImage(draggingGhost, (int) xx, (int) yy, null);
			}
			if (dragTabIndex >= 0) {
				g2.setPaint(lineColor == null ? UIManager
						.getColor("ToolBar.dockingForeground") : lineColor);
				g2.fill(lineRect);
			}
		}
	}
}