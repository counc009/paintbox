package com.acc240.paintbox.properties;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.listener.SuperListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

public class AddMenuListener extends SuperListener {

    private final AddMenuPanel panel;
    private JButton add, delete;
    private ArrayList<JCheckBox> deletes;
    private boolean changed = false;

    public AddMenuListener(AddMenuPanel panel) {
        this.panel = panel;
    }

    public void setAdd(JButton button) {
        add = button;
    }

    public void setDelete(JButton button) {
        delete = button;
    }

    public void setDeletes(ArrayList<JCheckBox> boxes) {
        deletes = boxes;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == add) {
            addNew();
        } else if (event.getSource() == delete) {
            deleteSelected();
        }
    }

    @Override
    public void windowClosing(WindowEvent event) {
        boolean close = true;
        if (changed) {
            int reply = JOptionPane.showConfirmDialog(panel.getFrame(), "Do you want to save the changes?");
            if (reply == JOptionPane.OK_OPTION) {
                Operations.properties.save();
            }
            if (reply == JOptionPane.CANCEL_OPTION) {
                close = false;
            }
        }
        if (close) {
            panel.getFrame().dispose();
        }
    }

    public void addNew() {
        String name = JOptionPane.showInputDialog("Please enter the shape's name.");

        if (name != null) {
            File file = Resources.getFile("Open", "shape files (*.shp", "shp");
            if (file != null) {
                String path = file.getPath();
                panel.add(name, path);
                changed = true;
                panel.refresh();
            }
        }
    }

    public void deleteSelected() {
        boolean change = false;
        for (int i = 0; i < deletes.size(); i++) {
            if (deletes.get(i).isSelected()) {
                panel.delete(i);
                change = true;
            }
        }
        if (change) {
            panel.refresh();
            changed = true;
        }
    }
}
