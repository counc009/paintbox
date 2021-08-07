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
package com.acc240.paintbox.properties;

import com.acc240.paintbox.Operations;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ColorPalletSelect extends JFrame {

    private final ArrayList<JButton> deletes = new ArrayList<>();
    private final ArrayList<JButton> changes = new ArrayList<>();
    private final ArrayList<JLabel> names = new ArrayList<>();
    private JButton add;

    private final ColorPalletSelectListener listener = new ColorPalletSelectListener(this);

    public ColorPalletSelect(PropertiesPanel panel) {
        super("Set Color Pallets");
        addWindowListener(listener);
        listener.setPropertiesPanel(panel);

        refresh();
    }

    public final void refresh() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel palletsPanel = new JPanel();
        palletsPanel.setLayout(new GridLayout(Operations.properties.getColorPallet().size(), 3, 5, 5));

        for (String fileName : Operations.properties.getColorPallet()) {
            JLabel label = new JLabel(getPalletName(fileName));
            names.add(label);
            palletsPanel.add(label);

            JButton change = new JButton("Change Pallet");
            change.addActionListener(listener);
            changes.add(change);
            palletsPanel.add(change);

            JButton delete = new JButton("X");
            delete.setForeground(Color.red);
            delete.addActionListener(listener);
            deletes.add(delete);
            palletsPanel.add(delete);
        }
        panel.add(palletsPanel);

        add = new JButton("Add New Color Pallet");
        add.addActionListener(listener);
        panel.add(add);
        panel.add(Box.createVerticalStrut(15));
        panel.add(Box.createVerticalGlue());

        listener.setAdd(add);
        listener.setNames(names);
        listener.setChanges(changes);
        listener.setDeletes(deletes);

        getContentPane().removeAll();
        getContentPane().add(panel);
        pack();
        setVisible(true);
        repaint();
    }

    private String getPalletName(String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        if (name.contains("Pallet")) {
            name = name.substring(0, name.indexOf("Pallet"));
        }

        name = name.substring(name.lastIndexOf("\\") + 1);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}
