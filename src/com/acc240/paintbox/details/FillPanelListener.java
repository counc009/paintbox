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
