package com.acc240.paintbox.properties;

import com.acc240.paintbox.Properties;
import com.acc240.paintbox.Operations;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PropertiesPanel extends JFrame {

    private final Properties properties;

    private final JTextField width, height;
    private final ArrayList<String> mouseModes;
    private final JRadioButton toolTop, toolBottom, detailRight, detailLeft, radians, degress;

    public PropertiesPanel(JFrame frame) {
        PropertiesListener listener = new PropertiesListener(this, this);
        properties = Operations.properties;

        setLocation(frame.getLocationOnScreen());
        Operations.panelOpened();

        mouseModes = new ArrayList<>();
        String[] temp = {"Cursor", "Select", "Rectangle", "Oval", "Line", "Polyline", "Polygon"};
        mouseModes.addAll(Arrays.asList(temp));

        JPanel mode = new JPanel();
        mode.add(new JLabel("Default Mouse Mode:  "));
        JComboBox modes = new JComboBox<>(temp);
        modes.setSelectedItem(modes.getItemAt(properties.getMode()));
        modes.addActionListener(listener);
        mode.add(modes);

        JPanel colorPallet = new JPanel();
        JButton pallet = new JButton("Set Color Pallets");
        pallet.addActionListener(listener);
        colorPallet.add(pallet);

        JPanel border = new JPanel();
        JButton borderButton = new JButton("Default Border Color");
        borderButton.setBackground(properties.getBorder());
        borderButton.addActionListener(listener);
        border.add(borderButton);

        JPanel background = new JPanel();
        JButton backgroundButton = new JButton("Default Background Color");
        backgroundButton.setBackground(properties.getBackground());
        backgroundButton.addActionListener(listener);
        background.add(backgroundButton);

        JPanel fill = new JPanel();
        JCheckBox fillBox = new JCheckBox("Filled By Default", properties.getFilled());
        fillBox.addActionListener(listener);
        JButton fillButton = new JButton("Default Fill Color");
        fillButton.setBackground(properties.getFill());
        fillButton.addActionListener(listener);
        fillButton.setEnabled(properties.getFilled() && !properties.useBorderFill());
        fill.add(fillBox);
        fill.add(fillButton);

        JCheckBox fillBorder = new JCheckBox("Use Border Color For Fill", properties.useBorderFill());
        fillBorder.setEnabled(properties.getFilled());
        fillBorder.addActionListener(listener);

        JRadioButton rgb255 = new JRadioButton("0 - 255");
        rgb255.addActionListener(listener);
        JRadioButton rgb01 = new JRadioButton("0 - 1");
        rgb01.addActionListener(listener);
        if (Operations.properties.useRGB255()) {
            rgb255.setSelected(true);
        } else {
            rgb01.setSelected(true);
        }

        ButtonGroup group = new ButtonGroup();
        group.add(rgb255);
        group.add(rgb01);

        JPanel rgb = new JPanel();
        rgb.setLayout(new BoxLayout(rgb, BoxLayout.X_AXIS));
        rgb.add(new JLabel("RGB Range:"));
        rgb.add(rgb255);
        rgb.add(rgb01);

        radians = new JRadioButton("Radians");
        radians.addActionListener(listener);
        degress = new JRadioButton("Degrees");
        degress.addActionListener(listener);
        if (Operations.properties.useRadians()) {
            radians.setSelected(true);
        } else {
            degress.setSelected(true);
        }

        ButtonGroup angles = new ButtonGroup();
        angles.add(radians);
        angles.add(degress);

        JPanel rotMode = new JPanel();
        rotMode.setLayout(new BoxLayout(rotMode, BoxLayout.X_AXIS));
        rotMode.add(new JLabel("Angle Measurement:"));
        rotMode.add(radians);
        rotMode.add(degress);

        String[] names = {"to front", "to back", "in front of original", "behind original"};
        JComboBox copyLocs = new JComboBox<>(names);
        copyLocs.setSelectedItem(copyLocs.getItemAt(properties.getCopyLoc()));
        copyLocs.addActionListener(listener);

        JPanel copyLocPanel = new JPanel();
        copyLocPanel.setLayout(new BoxLayout(copyLocPanel, BoxLayout.X_AXIS));
        copyLocPanel.add(new JLabel("Copies go "));
        copyLocPanel.add(copyLocs);

        JPanel dimension = new JPanel();
        dimension.setLayout(new BoxLayout(dimension, BoxLayout.X_AXIS));
        dimension.add(new JLabel("Default Canvas Size: "));

        width = new JTextField("" + Operations.properties.getDefaultSize().width);
        width.addActionListener(listener);
        height = new JTextField("" + Operations.properties.getDefaultSize().height);
        height.addActionListener(listener);
        dimension.add(width);
        dimension.add(new JLabel(", "));
        dimension.add(height);

        JPanel toolsLocation = new JPanel();
        toolsLocation.setLayout(new BoxLayout(toolsLocation, BoxLayout.X_AXIS));
        toolsLocation.add(new JLabel("Tool Shelf Location: "));

        toolTop = new JRadioButton("Top");
        toolTop.addActionListener(listener);
        toolTop.setSelected(Operations.properties.getToolsLocation() == Properties.TOOLS_TOP);
        toolBottom = new JRadioButton("Bottom");
        toolBottom.addActionListener(listener);
        toolBottom.setSelected(!toolTop.isSelected());
        toolsLocation.add(toolTop);
        toolsLocation.add(toolBottom);
        ButtonGroup tools = new ButtonGroup();
        tools.add(toolTop);
        tools.add(toolBottom);

        JPanel detailLocation = new JPanel();
        detailLocation.setLayout(new BoxLayout(detailLocation, BoxLayout.X_AXIS));
        detailLocation.add(new JLabel("Detail Panel Location: "));

        detailRight = new JRadioButton("Right");
        detailRight.addActionListener(listener);
        detailRight.setSelected(Operations.properties.getDetailsLocation() == Properties.DETAILS_RIGHT);
        detailLeft = new JRadioButton("Left");
        detailLeft.addActionListener(listener);
        detailLeft.setSelected(!detailRight.isSelected());
        detailLocation.add(detailRight);
        detailLocation.add(detailLeft);
        ButtonGroup details = new ButtonGroup();
        details.add(detailRight);
        details.add(detailLeft);

        JButton addM = new JButton("Customize Add Menu");
        addM.addActionListener(listener);
        addM.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JPanel options = new JPanel();
        JButton save = new JButton("Save");
        save.addActionListener(listener);
        JButton restore = new JButton("Restore Factory Setting");
        restore.addActionListener(listener);
        options.add(save);
        options.add(restore);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(mode);
        panel.add(colorPallet);
        panel.add(border);
        panel.add(background);
        panel.add(fill);
        panel.add(fillBorder);
        panel.add(rgb);
        panel.add(rotMode);
        panel.add(copyLocPanel);
        panel.add(dimension);
        panel.add(toolsLocation);
        panel.add(detailLocation);
        panel.add(addM);
        panel.add(options);

        listener.setCursors(modes);
        listener.setPallet(pallet);
        listener.setBorder(borderButton);
        listener.setBackground(backgroundButton);
        listener.setFill(fillButton);
        listener.setFilled(fillBox);
        listener.setBorderFill(fillBorder);
        listener.setWidth(width);
        listener.setHeight(height);
        listener.setToolTop(toolTop);
        listener.setToolBottom(toolBottom);
        listener.setDetailLeft(detailLeft);
        listener.setDetailRight(detailRight);
        listener.setAddMenu(addM);
        listener.setSave(save);
        listener.setRestore(restore);
        listener.setCopyLocs(copyLocs);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(listener);
        getContentPane().add(panel);
        pack();
        setVisible(true);
    }
}
