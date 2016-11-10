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

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 */
public class FontLabel
    extends JTextField {
    private int fixedPreviewSize;
    private Font chosenFont;
    private boolean showSize;

    /**
     * Creates a new FontLabel object.
     */
    public FontLabel() {
        this(null);
    }

    /**
     * Creates a new FontLabel object.
     *
     * @param chosenFont DOCUMENT ME!
     */
    public FontLabel(Font chosenFont) {
        this(chosenFont, FontUtil.getUIManagerLabelFontOrDefault("Label.font").getSize());
    }

    /**
     * Creates a new FontLabel object.
     *
     * @param chosenFont DOCUMENT ME!
     * @param fixedPreviewSize DOCUMENT ME!
     */
    public FontLabel(Font chosenFont, int fixedPreviewSize) {
        super();
        setFixedPreviewSize(fixedPreviewSize);
        setChosenFont(chosenFont);
        setEditable(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fixedPreviewSize DOCUMENT ME!
     */
    public void setFixedPreviewSize(int fixedPreviewSize) {
        this.fixedPreviewSize = fixedPreviewSize;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFixedPreviewSize() {
        return fixedPreviewSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    public void setChosenFont(Font f) {
        setFont(( f == null || f.getName().equals(FontChooser.AUTOMATIC) ? FontUtil.getUIManagerLabelFontOrDefault("Label.font") : f ).deriveFont((float)fixedPreviewSize));
        this.chosenFont = f;
        rebuildText();
    }
    
    void rebuildText() {
        if (chosenFont != null && !chosenFont.getName().equals(FontChooser.AUTOMATIC)) {
            setText(chosenFont.getName() + ( showSize ? ( "," + chosenFont.getSize() + "pt") : "" ) );
        }
        else {
            setText("Automatic");
        }
    }
    
    public void setShowSize(boolean showSize) {
        this.showSize = showSize;
        rebuildText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getChosenFont() {
        return chosenFont == null || chosenFont.getName().equals(FontChooser.AUTOMATIC) ? null : chosenFont;
    }
}
