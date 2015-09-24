package com.sshtools.ui.swing;

import java.awt.Point;
import java.util.EventListener;

public interface TabDraggedListener extends EventListener {
	void tabbedMoving(int oldIndex, int newIndex);
	void tabbedMoved(int oldIndex, int newIndex);
	void tabDetached(int index, Point point);
}