/*----------------------------------------------------
 * PaintBox is a free open source painting program
 * Copyright (C) 2021 Aaron Councilman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *--------------------------------------------------*/
package com.acc240.paintbox.details;

import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class PointsScrollPanel extends JPanel {

    private final ArrayList<JPanel> content;
    private JPanel contentPanel;
    private final JScrollBar scroll;

    public PointsScrollPanel(ArrayList<JPanel> content) {
        this.content = content;

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < Math.min(10, content.size()); i++) {
            contentPanel.add(content.get(i));
        }

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(contentPanel);
        if (content.size() > 10) {
            scroll = new JScrollBar(JScrollBar.VERTICAL, 0, 10, 0, content.size());
            scroll.addAdjustmentListener(new PointsScrollPanelListener(this, scroll));
            add(scroll);
        } else {
            scroll = new JScrollBar(JScrollBar.VERTICAL, 0, content.size(), 0, content.size());
            scroll.addAdjustmentListener(new PointsScrollPanelListener(this, scroll));
            add(scroll);
        }
    }

    public ArrayList<JPanel> getContent() {
        return content;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        removeAll();
        add(contentPanel);
        add(scroll);
        repaint();
    }
}
