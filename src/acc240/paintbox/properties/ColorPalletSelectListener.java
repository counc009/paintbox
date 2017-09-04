/*----------------------------------------------------
 * PaintBox is a free open source painting program
 * Copyright (C) 2014 PaintBox Foundation
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
package acc240.paintbox.properties;

import acc240.paintbox.Operations;
import acc240.paintbox.Resources;
import acc240.paintbox.listener.SuperListener;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ColorPalletSelectListener extends SuperListener {

    private ArrayList<JButton> deletes;
    private ArrayList<JButton> changes;
    private ArrayList<JLabel> names;
    private JButton add;
    private final ColorPalletSelect creator;
    private PropertiesPanel panel;

    public ColorPalletSelectListener(ColorPalletSelect creator) {
        this.creator = creator;
    }

    public void setAdd(JButton add) {
        this.add = add;
    }

    public void setDeletes(ArrayList<JButton> dels) {
        deletes = dels;
    }

    public void setChanges(ArrayList<JButton> chngs) {
        changes = chngs;
    }

    public void setNames(ArrayList<JLabel> names) {
        this.names = names;
    }

    public void setPropertiesPanel(PropertiesPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton source = (JButton) event.getSource();

        if (source == add) {
            File file = Resources.getFile("Select Color Pallet", "Color Pallet (*.clp)", "clp");
            if (file != null) {
                Operations.properties.addColorPallet(file.getAbsolutePath());
            }
        } else if (deletes.indexOf(source) != -1) {
            int index = deletes.indexOf(source);
            Operations.properties.removeColorPallet(index);
        } else if (changes.indexOf(source) != -1) {
            int index = changes.indexOf(source);

            File file = Resources.getFile("Select Color Pallet", "Color Pallet (*.clp)", "clp");
            if (file != null) {
                Operations.properties.setColorPallet(file.getAbsolutePath(), index);
            }
        }

        creator.refresh();
    }

    @Override
    public void windowClosing(WindowEvent event) {
        panel.toFront();
        panel.repaint();
    }
}
