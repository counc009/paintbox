package com.acc240.paintbox.details;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.Text;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FontPanel extends JPanel {

    public FontPanel() {
        FontListener listener = new FontListener();

        String[] fontNames = {"Times New Roman", "Arial", "Calibri", "Agency FB", "Berlin Sans FB", "Bodoni MT"};
        Integer[] sizes = {4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48};

        JPanel script = new JPanel();
        JComboBox<String> list = new JComboBox<>(fontNames);
        list.addActionListener(listener);
        list.setSelectedItem(((Text) Operations.getSelected()).getFont().getFamily());

        script.add(new JLabel("Font:  "));
        script.add(list);

        JPanel size = new JPanel();
        JComboBox sizeList = new JComboBox<>(sizes);
        sizeList.addActionListener(listener);
        sizeList.setSelectedItem(((Text) Operations.getSelected()).getFont().getSize());

        size.add(new JLabel("Size:  "));
        size.add(sizeList);

        JPanel style = new JPanel();

        JCheckBox bold = new JCheckBox("Bold", ((Text) Operations.getSelected()).getFont().isBold());
        bold.addActionListener(listener);
        JCheckBox italic = new JCheckBox("Italic", ((Text) Operations.getSelected()).getFont().isItalic());
        italic.addActionListener(listener);

        listener.setBold(bold);
        listener.setItalic(italic);

        style.add(new JLabel("Style:  "));
        style.add(bold);
        style.add(italic);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        add(script);
        add(size);
        add(style);
    }
}
