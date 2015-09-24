/* HEADER */
package com.sshtools.ui.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Panel;

public class BorderPanel extends Panel {
    Insets insets;
    Color borderColor;

    public BorderPanel() {
        super();
        init();
    }

    public BorderPanel(LayoutManager layout) {
        super(layout);
        init();
    }

    void init() {
        insets = new Insets(1, 1, 1, 1);
    }

    public void setBorderColor(Color c) {
        borderColor = c;
        repaint();
    }

    public void paint(Graphics g) {
        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
        super.paint(g);
    }

    public Insets getInsets() {
        return borderColor == null ? super.getInsets() : insets;
    }
}