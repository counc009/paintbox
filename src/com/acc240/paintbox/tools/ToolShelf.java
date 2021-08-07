package com.acc240.paintbox.tools;

import com.acc240.paintbox.AlphaButton;
import com.acc240.paintbox.Operations;
import com.acc240.paintbox.details.FillPanel;
import com.acc240.paintbox.geom.FillableShape;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ToolShelf extends JPanel {

    private final ArrayList<JButton> cursorModes;
    private final JButton border, move, rotate, delete, copy;
    private final FillPanel fill;
    private final Color defaultColor;
    private final JPanel insideOf;

    public ToolShelf(JPanel inside) {
        ToolShelfListener listener = new ToolShelfListener(this);
        addKeyListener(listener);

        JButton cursor = new JButton(new ImageIcon("cursor.gif"));
        cursor.addActionListener(listener);
        cursor.setMnemonic('c');

        JButton select = new JButton("Select");
        select.addActionListener(listener);
        select.setMnemonic('s');

        JButton rectangle = new JButton(new ImageIcon("rectangle.gif"));
        rectangle.addActionListener(listener);
        rectangle.setMnemonic('r');

        JButton oval = new JButton(new ImageIcon("oval.gif"));
        oval.addActionListener(listener);
        oval.setMnemonic('o');

        JButton line = new JButton(new ImageIcon("line.gif"));
        line.addActionListener(listener);
        line.setMnemonic('l');

        JButton polyline = new JButton(new ImageIcon("polyline.gif"));
        polyline.addActionListener(listener);
        polyline.setMnemonic('p');

        JButton polygon = new JButton(new ImageIcon("polygon.gif"));
        polygon.addActionListener(listener);
        polygon.setMnemonic('g');

        border = new AlphaButton("Border");
        border.addActionListener(listener);
        border.setMnemonic('b');
        border.setBackground(Operations.getBorder());

        fill = new FillPanel(FillPanel.TOOLS);

        //move = new JButton(" Move ");
        move = new JButton(new ImageIcon("transform.gif"));
        move.addActionListener(listener);
        move.setMnemonic('m');
        move.setEnabled(false);

        //rotate = new JButton("Rotate");
        rotate = new JButton(new ImageIcon("rotate.gif"));
        rotate.addActionListener(listener);
        rotate.setMnemonic('r');
        rotate.setEnabled(false);

        JPanel transformPanel = new JPanel();
        transformPanel.setLayout(new BoxLayout(transformPanel, BoxLayout.Y_AXIS));
        transformPanel.setBackground(Color.gray);
        transformPanel.add(move);
        transformPanel.add(rotate);

        delete = new JButton("Delete");
        delete.addActionListener(listener);
        delete.setMnemonic('d');
        delete.setEnabled(false);

        copy = new JButton(" Copy ");
        copy.addActionListener(listener);
        copy.setEnabled(false);
        copy.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel addRemovePanel = new JPanel();
        addRemovePanel.setLayout(new BoxLayout(addRemovePanel, BoxLayout.Y_AXIS));
        addRemovePanel.setBackground(Color.gray);
        addRemovePanel.add(copy);
        addRemovePanel.add(delete);

        cursorModes = new ArrayList<>();
        cursorModes.add(cursor);
        cursorModes.add(select);
        cursorModes.add(rectangle);
        cursorModes.add(oval);
        cursorModes.add(line);
        cursorModes.add(polyline);
        cursorModes.add(polygon);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.gray);

        add(cursor);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(select);
        add(Box.createRigidArea(new Dimension(15, 0)));
        add(rectangle);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(oval);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(line);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(polyline);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(polygon);
        add(Box.createRigidArea(new Dimension(15, 0)));
        add(border);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(fill);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(transformPanel);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(addRemovePanel);
        add(Box.createHorizontalGlue());

        defaultColor = cursor.getBackground();

        cursorModes.get(Operations.getMode()).setBackground(Color.yellow);

        insideOf = inside;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public JButton getMode() {
        if (Operations.getMode() < cursorModes.size()) {
            return cursorModes.get(Operations.getMode());
        }
        if (Operations.getLastMode() < cursorModes.size()) {
            return cursorModes.get(Operations.getLastMode());
        }
        return cursorModes.get(Operations.CURSOR);
    }

    public JButton getDefaultButton() {
        return cursorModes.get(0);
    }

    public ArrayList<JButton> getCursorModes() {
        return cursorModes;
    }

    public JButton getMove() {
        return move;
    }

    public JButton getRotate() {
        return rotate;
    }

    public JButton getDelete() {
        return delete;
    }

    public JButton getCopy() {
        return copy;
    }

    public JButton getBorderButton() {
        return border;
    }

    public FillPanel getFill() {
        return fill;
    }

    public void updateSelected() {
        if (Operations.getSelected() != null && Operations.getMode() == Operations.SELECT) {
            Operations.setBorder(Operations.getSelected().getColor());
            if (Operations.getSelected() instanceof FillableShape) {
                fill.setEnabled(true);
            } else {
                fill.setEnabled(false);
            }
            move.setEnabled(true);
            rotate.setEnabled(true);
            delete.setEnabled(true);
            copy.setEnabled(true);

            border.setBackground(Operations.getBorder());
        } else {
            fill.setEnabled(false);
            move.setEnabled(false);
            rotate.setEnabled(false);
            delete.setEnabled(false);
            copy.setEnabled(false);
            border.setBackground(Operations.getBorder());
        }

        border.setBackground(Operations.getBorder());
    }

    public void setBorderColor(Color color) {
        border.setBackground(color);
    }

    @Override
    public void setEnabled(boolean on) {
        if (on) {
            for (JButton cursor : cursorModes) {
                cursor.setEnabled(true);
            }
            border.setEnabled(true);
            fill.setEnabled(true);
            move.setEnabled(true);
            rotate.setEnabled(true);
            delete.setEnabled(true);
            copy.setEnabled(true);
            updateSelected();
        } else {
            for (JButton cursor : cursorModes) {
                cursor.setEnabled(false);
            }
            border.setEnabled(false);
            border.setBackground(defaultColor);
            fill.setEnabled(false);
            move.setEnabled(false);
            rotate.setEnabled(false);
            delete.setEnabled(false);
            copy.setEnabled(false);
        }
    }
}
