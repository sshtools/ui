package com.sshtools.ui.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ProgressBar extends Canvas {

    private Color borderColor;
    public ProgressBar(int min, int max, int val) {
        this.min = min;
        this.max = max;
        this.val = val;
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 20);
    }

    public Dimension getMinimumSize() {
        return new Dimension(super.getMinimumSize().width, 20);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        if(val > max) {
            val = max;
        }
        repaint();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        repaint();
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
        repaint();
    }

    public Color getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(Color progressColor) {
        this.progressColor = progressColor;
        repaint();
    }

    public String getProgressString() {
        return progressString;
    }

    public void setProgressString(String progressString) {
        this.progressString = progressString;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle bounds = getBounds();
        g.setColor(borderColor == null ? getForeground() : borderColor);
        g.drawRect(0, 0, bounds.width - 1, bounds.height - 1);
        g.setColor(progressColor != null ? progressColor : Color.blue);
        float scale = val / ((float) max - (float) min);
        g.fillRect(1, 1, (int) ((bounds.width - 2) * scale), bounds.height - 2);
        String s = progressString != null ? progressString : (int) (scale * 100F) + "%"; //$NON-NLS-1$
        FontMetrics fm = getFontMetrics(getFont());
        int swidth = fm.stringWidth(s);
        g.setColor(getForeground());
        g.setXORMode(Color.white);
        g.drawString(s, (bounds.width - swidth) / 2, (bounds.height / 2) + (fm.getAscent() / 2) - 1);
    }

    private int min;
    private int max;
    private int val;
    private Color progressColor;
    private String progressString;
}