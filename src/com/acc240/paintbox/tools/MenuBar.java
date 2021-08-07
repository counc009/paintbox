package com.acc240.paintbox.tools;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.FillableShape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuBar extends JPanel {

    public static final int NEW = 0;
    public static final int OPEN = 1;
    public static final int SAVE = 2;
    public static final int SAVE_AS = 3;
    public static final int EXPORT = 4;
    public static final int PRINT = 5;
    public static final int PROPERTIES = 6;
    public static final int SCALE = 7;
    public static final int BORDER = 8;
    public static final int FILL = 9;
    public static final int MOVE = 10;
    public static final int BACKGROUND = 11;
    public static final int TEXT = 12;
    public static final int PICTURE = 13;
    public static final int SQUARE = 14;
    public static final int CIRCLE = 15;
    public static final int TRIANGLE = 16;
    public static final int CUSTOM = 17;
    public static final int IMPORT = 18;

    private final JMenuItem createNew, save, saveAs, open, png, jpg, gif, print, properties, scale;
    private final JMenuItem border, move, background;
    private final JCheckBoxMenuItem filled;
    private final JMenuItem text, pic, circle, triangle, square, imp;
    private final ArrayList<JMenuItem> customObjects;
    private final JMenu export;

    public MenuBar() {
        JMenu file = new JMenu("File");
        MenuBarListener listener = new MenuBarListener(this);

        createNew = new JMenuItem("New");
        createNew.addActionListener(listener);

        open = new JMenuItem("Open");
        open.addActionListener(listener);

        save = new JMenuItem("Save");
        save.addActionListener(listener);

        saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(listener);

        export = new JMenu("Export to...");

        png = new JMenuItem("PNG");
        png.addActionListener(listener);
        export.add(png);

        jpg = new JMenuItem("JPG");
        jpg.addActionListener(listener);
        export.add(jpg);

        gif = new JMenuItem("GIF");
        gif.addActionListener(listener);
        export.add(gif);

        print = new JMenuItem("Print");
        print.addActionListener(listener);

        properties = new JMenuItem("Properties");
        properties.addActionListener(listener);

        scale = new JMenuItem("Scale Drawing");
        scale.addActionListener(listener);

        file.add(createNew);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(export);
        file.add(print);
        file.add(properties);
        file.add(scale);

        JMenu edit = new JMenu("Edit");

        border = new JMenuItem("Set Border Color");
        border.addActionListener(listener);

        filled = new JCheckBoxMenuItem("Filled");
        filled.setEnabled(Operations.getMode() == Operations.SELECT && Operations.getSelected() != null && Operations.getSelected() instanceof FillableShape);
        if (filled.isEnabled()) {
            filled.setState(((FillableShape) Operations.getSelected()).getFilled());
        }
        filled.addActionListener(listener);

        move = new JMenuItem("Move");
        move.addActionListener(listener);
        move.setEnabled(false);

        background = new JMenuItem("Set Background Color");
        background.addActionListener(listener);

        edit.add(border);
        edit.add(filled);
        edit.add(move);
        edit.add(background);

        JMenu add = new JMenu("Add");

        text = new JMenuItem("Add Text");
        text.addActionListener(listener);
        text.setEnabled(true);
        add.add(text);

        pic = new JMenuItem("Add Image");
        pic.addActionListener(listener);
        pic.setEnabled(true);
        add.add(pic);

        circle = new JMenuItem("Add Circle");
        circle.addActionListener(listener);
        circle.setEnabled(true);
        add.add(circle);

        square = new JMenuItem("Add Square");
        square.addActionListener(listener);
        square.setEnabled(true);
        add.add(square);

        triangle = new JMenuItem("Add Equilateral Triangle");
        triangle.addActionListener(listener);
        triangle.setEnabled(true);
        add.add(triangle);

        customObjects = new ArrayList<>();
        ArrayList<String> names = Operations.properties.getShapeNames();
        for (String name : names) {
            JMenuItem temp = new JMenuItem("Add " + name);
            temp.addActionListener(listener);
            temp.setEnabled(true);
            customObjects.add(temp);
            add.add(temp);
        }

        imp = new JMenuItem("Import Shape");
        imp.addActionListener(listener);
        imp.setEnabled(true);
        add.add(imp);

        setLayout(new FlowLayout(FlowLayout.LEADING));
        setBackground(Color.gray);

        JMenuBar bar = new JMenuBar();

        bar.add(file);
        bar.add(edit);
        bar.add(add);

        add(bar);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    }

    public void updateSelected() {
        if (Operations.getMode() == Operations.SELECT && Operations.getSelected() != null) {
            move.setEnabled(true);
            if (Operations.getSelected() instanceof FillableShape) {
                filled.setEnabled(true);
                filled.setState(((FillableShape) Operations.getSelected()).getFilled());
            } else {
                filled.setEnabled(false);
                filled.setState(false);
            }
        } else {
            move.setEnabled(false);
            filled.setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean on) {
        if (on) {
            save.setEnabled(true);
            saveAs.setEnabled(true);
            export.setEnabled(true);
            png.setEnabled(true);
            jpg.setEnabled(true);
            print.setEnabled(true);
            move.setEnabled(true);
            properties.setEnabled(true);
            scale.setEnabled(true);
            border.setEnabled(true);
            filled.setEnabled(true);
            background.setEnabled(true);
            text.setEnabled(true);
            pic.setEnabled(true);
            circle.setEnabled(true);
            square.setEnabled(true);
            triangle.setEnabled(true);
            imp.setEnabled(true);

            for (JMenuItem field : customObjects) {
                field.setEnabled(true);
            }

            updateSelected();
        } else {
            save.setEnabled(false);
            saveAs.setEnabled(false);
            export.setEnabled(false);
            png.setEnabled(true);
            jpg.setEnabled(true);
            print.setEnabled(false);
            move.setEnabled(false);
            properties.setEnabled(false);
            scale.setEnabled(false);
            border.setEnabled(false);
            filled.setEnabled(false);
            background.setEnabled(false);
            text.setEnabled(false);
            pic.setEnabled(false);
            circle.setEnabled(false);
            square.setEnabled(false);
            triangle.setEnabled(false);
            imp.setEnabled(false);

            for (JMenuItem field : customObjects) {
                field.setEnabled(false);
            }
        }
    }

    public int getComponentNumber(JComponent component) {
        if (component == createNew) {
            return NEW;
        }
        if (component == open) {
            return OPEN;
        }
        if (component == save) {
            return SAVE;
        }
        if (component == saveAs) {
            return SAVE_AS;
        }
        if (component == png || component == jpg || component == gif) {
            return EXPORT;
        }
        if (component == print) {
            return PRINT;
        }
        if (component == properties) {
            return PROPERTIES;
        }
        if (component == scale) {
            return SCALE;
        }
        if (component == border) {
            return BORDER;
        }
        if (component == filled) {
            return FILL;
        }
        if (component == move) {
            return MOVE;
        }
        if (component == background) {
            return BACKGROUND;
        }
        if (component == text) {
            return TEXT;
        }
        if (component == pic) {
            return PICTURE;
        }
        if (component == circle) {
            return CIRCLE;
        }
        if (component == square) {
            return SQUARE;
        }
        if (component == triangle) {
            return TRIANGLE;
        }
        if (component == imp) {
            return IMPORT;
        }
        if (customObjects.indexOf(component) != -1) {
            return CUSTOM;
        }
        return -1;
    }

    public ArrayList<JMenuItem> getCustoms() {
        return customObjects;
    }
}
