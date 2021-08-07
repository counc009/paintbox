package com.acc240.paintbox.details;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.Text;
import com.acc240.paintbox.listener.SuperListener;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class FontListener extends SuperListener {

    JCheckBox bold, italic;

    public void setBold(JCheckBox bold) {
        this.bold = bold;
    }

    public void setItalic(JCheckBox italic) {
        this.italic = italic;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JComboBox && ((JComboBox) event.getSource()).getSelectedItem() instanceof String) {
            Script.action(event);
        } else if (event.getSource() instanceof JComboBox) {
            Size.action(event);
        } else if (event.getSource() instanceof JCheckBox) {
            Style.action(bold, italic);
        }
    }

    private static class Script {

        public static void action(ActionEvent event) {
            JComboBox box = (JComboBox) event.getSource();
            String script = (String) box.getSelectedItem();

            Font font = ((Text) Operations.getSelected()).getFont();
            Font newFont = new Font(script, font.getStyle(), font.getSize());

            ((Text) Operations.getSelected()).setFont(newFont);
            Operations.setChanged();
            Operations.update();
        }
    }

    private static class Size {

        public static void action(ActionEvent event) {
            JComboBox box = (JComboBox) event.getSource();
            int size = (Integer) box.getSelectedItem();

            Font font = ((Text) Operations.getSelected()).getFont();
            Font newFont = new Font(font.getFamily(), font.getStyle(), size);

            ((Text) Operations.getSelected()).setFont(newFont);
            Operations.setChanged();
            Operations.update();
        }
    }

    private static class Style {

        public static void action(JCheckBox bold, JCheckBox italic) {
            int style = Font.PLAIN;
            if (bold.isSelected()) {
                style = Font.BOLD;
            }
            if (italic.isSelected()) {
                style += Font.ITALIC;
            }

            Font font = ((Text) Operations.getSelected()).getFont();
            Font newFont = new Font(font.getFamily(), style, font.getSize());

            ((Text) Operations.getSelected()).setFont(newFont);
            Operations.setChanged();
            Operations.update();
        }
    }
}
