package com.sshtools.ui.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {

	private Color background2;

	public GradientPanel() {
		super();
	}

	public GradientPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public GradientPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public GradientPanel(LayoutManager layout) {
		super(layout);
	}

	public Color getBackground2() {
		return background2;
	}

	public void setBackground2(Color background2) {
		this.background2 = background2;
		repaint();
	}

	protected void paintComponent(Graphics g) {
		if (!isOpaque()) {
			super.paintComponent(g);
			return;
		}
		
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth( );
		int h = getHeight( );
		
		Color color1 = getBackground();
		Color color2 = getBackground2();
		if(color2 == null) {
			color2 = color1.brighter();
		}
		 
		// Paint a gradient from top to bottom
		GradientPaint gp = new GradientPaint(
		    0, 0, color1,
		    0, h, color2 );

		g2d.setPaint( gp );
		g2d.fillRect( 0, 0, w, h );

		setOpaque(false);
		super.paintComponent(g);
		setOpaque(true);
	}
}
