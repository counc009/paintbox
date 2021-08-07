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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class FillPanelListener extends SuperListener {

    private static FillPanel panel;

    public FillPanelListener(FillPanel panel) {
        FillPanelListener.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JCheckBox) {
            Filled.action(event);
        } else if (event.getSource() instanceof JButton) {
            Filling.action(event);
        }
    }

    private static class Filled {

        public static void action(ActionEvent event) {
            JCheckBox fill = (JCheckBox) event.getSource();
            Operations.setFilled(fill.isSelected());
            Operations.update();
        }
    }

    private static class Filling {

        public static void action(ActionEvent event) {
            Resources.getColor("Pick Fill Color", Operations.getFill(), MemoryColorChooser.FILL, (JComponent) event.getSource());
        }
    }
}
