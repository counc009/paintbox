/*----------------------------------------------------
 * PaintBox is a free open source painting program
 * Copyright (C) 2014 PaintBox Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *--------------------------------------------------*/
package acc240.paintbox;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Properties {

    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int INFRONT = 2;
    public static final int BEHIND = 3;

    public static final int TOOLS_TOP = 0;
    public static final int TOOLS_BOTTOM = 1;

    public static final int DETAILS_LEFT = 0;
    public static final int DETAILS_RIGHT = 1;

    private int defaultMode;
    private int copyLocation;
    private int toolsLocation;
    private int detailsLocation;

    private Color defaultBorderColor;
    private Color defaultBackgroundColor;
    private Color defaultFillColor;

    private boolean defaultFilled;
    private boolean fillBorder;
    private boolean errorReported = false;
    private boolean rgb255;
    private boolean radians;

    private Dimension defaultSize;

    private ArrayList<String> shapeNames;
    private ArrayList<String> shapePaths;
    private ArrayList<String> colorPallet;

    private String lastPath;

    public Properties() {
        load();
    }

    public final void load() {
        try {
            File file = new File("properties.prp");
            Scanner s = new Scanner(file);

            ArrayList<String> lines = new ArrayList<>();
            while (s.hasNext()) {
                lines.add(s.nextLine());
            }

            Scanner scan;
            ArrayList<ArrayList<String>> details = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                scan = new Scanner(lines.get(i));
                scan.useDelimiter(";");

                details.add(new ArrayList<>());
                while (scan.hasNext()) {
                    details.get(i).add(scan.next());
                }
            }

            defaultMode = Integer.parseInt(details.get(0).get(0));

            defaultBorderColor = new Color(Integer.parseInt(details.get(1).get(0)), Integer.parseInt(details.get(1).get(1)), Integer.parseInt(details.get(1).get(2)));
            defaultBackgroundColor = new Color(Integer.parseInt(details.get(2).get(0)), Integer.parseInt(details.get(2).get(1)), Integer.parseInt(details.get(2).get(2)));
            defaultFillColor = new Color(Integer.parseInt(details.get(3).get(0)), Integer.parseInt(details.get(3).get(1)), Integer.parseInt(details.get(3).get(2)), Integer.parseInt(details.get(3).get(3)));

            defaultFilled = Boolean.parseBoolean(details.get(4).get(0));
            fillBorder = Boolean.parseBoolean(details.get(5).get(0));

            try {
                lastPath = details.get(6).get(0);
            } catch (Exception e) {
                lastPath = "NONE";
            }
            colorPallet = details.get(7);

            rgb255 = Boolean.parseBoolean(details.get(8).get(0));
            radians = Boolean.parseBoolean(details.get(9).get(0));
            copyLocation = Integer.parseInt(details.get(10).get(0));
            defaultSize = new Dimension(Integer.parseInt(details.get(11).get(0)), Integer.parseInt(details.get(11).get(1)));
            toolsLocation = Integer.parseInt(details.get(12).get(0));
            detailsLocation = Integer.parseInt(details.get(13).get(0));

            shapeNames = new ArrayList<>();
            shapePaths = new ArrayList<>();

            for (int i = 14; i < details.size(); i++) {
                shapeNames.add(details.get(i).get(0));
                shapePaths.add(details.get(i).get(1));
            }
        } catch (FileNotFoundException | NumberFormatException | IndexOutOfBoundsException e) {
            restore();
            save();
        }
    }

    public int getMode() {
        return defaultMode;
    }

    public void setMode(int mode) {
        defaultMode = mode;
    }

    public Color getBorder() {
        return defaultBorderColor;
    }

    public void setBorder(Color color) {
        defaultBorderColor = color;
    }

    public Color getBackground() {
        return defaultBackgroundColor;
    }

    public void setBackground(Color color) {
        defaultBackgroundColor = color;
    }

    public Color getFill() {
        return defaultFillColor;
    }

    public void setFill(Color color) {
        defaultFillColor = color;
    }

    public boolean getFilled() {
        return defaultFilled;
    }

    public void setFilled(boolean filled) {
        defaultFilled = filled;
        fillBorder = false;
    }

    public boolean useBorderFill() {
        return fillBorder;
    }

    public void setBorderFill(boolean fill) {
        if (defaultFilled) {
            fillBorder = fill;
        }
    }

    public String getPath() {
        return lastPath;
    }

    public void setPath(String path) {
        lastPath = path;
    }

    public ArrayList<String> getColorPallet() {
        return colorPallet;
    }

    public void setColorPallet(String pallet, int index) {
        colorPallet.set(index, pallet);
    }

    public void addColorPallet(String pallet) {
        colorPallet.add(pallet);
    }

    public void removeColorPallet(int index) {
        colorPallet.remove(index);
    }

    public boolean useRGB255() {
        return rgb255;
    }

    public void setRGB255(boolean rgb) {
        rgb255 = rgb;
    }

    public boolean useRadians() {
        return radians;
    }

    public void setRadianUse(boolean rad) {
        radians = rad;
    }

    public int getCopyLoc() {
        return copyLocation;
    }

    public void setCopyLoc(int num) {
        if (num >= TOP && num <= BEHIND) {
            copyLocation = num;
        }
    }

    public Dimension getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(Dimension size) {
        defaultSize = size;
    }

    public void setDefaultSizeWidth(int width) {
        defaultSize = new Dimension(width, defaultSize.height);
    }

    public void setDefaultSizeHeight(int height) {
        defaultSize = new Dimension(defaultSize.width, height);
    }

    public ArrayList<String> getShapeNames() {
        return shapeNames;
    }

    public ArrayList<String> getShapePaths() {
        return shapePaths;
    }

    public int getToolsLocation() {
        return toolsLocation;
    }

    public void setToolsLocation(int loc) {
        toolsLocation = loc;
    }

    public int getDetailsLocation() {
        return detailsLocation;
    }

    public void setDetailsLocation(int loc) {
        detailsLocation = loc;
    }

    public void addShape(String name, String path) {
        shapeNames.add(name);
        shapePaths.add(path);
    }

    public void removeShape(String name) {
        int index = shapeNames.indexOf(name);
        shapeNames.remove(index);
        shapePaths.remove(index);
    }

    /**
     * Returns the properties to factory settings
     */
    public void restore() {
        defaultMode = Operations.CURSOR;
        defaultBorderColor = new Color(0, 0, 0);
        defaultBackgroundColor = new Color(255, 255, 255);
        defaultFillColor = new Color(255, 255, 255, 0);
        defaultFilled = false;
        fillBorder = false;
        lastPath = "NONE";
        colorPallet = new ArrayList<>();
        colorPallet.add("defaultPallet.clp");
        rgb255 = true;
        radians = true;
        copyLocation = 0;
        defaultSize = new Dimension(768, 400);
        toolsLocation = 0;
        detailsLocation = 1;
        shapeNames = new ArrayList<>();
        shapePaths = new ArrayList<>();

        try {
            Operations.getDetailCollapse().setLocation(detailsLocation);
            Operations.getCanvasPanel().update();
        } catch (NullPointerException ignore) {

        }
    }

    public void save() {
        try {
            File file = new File("properties.prp");
            BufferedWriter ot = new BufferedWriter(new FileWriter(file));
            try (PrintWriter output = new PrintWriter(ot)) {
                output.println(defaultMode);
                output.println(defaultBorderColor.getRed() + ";" + defaultBorderColor.getGreen() + ";" + defaultBorderColor.getBlue());
                output.println(defaultBackgroundColor.getRed() + ";" + defaultBackgroundColor.getGreen() + ";" + defaultBackgroundColor.getBlue());
                output.println(defaultFillColor.getRed() + ";" + defaultFillColor.getGreen() + ";" + defaultFillColor.getBlue() + ";" + defaultFillColor.getAlpha());
                output.println(defaultFilled);
                output.println(fillBorder);
                output.println(lastPath);

                for (int i = 0; i < colorPallet.size(); i++) {
                    output.print(colorPallet.get(i));
                    if (i + 1 < colorPallet.size()) {
                        output.print(";");
                    }
                }
                output.println();

                output.println(rgb255);
                output.println(radians);
                output.println(copyLocation);
                output.println((int) defaultSize.getWidth() + ";" + (int) defaultSize.getHeight());
                output.println(toolsLocation);
                output.println(detailsLocation);

                for (int i = 0; i < shapeNames.size(); i++) {
                    output.println(shapeNames.get(i) + ";" + shapePaths.get(i));
                }

                try {
                    Operations.getDetailCollapse().setLocation(detailsLocation);
                    Operations.getCanvasPanel().update();
                } catch (NullPointerException ignore) {

                }
            }
        } catch (IOException e) {
            if (!errorReported) {
                JOptionPane.showMessageDialog(null, "An error occurred while attempting to save your properties. The properties may not have saved. You may wish to try again, but run the program as an administrator.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            errorReported = true;
        }
    }
}
