package com.acc240.paintbox.details;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.FillableShape;
import com.acc240.paintbox.geom.Shape;
import com.acc240.paintbox.geom.Picture;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class DetailPanel extends JTabbedPane {

    private final JLabel layer;
    private Shape selected = null;
    private final DetailPanelListener listener;

    private final JButton borderColor, up, down, export, center;
    private final JTextField strokeWidth;
    private final FillPanel fillPanel;
    private final JComboBox moveTo;

    public DetailPanel() {
        listener = new DetailPanelListener(this);

        JPanel apperance = new JPanel();
        apperance.setLayout(new BoxLayout(apperance, BoxLayout.Y_AXIS));
        {
            JPanel border = new JPanel();
            border.setLayout(new BoxLayout(border, BoxLayout.X_AXIS));
            border.add(new JLabel("Border Color: "));
            borderColor = new JButton();
            borderColor.setBackground(Operations.getBorder());
            borderColor.addActionListener(listener);
            border.add(borderColor);
            apperance.add(border);

            fillPanel = new FillPanel(FillPanel.DETAILS);
            apperance.add(fillPanel);

            JPanel stroke = new JPanel();
            stroke.setLayout(new BoxLayout(stroke, BoxLayout.X_AXIS));
            stroke.add(new JLabel("Stroke Width: "));
            strokeWidth = new JTextField("1.0");
            strokeWidth.setMaximumSize(new Dimension(50, strokeWidth.getPreferredSize().height));
            strokeWidth.addActionListener(listener);
            stroke.add(strokeWidth);
            stroke.setMaximumSize(new Dimension(Integer.MAX_VALUE, strokeWidth.getPreferredSize().height));
            apperance.add(stroke);
            apperance.add(Box.createVerticalGlue());
        }
        add(apperance, "Apperance");

        JPanel shape = new JPanel();
        shape.setLayout(new BoxLayout(shape, BoxLayout.Y_AXIS));
        {

        }
        add(shape, "Shape");

        JPanel layers = new JPanel();
        layers.setLayout(new BoxLayout(layers, BoxLayout.Y_AXIS));
        {
            JPanel layerNumber = new JPanel();
            layerNumber.setLayout(new BoxLayout(layerNumber, BoxLayout.X_AXIS));
            layerNumber.add(new JLabel("Layer: "));
            layer = new JLabel("-1");
            layerNumber.add(layer);
            layers.add(layerNumber);

            JPanel upDown = new JPanel();
            upDown.setLayout(new BoxLayout(upDown, BoxLayout.X_AXIS));
            up = new JButton("Move Forward One Layer");
            up.addActionListener(listener);
            down = new JButton("Move Backward One Layer");
            down.addActionListener(listener);
            upDown.add(up);
            upDown.add(down);
            layers.add(upDown);

            JPanel setLayer = new JPanel();
            setLayer.setLayout(new BoxLayout(setLayer, BoxLayout.X_AXIS));
            setLayer.add(new JLabel("Move to Layer: "));
            moveTo = new JComboBox();
            moveTo.setMaximumSize(new Dimension(50, 20));
            moveTo.addActionListener(listener);
            setLayer.add(moveTo);
            layers.add(setLayer);
        }
        add(layers, "Layers");

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        {
            export = new JButton("Export Shape");
            export.addActionListener(listener);
            options.add(export);

            center = new JButton("Center Shape");
            center.addActionListener(listener);
            options.add(center);
        }
        add(options, "Options");

        update();
    }

    @Override
    public void setEnabled(boolean enabled) {
        setComponentAt(1, new JPanel());
        setSelectedIndex(1);
        setEnabledAt(0, enabled);
        setEnabledAt(1, enabled);
        setEnabledAt(2, enabled);
        setEnabledAt(3, enabled);
        if (enabled) {
            setSelectedIndex(0);
            update();
        }
    }

    public void setSelected(Shape selected) {
        this.selected = selected;
        update();
    }

    public final void update() {
        fillPanel.setEnabled(true);
        if (selected == null) {
            setSelectedIndex(0);
            setEnabledAt(1, false);
            setEnabledAt(2, false);
            setEnabledAt(3, false);
        } else {
            if (selected instanceof Picture) {
                if (getSelectedIndex() == 0) {
                    setSelectedIndex(1);
                }
                setEnabledAt(0, false);
            }
            setEnabledAt(1, true);
            setEnabledAt(2, true);
            setEnabledAt(3, true);

            setComponentAt(1, selected.getPointPanel());

            borderColor.setBackground(selected.getColor());
            strokeWidth.setText(String.valueOf(selected.getStrokeWidth()));
            layer.setText("" + (Operations.getIndex(selected) + 1));

            moveTo.removeActionListener(listener);
            moveTo.removeAllItems();
            for (int i = 0; i < Operations.getShapes().size(); i++) {
                moveTo.addItem("" + (i + 1));
            }
            moveTo.setSelectedIndex(Operations.getIndex(selected));
            moveTo.addActionListener(listener);

            if (!(selected instanceof FillableShape)) {
                fillPanel.setEnabled(false);
            }
        }
    }

    public JButton getBorderColor() {
        return borderColor;
    }

    public JTextField getStrokeWidth() {
        return strokeWidth;
    }

    public JButton getUp() {
        return up;
    }

    public JButton getDown() {
        return down;
    }

    public JComboBox getMoveTo() {
        return moveTo;
    }

    public JButton getExport() {
        return export;
    }

    public JButton getCenter() {
        return center;
    }

    public FillPanel getFillPanel() {
        return fillPanel;
    }
}
