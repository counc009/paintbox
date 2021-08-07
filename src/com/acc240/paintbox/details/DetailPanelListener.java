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

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.colorSelect.MemoryColorChooser;
import com.acc240.paintbox.listener.SuperListener;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class DetailPanelListener extends SuperListener {

    private final DetailPanel panel;

    public DetailPanelListener(DetailPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == panel.getBorderColor()) {
            Resources.getColor("Pick Border Color", Operations.getBorder(), MemoryColorChooser.BORDER, (JComponent) event.getSource());
            Operations.setChanged();
        } else if (event.getSource() == panel.getStrokeWidth()) {
            Operations.setStrokeWidth(Float.parseFloat(((JTextField) event.getSource()).getText()));
            Operations.setChanged();
            Operations.update();
        } else if (event.getSource() == panel.getUp()) {
            Operations.pullForward(Operations.getSelected());
            Operations.setChanged();
            Operations.update();
            panel.update();
        } else if (event.getSource() == panel.getDown()) {
            Operations.pushBack(Operations.getSelected());
            Operations.setChanged();
            Operations.update();
            panel.update();
        } else if (event.getSource() == panel.getMoveTo()) {
            Operations.setIndex(Operations.getSelected(), panel.getMoveTo().getSelectedIndex());
            Operations.setChanged();
            Operations.update();
            panel.update();
        } else if (event.getSource() == panel.getExport()) {
            Operations.exportShape(Operations.getSelected());
        } else if (event.getSource() == panel.getCenter()) {
            Operations.getSelected().center();
            Operations.setChanged();
            Operations.update();
            panel.update();
        }
    }
}
