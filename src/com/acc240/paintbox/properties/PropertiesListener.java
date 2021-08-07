package com.acc240.paintbox.properties;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Properties;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.colorSelect.MemoryColorChooser;
import com.acc240.paintbox.listener.SuperListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PropertiesListener extends SuperListener {

    private JComboBox cursors, copyLocs;
    private JButton border, background, fill, addM, save, restore;
    private JCheckBox filled, chooser, borderFill;
    private final JFrame frame;
    private JButton colorPallet;
    private JTextField width, height;
    private final boolean changedPallet = false;
    private final PropertiesPanel caller;
    private JRadioButton toolTop, toolBottom, detailLeft, detailRight;

    public PropertiesListener(JFrame frame, PropertiesPanel panel) {
        this.frame = frame;
        caller = panel;
    }

    public void setCursors(JComboBox box) {
        cursors = box;
    }

    public void setCopyLocs(JComboBox box) {
        copyLocs = box;
    }

    public void setPallet(JButton pallet) {
        colorPallet = pallet;
    }

    public void setBorder(JButton button) {
        border = button;
    }

    public void setBackground(JButton button) {
        background = button;
    }

    public void setFill(JButton button) {
        fill = button;
    }

    public void setFilled(JCheckBox box) {
        filled = box;
    }

    public void setBorderFill(JCheckBox box) {
        borderFill = box;
    }

    public void setChooser(JCheckBox box) {
        chooser = box;
    }

    public void setWidth(JTextField field) {
        width = field;
    }

    public void setHeight(JTextField field) {
        height = field;
    }

    public void setToolTop(JRadioButton button) {
        toolTop = button;
    }

    public void setToolBottom(JRadioButton button) {
        toolBottom = button;
    }

    public void setDetailLeft(JRadioButton button) {
        detailLeft = button;
    }

    public void setDetailRight(JRadioButton button) {
        detailRight = button;
    }

    public void setAddMenu(JButton button) {
        addM = button;
    }

    public void setSave(JButton button) {
        save = button;
    }

    public void setRestore(JButton button) {
        restore = button;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource();

        if (source == border) {
            Resources.getColor("Pick Default Border Color", source.getBackground(), MemoryColorChooser.BORDER, (JComponent) event.getSource(), true);
        }
        if (source == colorPallet) {
            new ColorPalletSelect(caller);
        } else if (source == fill) {
            Resources.getColor("Pick Default Fill Color", source.getBackground(), MemoryColorChooser.FILL, (JComponent) event.getSource(), true);
        } else if (source == background) {
            Resources.getColor("Pick Default Background Color", source.getBackground(), MemoryColorChooser.BACKGROUND, (JComponent) event.getSource(), true);
        } else if (source instanceof JRadioButton) {
            if (((JRadioButton) source).getText().equals("Radians")) {
                Operations.properties.setRadianUse(true);
            } else if (((JRadioButton) source).getText().equals("Degrees")) {
                Operations.properties.setRadianUse(false);
            } else if (((JRadioButton) source).getText().equals("0 - 255")) {
                Operations.properties.setRGB255(true);
            } else {
                Operations.properties.setRGB255(false);
            }
        } else if (source == borderFill) {
            Operations.properties.setBorderFill(borderFill.isSelected());
            fill.setEnabled(!borderFill.isSelected());
            if (borderFill.isSelected()) {
                fill.setBackground(Color.white);
                Operations.properties.setFill(Color.white);
            }
        } else if (source == filled) {
            Operations.properties.setFilled(filled.isSelected());
            if (filled.isSelected()) {
                borderFill.setSelected(false);
                Operations.properties.setBorderFill(false);
            }
            borderFill.setEnabled(filled.isSelected());
            fill.setEnabled(filled.isSelected());
        } else if (source == width) {
            Operations.properties.setDefaultSizeWidth(Integer.parseInt(width.getText()));
        } else if (source == height) {
            Operations.properties.setDefaultSizeHeight(Integer.parseInt(height.getText()));
        }
        if (source == toolTop || source == toolBottom) {
            if (toolTop.isSelected()) {
                Operations.properties.setToolsLocation(Properties.TOOLS_TOP);
            } else {
                Operations.properties.setToolsLocation(Properties.TOOLS_BOTTOM);
            }
        } else if (source == detailLeft || source == detailRight) {
            if (detailLeft.isSelected()) {
                Operations.properties.setDetailsLocation(Properties.DETAILS_LEFT);
            } else {
                Operations.properties.setDetailsLocation(Properties.DETAILS_RIGHT);
            }
        }

        Operations.properties.setMode(cursors.getSelectedIndex());
        Operations.properties.setCopyLoc(copyLocs.getSelectedIndex());

        if (source == save) {
            Operations.properties.save();
            frame.dispose();
            Operations.reload();
        } else if (source == restore) {
            Operations.properties.restore();
            Operations.properties.save();
            frame.dispose();
            Operations.reload();
        } else if (source == addM) {
            new AddMenuPanel();
        }
    }

    @Override
    public void windowClosing(WindowEvent event) {
        Operations.properties.load();
        ((JFrame) event.getSource()).dispose();
    }
}
