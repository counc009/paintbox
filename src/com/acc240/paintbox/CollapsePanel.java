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
package com.acc240.paintbox;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CollapsePanel extends JPanel {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private JComponent normalContent, collapsableContent, container;
    private JButton button;
    private int location;
    private boolean expanded;

    public CollapsePanel(JComponent normalContent, JComponent collapsableContent, JComponent container, boolean expanded) {
        this(normalContent, collapsableContent, container, expanded, RIGHT);
    }

    public CollapsePanel(JComponent normalContent, JComponent collapsableContent, JComponent container, boolean expanded, int location) {
        this.normalContent = normalContent;
        this.collapsableContent = collapsableContent;
        this.container = container;
        this.location = location;
        this.expanded = expanded;
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        setup();
        if (expanded) {
            expand();
        } else {
            collapse();
        }
    }

    private void setup() {
        if (location == RIGHT) {
            button = new JButton(new ImageIcon("close.gif"));
        } else {
            button = new JButton(new ImageIcon("open.gif"));
        }
        button.setPreferredSize(new Dimension(20, 20));
        button.setBackground(Color.white);
        button.addActionListener(new CollapsePanelListener(this));
        add(button);
        setPreferredSize(new Dimension(20, 20));
        setBackground(Color.gray);
    }

    public void setLocation(int loc) {
        location = loc;
        if (expanded) {
            expand();
        } else {
            collapse();
        }
    }

    private void expand() {
        container.removeAll();
        if (location == RIGHT) {
            button.setIcon(new ImageIcon("close.gif"));
        } else {
            button.setIcon(new ImageIcon("open.gif"));
        }
        switch (location) {
            case LEFT:
                container.add(collapsableContent);
                container.add(this);
                container.add(normalContent);
                break;
            case RIGHT:
                container.add(normalContent);
                container.add(this);
                container.add(collapsableContent);
                break;
        }
        container.repaint();
        Operations.getFrame().pack();
    }

    private void collapse() {
        container.removeAll();
        if (location == RIGHT) {
            button.setIcon(new ImageIcon("open.gif"));
        } else {
            button.setIcon(new ImageIcon("close.gif"));
        }
        switch (location) {
            case LEFT:
                container.add(this);
                container.add(normalContent);
                break;
            case RIGHT:
                container.add(normalContent);
                container.add(this);
                break;
        }
        container.repaint();
        Operations.getFrame().pack();
    }

    public boolean getExpanded() {
        return expanded;
    }

    public void clicked() {
        expanded = !expanded;
        if (expanded) {
            expand();
        } else {
            collapse();
        }
    }

    public int getTotalWidth() {
        if (expanded) {
            return getSize().width + collapsableContent.getSize().width;
        } else {
            return getSize().width;
        }
    }
}
