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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * VWrappingLabel is based on Symantec's class WrappingLabel; however, this
 * class can format the text vertically, too. It also wraps text at newlines
 * embedded in the label's text.
 * 
 * @see symantec.awt.WrappingLabel
 * @author Paul F. Williams (mailto:paul@criterioninc.com) Criterion, Inc.
 *         (http://www.criterioninc.com)
 * @author Kyle Morris (mailto:morriskg@nexusfusion.com)
 * 
 */
@SuppressWarnings("serial")
public class WrappingLabel extends JComponent {
	// --------------------------------------------------
	// constants
	// --------------------------------------------------
	// --------------------------------------------------
	// class variables
	// --------------------------------------------------
	// --------------------------------------------------
	// member variables
	// --------------------------------------------------
	protected String text;
	protected float m_nHAlign;
	protected float m_nVAlign;
	protected int baseline;
	protected FontMetrics fm;
	// --------------------------------------------------
	// constructors
	// --------------------------------------------------

	public WrappingLabel() {
		this("");
	}

	public WrappingLabel(String s) {
		this(s, Canvas.LEFT_ALIGNMENT, Canvas.CENTER_ALIGNMENT);
	}

	public WrappingLabel(String s, float nHorizontal, float nVertical) {
		setText(s);
		setHAlignStyle(nHorizontal);
		setVAlignStyle(nVertical);
		setFont(UIManager.getFont("Label.font"));
		setLayout(new WrappingLabelLayout());
	}
	// --------------------------------------------------
	// accessor members
	// --------------------------------------------------

	public float getHAlignStyle() {
		return m_nHAlign;
	}

	public float getVAlignStyle() {
		return m_nVAlign;
	}

	public String getText() {
		return text;
	}

	public void setHAlignStyle(float a) {
		m_nHAlign = a;
		invalidate();
	}

	public void setVAlignStyle(float a) {
		m_nVAlign = a;
		invalidate();
	}

	public void setText(String s) {
		text = s;
		repaint();
	}
	// --------------------------------------------------
	// member methods
	// --------------------------------------------------

	public String paramString() {
		return "";
	}

	public void paintComponent(Graphics g) {
		if (text != null) {
			Dimension d;
			int currentY = 0;
			Vector<String> lines;
			// Set up some class variables
			fm = getFontMetrics(getFont());
			baseline = fm.getMaxAscent();
			// Get the maximum height and width of the current control
			d = getSize();
			lines = breakIntoLines(text, d.width, fm);
			// if (m_nVAlign == V_ALIGN_CENTER)
			if (m_nVAlign == Canvas.CENTER_ALIGNMENT) {
				int center = (d.height / 2);
				currentY = center - (int) ((float) (lines.size() / 2.0) * (float) fm.getHeight());
			}
			// else if (m_nVAlign == V_ALIGN_BOTTOM)
			else if (m_nVAlign == Canvas.BOTTOM_ALIGNMENT) {
				currentY = d.height - (lines.size() * fm.getHeight());
			}
			// now we have broken into substrings, print them
			for (String line : lines) {
				drawAlignedString(g, line, 0, currentY, d.width);
				currentY += fm.getHeight();
			}
			// We're done with the font metrics...
			fm = null;
		}
	}

	protected Vector<String> breakIntoLines(String s, int width, FontMetrics fm) {
		String text = s;
		int fromIndex = 0;
		int pos = 0;
		int bestpos;
		String largestString;
		Vector<String> lines = new Vector<>();
		// while we haven't run past the end of the string...
		while (fromIndex != -1) {
			// Automatically skip any spaces at the beginning of the line
			while (fromIndex < text.length() && text.charAt(fromIndex) == ' ') {
				++fromIndex;
				// If we hit the end of line
				// while skipping spaces, we're done.
				if (fromIndex >= text.length())
					break;
			}
			// fromIndex represents the beginning of the line
			pos = fromIndex;
			bestpos = -1;
			largestString = null;
			while (pos >= fromIndex) {
				boolean bHardNewline = false;
				int newlinePos = text.indexOf('\n', pos);
				int spacePos = text.indexOf(' ', pos);
				if (newlinePos != -1 && // there is a newline and either
						((spacePos == -1) || // 1. there is no space,
						// or
								(spacePos != -1 && newlinePos < spacePos)))
				// 2. the newline is first
				{
					pos = newlinePos;
					bHardNewline = true;
				} else {
					pos = spacePos;
					bHardNewline = false;
				}
				// Couldn't find another space?
				if (pos == -1) {
					s = text.substring(fromIndex);
				} else {
					s = text.substring(fromIndex, pos);
				}
				// If the string fits, keep track of it.
				if (fm == null || fm.stringWidth(s) <= width) {
					largestString = s;
					bestpos = pos;
					// If we've hit the end of the
					// string or a newline, use it.
					if (bHardNewline)
						bestpos++;
					if (pos == -1 || bHardNewline)
						break;
				} else {
					break;
				}
				++pos;
			}
			if (largestString == null) {
				// Couldn't wrap at a space, so find the largest line
				// that fits and print that. Note that this will be
				// slightly off -- the width of a string will not necessarily
				// be the sum of the width of its characters, due to kerning.
				int totalWidth = 0;
				int oneCharWidth = 0;
				pos = fromIndex;
				while (pos < text.length()) {
					oneCharWidth = fm.charWidth(text.charAt(pos));
					if ((totalWidth + oneCharWidth) > width)
						break;
					totalWidth += oneCharWidth;
					++pos;
				}
				if (pos - fromIndex == 0) {
					// Cant fit anything!
					break;
				}
				lines.addElement(text.substring(fromIndex, pos));
				fromIndex = pos;
			} else {
				lines.addElement(largestString);
				fromIndex = bestpos;
			}
		}
		return lines;
	}

	protected void drawAlignedString(Graphics g, String s, int x, int y, int width) {
		int drawx;
		int drawy;
		drawx = x;
		drawy = y + baseline;
		if (m_nHAlign != Canvas.LEFT_ALIGNMENT) {
			int sw;
			sw = fm.stringWidth(s);
			if (m_nHAlign == Canvas.CENTER_ALIGNMENT) {
				drawx += (width - sw) / 2;
			} else if (m_nHAlign == Canvas.RIGHT_ALIGNMENT) {
				drawx = drawx + width - sw;
			}
		}
		g.drawString(s, drawx, drawy);
	}

	class WrappingLabelLayout implements LayoutManager {
		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			FontMetrics fm = parent.getFontMetrics(parent.getFont());
			Vector<String> l = breakIntoLines(getText() == null ? "" : getText(),
					parent.getWidth() == 0 ? Integer.MAX_VALUE : parent.getWidth(), fm);
			int h = 0;
			int w = 0;
			for (String a : l) {
				h += fm.getHeight();
				w = Math.max(w, fm.stringWidth(a));
			}
			return new Dimension(w, h);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			FontMetrics fm = parent.getFontMetrics(parent.getFont());
			return new Dimension(0,
					fm.getHeight() * breakIntoLines(getText() == null ? "" : getText(), parent.getWidth(), fm).size());
		}

		@Override
		public void layoutContainer(Container parent) {
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		WrappingLabel l1 = new WrappingLabel();
		// l1.setText("Line 1\nLine 2\nLine 3\nLine 4 should be a lot longer and
		// give a larger preferred size");
		// l1.setText("Line 4 should be a lot longer and give a larger preferred
		// size");
		// l1.setText("Line 1\nLine 2\nLine 3 should be a lot longer and give a
		// larger preferred size");
		l1.setText("This is a single line");
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(l1, BorderLayout.NORTH);
		f.pack();
		f.setVisible(true);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(10000);
					SwingUtilities.invokeLater(() -> l1.setText("AAAAAAAAAAAAA"));
				} catch (Exception e) {
				}
			}
		}.start();
	}
}