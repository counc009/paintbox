package com.acc240.paintbox.details;

import com.acc240.paintbox.AlphaButton;
import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.FillableShape;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FillPanel extends JPanel {

    public static final int TOOLS = 0;
    public static final int DETAILS = 1;

    private Color defaultColor;
    private JCheckBox fill;
    private JButton fillColor;
    private final int mode;

    public FillPanel(int mode) {
        this.mode = mode;
        FillPanelListener listener = new FillPanelListener(this);

        if (mode == TOOLS) {
            fill = new JCheckBox("Filled");
            fill.setEnabled(false);
            fill.addActionListener(listener);
            fill.setBackground(Color.gray);
            fill.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);

            fillColor = new AlphaButton("Fill Color");
            fillColor.setMnemonic('f');
            fillColor.setEnabled(false);
            fillColor.addActionListener(listener);

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.gray);

            add(fill);
            add(fillColor);

            defaultColor = fillColor.getBackground();
        } else if (mode == DETAILS) {
            fill = new JCheckBox("Filled");
            fill.addActionListener(listener);

            fillColor = new AlphaButton();
            if (Operations.getSelected() != null && Operations.getSelected() instanceof FillableShape) {
                fillColor.setBackground(((FillableShape) (Operations.getSelected())).getFilling());
            } else {
                fillColor.setBackground(Operations.getFill());
            }
            fillColor.addActionListener(listener);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(fill);
            add(new JLabel("    Fill Color:  "));
            add(fillColor);

            setEnabled(true);
        } else {
            throw (new IllegalArgumentException("FillPanel mode " + mode + " is not recognized"));
        }
    }

    @Override
    public final void setEnabled(boolean on) {
        if (on) {
            fill.setEnabled(true);
            if ((Operations.getMode() == Operations.SELECT && Operations.getSelected() != null && Operations.getSelected() instanceof FillableShape) || mode == DETAILS) {
                if (Operations.getSelected() != null && Operations.getSelected() instanceof FillableShape && ((FillableShape) Operations.getSelected()).getFilled()) {
                    fillColor.setEnabled(true);
                    fillColor.setBackground(((FillableShape) Operations.getSelected()).getFilling());

                    fill.setSelected(true);
                } else {
                    if (mode == DETAILS) {
                        fillColor.setEnabled(true);
                        fillColor.setBackground(Operations.getFill());
                    } else {
                        fillColor.setEnabled(false);
                        fillColor.setBackground(defaultColor);
                    }

                    fill.setSelected(false);
                }
            }
        } else {
            fill.setEnabled(false);
            fill.setSelected(false);

            fillColor.setEnabled(false);
            fillColor.setBackground(defaultColor);
        }
    }

    public void setColorEnabled(boolean on) {
        fillColor.setEnabled(on);
    }

    public void setColor() {
        setColor(Operations.getBorder());
    }

    public void setColor(Color color) {
        fillColor.setBackground(color);
    }

    public boolean colorEnabled() {
        return fill.isSelected();
    }

    public void setFilled(boolean bool) {
        fill.setSelected(bool);
    }
}
