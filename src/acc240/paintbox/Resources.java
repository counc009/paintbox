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
package acc240.paintbox;

import acc240.paintbox.colorSelect.MemoryColorChooser;
import acc240.paintbox.geom.Shape;

import java.awt.Color;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Resources {

    public static void getColor(String title, Color color, int mode, JComponent caller) {
        MemoryColorChooser chooser = new MemoryColorChooser(title, color, Operations.getFrame(), mode, caller);
    }

    public static void getColor(String title, Color color, int mode, JComponent caller, boolean properties) {
        if (properties) {
            MemoryColorChooser chooser = new MemoryColorChooser(title, color, Operations.getFrame(), mode, caller, properties);
        } else {
            getColor(title, color, mode, caller);
        }
    }

    public static File getFile(String title, String fileExtensionDesc, String... fileExtension) {
        JFileChooser chooser = new JFileChooser(Operations.properties.getPath());
        if (!Operations.properties.getPath().equals("NONE") && fileExtension[0].equals("drw")) {
            chooser.setCurrentDirectory(new File(Operations.properties.getPath()));
        } else if (fileExtension[0].equals("clp")) {
            chooser.setCurrentDirectory(new File(Operations.properties.getColorPallet().get(0)));
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileExtensionDesc, fileExtension);
        chooser.setFileFilter(filter);

        int status = chooser.showDialog(Operations.getFrame(), title);

        if (status == JFileChooser.APPROVE_OPTION) {
            Operations.properties.setPath(chooser.getSelectedFile().getAbsolutePath());
            Operations.properties.save();
            return chooser.getSelectedFile();
        }
        return null;
    }

    public static void addCopy(Shape result, Shape caller) {
        Operations.addShape(result);
        switch (Operations.properties.getCopyLoc()) {
            case Properties.BOTTOM:
                Operations.toBack(result);
                break;
            case Properties.INFRONT:
                for (int i = 1; i < Operations.getShapes().size() - 1 - Operations.getIndex(caller); i++) {
                    Operations.pushBack(result);
                }
                break;
            case Properties.BEHIND:
                for (int i = 1; i <= Operations.getShapes().size() - 1 - Operations.getIndex(caller); i++) {
                    Operations.pushBack(result);
                }
                break;
        }
    }
}
