package com.acc240.paintbox.properties;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.listener.SuperListener;

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
