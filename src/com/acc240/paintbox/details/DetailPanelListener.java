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
