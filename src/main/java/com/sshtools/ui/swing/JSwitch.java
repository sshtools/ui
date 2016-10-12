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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class JSwitch extends AbstractButton {

	private Color shadow1 = UIManager.getColor("controlHighlight");
	private Color shadow2 = UIManager.getColor("control");
	private Color colorBright = UIManager.getColor("Button.light");
	private Color red = UIManager.getColor("controlShadow");
	private Color redf = UIManager.getColor("Button.foreground");
	private Color trackBackground = UIManager.getColor("textHighlight");
	private Color trackBackgroundText = UIManager.getColor("textHighlightText");
	private Border buttonBorder = UIManager.getBorder("Button.border");
	private Border trackBorder = UIManager.getBorder("Button.border");

	private Font font = FontUtil.getUIManagerButtonFontOrDefault("Button.font");
	private int gap = 5;
	private int globalWitdh = 0;
	private Dimension thumbBounds;
	private Rectangle2D bounds;
	private int max;
	private String trueLabel = "On";
	private String falseLabel = "Off";
	private Insets borderInsets;

	public JSwitch() {
		setBackground(UIManager.getColor("Panel.background"));
		setModel(new DefaultButtonModel());
		setSelected(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (new Rectangle(getPreferredSize()).contains(e.getPoint())) {
					setSelected(!isSelected());
				}
			}
		});
		recalcBounds();
	}

	public JSwitch(String trueLabel, String falseLabel) {
		this();
		setTrueLabel(trueLabel);
		setFalseLabel(trueLabel);
	}

	public String getTrueLabel() {
		return trueLabel;
	}

	public void setTrueLabel(String trueLabel) {
		this.trueLabel = trueLabel;
		recalcBounds();
	}

	public String getFalseLabel() {
		return falseLabel;
	}

	public void setFalseLabel(String falseLabel) {
		this.falseLabel = falseLabel;
		recalcBounds();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(globalWitdh, thumbBounds.height);
	}

	@Override
	public int getHeight() {
		return getPreferredSize().height;
	}

	@Override
	public int getWidth() {
		return getPreferredSize().width;
	}

	@Override
	public Font getFont() {
		return font;
	}
	

	@Override
	protected void paintComponent(Graphics g) {
//		private Color trackBackground = UIManager.getColor("textHighlight");
//		private Color trackBackgroundText = UIManager.getColor("textHighlightText");
//		private Border buttonBorder = UIManager.getBorder("Button.border");
//		private Border trackBorder = UIManager.getBorder("Button.border");

		g.setColor(Color.green);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		// Paint the track
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(trackBackground);
		g2.fillRect(0, 3, getWidth(), getHeight() - 6);
		trackBorder.paintBorder(this, g2, 0, 3, getWidth(), getHeight() - 6);
		
		// Paint the thumb
		int buttonX = 0;
		int y = borderInsets.top * - 1;
		int h = thumbBounds.height + borderInsets.top + borderInsets.bottom;
		int w = thumbBounds.width;
//		g2.setPaint(new GradientPaint(buttonX, (int) (y - 0.1 * h), shadow2, buttonX,
//				(int) (y + 1.2 * h), shadow1));
		g2.setColor(Color.red);
		g2.fillRect(buttonX, y, w, h);
//		g2.setPaint(new GradientPaint(buttonX, (int) (y + .65 * h), shadow1, buttonX,
//				(int) (y + 1.3 * h), shadow2));
//		g2.fillRect(buttonX, (int) (y + .65 * h), w, (int) (h - .65 * h));

		if (w > 14) {
			int size = 10;
			g2.setColor(colorBright);
			g2.fillRect(buttonX + w / 2 - size / 2, y + h / 2 - size / 2, size, size);
			g2.setColor(colorBright.darker());
			g2.fillRect(buttonX + w / 2 - 4, h / 2 - 4, 2, 2);
			g2.fillRect(buttonX + w / 2 - 1, h / 2 - 4, 2, 2);
			g2.fillRect(buttonX + w / 2 + 2, h / 2 - 4, 2, 2);
			g2.setColor(colorBright.darker().darker());
			g2.fillRect(buttonX + w / 2 - 4, h / 2 - 2, 2, 6);
			g2.fillRect(buttonX + w / 2 - 1, h / 2 - 2, 2, 6);
			g2.fillRect(buttonX + w / 2 + 2, h / 2 - 2, 2, 6);
			g2.setColor(colorBright.darker());
			g2.fillRect(buttonX + w / 2 - 4, h / 2 + 2, 2, 2);
			g2.fillRect(buttonX + w / 2 - 1, h / 2 + 2, 2, 2);
			g2.fillRect(buttonX + w / 2 + 2, h / 2 + 2, 2, 2);
		}
		buttonBorder.paintBorder(this, g2, buttonX, 0, thumbBounds.width, thumbBounds.height);
		
		
	}

	protected void XXXpaintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight() - 4);
		Graphics2D g2 = (Graphics2D) g;

		// g2.setColor(black);
		// g2.drawRoundRect(1, 1, getWidth() - 2 - 1, getHeight() - 2 - 1, 2,
		// 2);
		// g2.setColor(white);
		// g2.drawRoundRect(1 + 1, 1 + 1, getWidth() - 2 - 3, getHeight() - 2 -
		// 3,
		// 2, 2);

		trackBorder.paintBorder(this, g2, 0, 2, getWidth(), getHeight() - 4);

		int buttonX = 0;
		int textX = 0;
		if (isSelected()) {
			textX = thumbBounds.width;
		} else {
			buttonX = thumbBounds.width;
		}
		int y = 0;
		int w = thumbBounds.width;
		int h = thumbBounds.height;

		g2.setPaint(new GradientPaint(buttonX, (int) (y - 0.1 * h), shadow2, buttonX,
				(int) (y + 1.2 * h), shadow1));
		g2.fillRect(buttonX, y, w, h);
		g2.setPaint(new GradientPaint(buttonX, (int) (y + .65 * h), shadow1, buttonX,
				(int) (y + 1.3 * h), shadow2));
		g2.fillRect(buttonX, (int) (y + .65 * h), w, (int) (h - .65 * h));

		if (w > 14) {
			int size = 10;
			g2.setColor(colorBright);
			g2.fillRect(buttonX + w / 2 - size / 2, y + h / 2 - size / 2, size, size);
			g2.setColor(colorBright.darker());
			g2.fillRect(buttonX + w / 2 - 4, h / 2 - 4, 2, 2);
			g2.fillRect(buttonX + w / 2 - 1, h / 2 - 4, 2, 2);
			g2.fillRect(buttonX + w / 2 + 2, h / 2 - 4, 2, 2);
			g2.setColor(colorBright.darker().darker());
			g2.fillRect(buttonX + w / 2 - 4, h / 2 - 2, 2, 6);
			g2.fillRect(buttonX + w / 2 - 1, h / 2 - 2, 2, 6);
			g2.fillRect(buttonX + w / 2 + 2, h / 2 - 2, 2, 6);
			g2.setColor(colorBright.darker());
			g2.fillRect(buttonX + w / 2 - 4, h / 2 + 2, 2, 2);
			g2.fillRect(buttonX + w / 2 - 1, h / 2 + 2, 2, 2);
			g2.fillRect(buttonX + w / 2 + 2, h / 2 + 2, 2, 2);
		}

		buttonBorder.paintBorder(this, g2, buttonX, y, w, h);
		// g2.setColor(black);
		// g2.drawRoundRect(x, y, w - 1, h - 1, 2, 2);
		// g2.setColor(white);
		// g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, 2, 2);

		g2.setColor(getForeground());
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(getFont());
		g2.drawString(getText(), textX + gap, y + h / 2 + h / 4);
	}
	
	private void recalcBounds() {
		FontMetrics fontMetrics = getFontMetrics(getFont());
		double trueLenth = fontMetrics
				.getStringBounds(trueLabel, getGraphics()).getWidth();
		double falseLenght = fontMetrics.getStringBounds(falseLabel,
				getGraphics()).getWidth();
		max = (int) Math.max(trueLenth, falseLenght);
		gap = Math.max(5, 5 + (int) Math.abs(trueLenth - falseLenght));
		thumbBounds = new Dimension(max + gap * 2, (int)((float)fontMetrics.getHeight() * 1.5));
		globalWitdh = max + thumbBounds.width + gap * 2;
		borderInsets = buttonBorder.getBorderInsets(this);
	}
}