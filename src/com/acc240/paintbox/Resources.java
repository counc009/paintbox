package com.acc240.paintbox;

import com.acc240.paintbox.colorSelect.MemoryColorChooser;
import com.acc240.paintbox.geom.Shape;

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
