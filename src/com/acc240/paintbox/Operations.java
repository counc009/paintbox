/*----------------------------------------------------
 * PaintBox is a free open source painting program
 * Copyright (C) 2021 Aaron Councilman
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
package com.acc240.paintbox;

import com.acc240.paintbox.canvas.Canvas;
import com.acc240.paintbox.tools.ToolShelf;
import com.acc240.paintbox.canvas.CanvasPanel;
import com.acc240.paintbox.details.DetailPanel;
import com.acc240.paintbox.geom.FillableShape;
import com.acc240.paintbox.geom.Line;
import com.acc240.paintbox.geom.Oval;
import com.acc240.paintbox.geom.Picture;
import com.acc240.paintbox.geom.Polygon;
import com.acc240.paintbox.geom.Polyline;
import com.acc240.paintbox.geom.Rectangle;
import com.acc240.paintbox.geom.Shape;
import com.acc240.paintbox.geom.Text;
import com.acc240.paintbox.tools.MenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Operations {

    /**
     * Present Version
     */
    private static final double version = 1.11;
    /**
     * Oldest version that can still be opened
     */
    private static final double oldest_open = 1.09;

    /**
     * Cursor mode, nothing happens
     */
    public static final int CURSOR = 0;
    /**
     * Select mode, allows user to select figures and change their properties
     */
    public static final int SELECT = 1;
    /**
     * Rectangle draw mode, in which the user can draw rectangles
     */
    public static final int RECTANGLE = 2;
    /**
     * Oval draw mode
     */
    public static final int OVAL = 3;
    /**
     * Line draw mode
     */
    public static final int LINE = 4;
    /**
     * Polyline draw mode
     */
    public static final int POLYLINE = 5;
    /**
     * Polygon draw mode
     */
    public static final int POLYGON = 6;
    /**
     * Text draw mode
     */
    public static final int TEXT = 7;
    /**
     * Picture draw mode
     */
    public static final int PICTURE = 8;
    public static final int SQUARE = 9;
    public static final int CIRCLE = 10;
    public static final int TRIANGLE = 11;
    /**
     * Imported shape draw mode
     */
    public static final int IMPORT = 12;

    /**
     * Means that the the next click on the canvas will result in the selected
     * item starting to move
     */
    public static final int CAN_MOVE = 1;
    /**
     * Means that the selected object is presently moving with the mouse
     */
    public static final int MOVING = 0;
    /**
     * Means that until the user hits the move button nothing can move
     */
    public static final int NO_MOVE = -1;

    public static final int NO_ROTATE = -1;
    public static final int CAN_ROTATE = 0;
    public static final int ROTATING = 1;

    public static final Properties properties = new Properties();

    private static int mouseMode = properties.getMode();
    private static int moveMode = NO_MOVE;
    private static int rotMode = NO_ROTATE;
    private static int lastMode = CURSOR;

    private static ArrayList<Shape> shapes;
    private static ArrayList<Color> recentColors;

    private static Color borderColor;
    private static float strokeWidth;
    private static Color fillColor;
    private static boolean filled;

    private static Canvas canvas = null;
    private static ToolShelf tools = null;
    private static MenuBar menu = null;
    private static DetailPanel detailPanel = null;
    private static CollapsePanel detailCollapse = null;
    private static CanvasPanel canvasPanel = null;

    private static Shape selected = null;

    private static boolean changed = false;
    private static boolean panelOpened = false;

    private static String fileName = "";
    private static String filePath = "";
    private static String tempStr = "";

    private static JFrame frame = null;

    /**
     * Default set up method, with a new canvas and in cursor mode
     */
    public static void setup() {
        setup(properties.getMode());
    }

    /**
     * Overridden set up method to set up to a specific mode of operation and a
     * new canvas
     *
     * @param mode the cursor mode to set the program to
     */
    public static void setup(int mode) {
        setup(mode, new ArrayList<>());
    }

    /**
     * Overridden set up method to set up to a specific mode and a specific
     * canvas
     *
     * @param mode   the cursor mode to set the program to
     * @param canvas a list of the shapes presently on the canvas
     */
    public static void setup(int mode, ArrayList<Shape> canvas) {
        shapes = canvas;
        mouseMode = mode;
        borderColor = properties.getBorder();
        fillColor = properties.getFill();
        filled = properties.getFilled();
        recentColors = new ArrayList<>();
    }

    /**
     * Opens the splash screen and sets it so that it cannot be edited
     */
    public static void splash() {
        changed = false;
        selected = null;
        fileName = "Splash.drw";
        open(new File(fileName), true);
        canvas.repaint();
        PaintBox.setName(" ");
        tools.setEnabled(false);
        detailPanel.setEnabled(false);
        menu.setEnabled(false);

        filePath = "";
    }

    /**
     * Clears the entire program, resets mode, canvas, etc.
     */
    public static void clear() {
        clear(false);
    }

    /**
     * Clears the entire program, resets mode, canvas, etc.
     *
     * @param saveOffered whether or not a save has been offered to the user
     */
    public static void clear(boolean saveOffered) {
        boolean clear = true;
        if (!saveOffered) {
            clear = closeSave();
        }

        if (clear) {
            mouseMode = properties.getMode();
            tools.getMode().doClick();
            moveMode = NO_MOVE;
            rotMode = NO_ROTATE;
            shapes = new ArrayList<>();
            borderColor = properties.getBorder();
            filled = false;
            fillColor = properties.getFill();
            selected = null;
            changed = false;
            fileName = "";
            canvas.setBackground(properties.getBackground());
            canvas.repaint();
            canvas.setPreferredSize(properties.getDefaultSize());
            frame.pack();
            PaintBox.setName(fileName);
            tools.getMode().doClick();
            tools.setEnabled(true);
            detailPanel.setEnabled(true);
            menu.setEnabled(true);
        }

        tools.requestFocus();
    }

    /**
     * Clears everything and then reloads the present file, border color, and
     * mode
     */
    public static void reload() {
        boolean clear = closeSave();
        if (clear) {
            int mode = mouseMode;
            String file = filePath;
            Color color = borderColor;

            clear(true);
            update();
            if (file.length() > 0) {
                open(new File(file));
                borderColor = color;
                tools.getCursorModes().get(mode).doClick();
            }
            update();
        }
    }

    public static void scale(double scaleX, double scaleY) {
        for (Shape shape : shapes) {
            shape.scale(scaleX, scaleY);
        }

        canvas.setPreferredSize(new Dimension((int) (canvas.getSize().getWidth() * scaleX), (int) (canvas.getSize().getHeight() * scaleY)));
        frame.setSize(new Dimension((int) (canvas.getSize().getWidth() * scaleX) + detailCollapse.getTotalWidth(), (int) (canvas.getSize().getHeight() * scaleY) + 132));

        update();
    }

    /**
     * If changes have been made to a drawing asks if the user wants to save
     *
     * @return whether or not to continue closing the program
     */
    public static boolean closeSave() {
        boolean clear = true;
        if (isChanged()) {
            int reply = JOptionPane.showConfirmDialog(null, "Unsaved changes, do you wish to save?");

            if (reply == JOptionPane.YES_OPTION) {
                if (getFileName().equals("")) {
                    File file = Resources.getFile("Save", "draw files (*.drw)", "drw");
                    if (file != null) {
                        save(file);
                    }
                } else {
                    Operations.save();
                }
            } else if (reply == JOptionPane.CANCEL_OPTION) {
                clear = false;
            }
        }
        return clear;
    }

    /**
     * Updates the program's components: the canvas, the toolshelf, and the
     * menus
     */
    public static void update() {
        canvas.repaint();
        tools.updateSelected();
        menu.updateSelected();

        if (!panelOpened) {
            tools.requestFocus();
        }

        if (mouseMode != SELECT) {
            moveMode = NO_MOVE;
            rotMode = NO_ROTATE;
        }
        if (selected == null) {
            moveMode = NO_MOVE;
            rotMode = NO_ROTATE;
        }
    }

    /**
     * Sets the JFrame for the program
     *
     * @param fram the JFrame the program is in
     */
    public static void setFrame(JFrame fram) {
        frame = fram;
    }

    /**
     * Returns the JFrame
     *
     * @return the program's JFrame
     */
    public static JFrame getFrame() {
        return frame;
    }

    /**
     * Sets the canvas for the program
     *
     * @param can the program's canvas
     */
    public static void setCanvas(Canvas can) {
        canvas = can;
    }

    /**
     * Returns the canvas
     *
     * @return the program's canvas
     */
    public static Canvas getCanvas() {
        return canvas;
    }

    /**
     * Sets the canvasPanel for the program
     *
     * @param panel the program's canvasPanel
     */
    public static void setCanvasPanel(CanvasPanel panel) {
        canvasPanel = panel;
    }

    /**
     * Returns the canvas
     *
     * @return the program's canvas
     */
    public static CanvasPanel getCanvasPanel() {
        return canvasPanel;
    }

    /**
     * Sets the Toolshelf for the program
     *
     * @param tool the program's toolshelf
     */
    public static void setTools(ToolShelf tool) {
        tools = tool;
    }

    /**
     * Returns the toolshelf
     *
     * @return the program's toolshelf
     */
    public static ToolShelf getTools() {
        return tools;
    }

    /**
     * Sets the menus for the program
     *
     * @param menu the program's menus
     */
    public static void setMenu(MenuBar menu) {
        Operations.menu = menu;
    }

    /**
     * Returns the menus
     *
     * @return the program's menus
     */
    public static MenuBar getMenu() {
        return menu;
    }

    /**
     * Sets the detail panel for the program
     *
     * @param detailPanel the program's detailPanel
     */
    public static void setDetailPanel(DetailPanel detailPanel) {
        Operations.detailPanel = detailPanel;
    }

    /**
     * Returns the detail panel
     *
     * @return the program's detail panel
     */
    public static DetailPanel getDetailPanel() {
        return detailPanel;
    }

    /**
     * Sets the collapse panel controlling the detail panel
     *
     * @param collapsePanel the collapse panel controlling the detail panel
     */
    public static void setDetailCollapse(CollapsePanel collapsePanel) {
        Operations.detailCollapse = collapsePanel;
    }

    /**
     * Returns the detail collapse panel
     *
     * @return the collapse panel controlling the detail panel
     */
    public static CollapsePanel getDetailCollapse() {
        return detailCollapse;
    }

    /**
     * Returns the temporary string
     *
     * @return the program's current temporary string
     */
    public static String getTempText() {
        return tempStr;
    }

    public static ArrayList<Color> getRecentColors() {
        return recentColors;
    }

    public static void addRecentColor(Color color) {
        if (recentColors.size() < 10 && recentColors.indexOf(color) == -1) {
            recentColors.add(0, color);
        } else if (recentColors.indexOf(color) == -1) {
            recentColors.add(0, color);
            recentColors.remove(10);
        }
    }

    public static void panelOpened() {
        panelOpened = true;
    }

    public static void panelClosed() {
        panelOpened = false;
        tools.requestFocus();
    }

    public static boolean isPanelOpen() {
        return panelOpened;
    }

    /**
     * Returns the operation mode
     *
     * @return the current cursor mode
     */
    public static int getMode() {
        return mouseMode;
    }

    /**
     * Sets the operation mode if it is in the valid range, otherwise it throws
     * a RuntimeException and closes the program
     *
     * @param mode the cursor mode to set the program to
     * @throws RuntimeException
     */
    public static void setMode(int mode) throws RuntimeException {
        setMode(mode, false);
    }

    private static void setMode(int mode, boolean inside) {
        if (mouseMode == SELECT && mode != SELECT) {
            detailPanel.setSelected(null);
        }
        if (mode >= CURSOR && mode <= IMPORT) {
            if (mouseMode != TEXT && mouseMode != PICTURE && mouseMode != IMPORT) {
                lastMode = mouseMode;
            } else if (mode != TEXT && mode != PICTURE && mode != IMPORT) {
                lastMode = mode;
            } else {
                lastMode = CURSOR;
            }
            mouseMode = mode;
        } else {
            System.exit(0);
            throw (new RuntimeException("Invalid mouse mode. Must close"));
        }
        if (mode == TEXT) {
            if (!inside) {
                String input = JOptionPane.showInputDialog("Please enter the text to be created.");
                tempStr = input;
            } else {
                System.exit(0);
                throw (new RuntimeException("Unrecoverable situation, must close."));
            }
        }
        if (mode == PICTURE) {
            File input = Resources.getFile("Select Image", "Image Files (*.png, *.jpg, *.gif)", "png", "jpg", "gif");
            if (input != null) {
                tempStr = input.getAbsolutePath();
            } else {
                tempStr = "error.gif";
            }
        }
        if (mode == IMPORT && !inside) {
            File file = Resources.getFile("Import", "shape files (*.shp)", "shp");
            if (file != null) {
                tempStr = file.getPath();
            }
        }
    }

    public static void setCustom(int objNum) {
        setMode(IMPORT, true);
        tempStr = properties.getShapePaths().get(objNum);
    }

    /**
     * Returns the last mode that it had
     *
     * @return the cursor mode the program was previously in
     */
    public static int getLastMode() {
        return lastMode;
    }

    /**
     * Returns the move mode
     *
     * @return the current move mode of the program
     */
    public static int getMove() {
        return moveMode;
    }

    /**
     * Returns the rotation mode
     *
     * @return the current rotation mode of the program
     */
    public static int getRotation() {
        return rotMode;
    }

    /**
     * Sets the move mode if it is the valid range and the context is
     * appropriate, otherwise it throws a RuntimeException and closes the
     * program
     *
     * @param mode the move mode to which the program will be set
     */
    public static void setMove(int mode) {
        if (mode >= NO_MOVE && mode <= CAN_MOVE && mouseMode == SELECT && selected != null) {
            moveMode = mode;
        } else {
            System.exit(0);
            throw (new RuntimeException("Invalid move mode. Must close"));
        }
    }

    /**
     * Returns the shapes on the canvas in an ArrayListn
     *
     * @return a list of the shapes on the canvas
     */
    public static ArrayList<Shape> getShapes() {
        return shapes;
    }

    /**
     * Adds a new shape to the canvas
     *
     * @param shape the shape to be added to the canvas
     */
    public static void addShape(Shape shape) {
        shapes.add(shape);
        changed = true;
    }

    /**
     * Returns the present border color
     *
     * @return the current border color
     */
    public static Color getBorder() {
        return borderColor;
    }

    /**
     * Sets the present border color
     *
     * @param color the color to change the border color to
     */
    public static void setBorder(Color color) {
        borderColor = color;
        if (selected != null && mouseMode == SELECT) {
            selected.setColor(borderColor);
        }
        detailPanel.getBorderColor().setBackground(borderColor);
    }

    /**
     * Returns the present stroke width
     *
     * @return the current stroke width
     */
    public static float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Sets the present stroke width
     *
     * @param width the width to change the stroke width to
     */
    public static void setStrokeWidth(float width) {
        strokeWidth = width;
        if (selected != null && mouseMode == SELECT) {
            selected.setStrokeWidth(strokeWidth);
        }
        detailPanel.getStrokeWidth().setText("" + width);
    }

    /**
     * Returns the present fill color
     *
     * @return the current fill color
     */
    public static Color getFill() {
        return fillColor;
    }

    /**
     * Sets the present fill color
     *
     * @param color the color to change the fill color to
     */
    public static void setFill(Color color) {
        fillColor = color;
        if (selected != null && mouseMode == SELECT && selected instanceof FillableShape && ((FillableShape) selected).getFilled()) {
            ((FillableShape) selected).setFilling(fillColor);
        }
    }

    /**
     * Returns the present fill state
     *
     * @return the present fill state
     */
    public static boolean getFilled() {
        return filled;
    }

    /**
     * Sets the present fill state
     *
     * @param bool the state to change the present fill state to
     */
    public static void setFilled(boolean bool) {
        filled = bool;
        detailPanel.getFillPanel().setFilled(filled);
        if (selected != null && mouseMode == SELECT && selected instanceof FillableShape) {
            if (filled) {
                ((FillableShape) selected).setFilled();
                if (fillColor.getAlpha() == 0) {
                    ((FillableShape) selected).setFilling(selected.getColor());
                    fillColor = selected.getColor();
                    detailPanel.getFillPanel().setColor(fillColor);
                } else {
                    ((FillableShape) selected).setFilling(fillColor);
                }
            } else {
                ((FillableShape) selected).setUnfilled();
            }
        }
    }

    /**
     * Returns the presently selected shape
     *
     * @return the selected shape
     */
    public static Shape getSelected() {
        return selected;
    }

    /**
     * Sets the selected shape
     *
     * @param set the shape to set selected
     */
    public static void setSelected(Shape set) {
        selected = set;
        detailPanel.setSelected(selected);
        filled = selected instanceof FillableShape && ((FillableShape) selected).getFilled();
        if (filled) {
            fillColor = ((FillableShape) selected).getFilling();
        }
    }

    /**
     * Returns the index(layer) of the specified shape
     *
     * @param shape the shape to get the layer number of
     * @return the layer number of the shape
     */
    public static int getIndex(Shape shape) {
        return shapes.indexOf(shape);
    }

    /**
     * Moves the specified shape to the bottom layer of the painting
     *
     * @param shape the shape to move to the bottom layer
     */
    public static void toBack(Shape shape) {
        shapes.remove(shape);
        shapes.add(0, shape);
    }

    /**
     * Moves the specified shape to the top layer of the painting
     *
     * @param shape the shape to move to the top layer
     */
    public static void toFront(Shape shape) {
        shapes.remove(shape);
        shapes.add(shape);
    }

    /**
     * Moves the specified shape back one layer
     *
     * @param shape the shape to move back one layer
     */
    public static void pushBack(Shape shape) {
        int index = getIndex(shape);
        if (index - 1 >= 0) {
            shapes.remove(shape);
            shapes.add(index - 1, shape);
        }
    }

    /**
     * Moves the specified shape forward one layer
     *
     * @param shape the shape to move forward one layer
     */
    public static void pullForward(Shape shape) {
        int index = getIndex(shape);
        if (index + 1 <= shapes.size()) {
            shapes.remove(shape);
            shapes.add(index + 1, shape);
        }
    }

    /**
     * Moves the specified shape to the specified layer
     *
     * @param shape the shape to be moved
     * @param index the index to move the shape to
     */
    public static void setIndex(Shape shape, int index) {
        if (index >= 0 && index <= shapes.size()) {
            shapes.remove(shape);
            shapes.add(index, shape);
        }
    }

    /**
     * Returns the present file's file name
     *
     * @return the file location of the present drawing
     */
    public static String getFileName() {
        return fileName;
    }

    /**
     * Returns the present file's name without the extension
     *
     * @return the name of the present drawing
     */
    public static String getName() {
        return stripExtension(fileName);
    }

    /**
     * Enables the moving of items
     */
    public static void enableMove() {
        moveMode = CAN_MOVE;
    }

    /**
     * Enables the rotation of items
     */
    public static void enableRotate() {
        rotMode = CAN_ROTATE;
    }

    /**
     * Sets the move mode to moving
     */
    public static void setMoving() {
        moveMode = MOVING;
    }

    /**
     * Sets the rotation mode to rotating
     */
    public static void setRotating() {
        rotMode = ROTATING;
    }

    /**
     * Disables the moving of items
     */
    public static void setNoMove() {
        moveMode = NO_MOVE;
    }

    /**
     * Disables the rotation of items
     */
    public static void setNoRot() {
        rotMode = NO_ROTATE;
    }

    /**
     * Sets the project as changed to keep you from closing and loosing work
     */
    public static void setChanged() {
        changed = true;
        PaintBox.setName(getName());
    }

    /**
     * Returns if the project has been changed
     *
     * @return whether the drawing has been changed since the last save
     */
    public static boolean isChanged() {
        return changed;
    }

    /**
     * Deletes the selected shape
     */
    public static void deleteSelected() {
        shapes.remove(selected);
        selected = null;
        tools.updateSelected();
        setChanged();
        canvas.repaint();
    }

    /**
     * Deletes the shape passed in
     *
     * @param shape the shape to delete
     */
    public static void delete(Shape shape) {
        shapes.remove(shape);
        setChanged();
        canvas.repaint();
    }

    /**
     * Saves the present file to its present location
     */
    public static void save() {
        if (changed) {
            if (getFileName().length() > 0) {
                File file = new File(filePath);
                if (file != null) {
                    Operations.save(file);
                }
            } else {
                File file = Resources.getFile("Save", "draw files (*.drw)", "drw");
                if (file != null) {
                    Operations.save(file);
                }
            }
        } else {
            File file = Resources.getFile("Save", "draw files (*.drw)", "drw");
            if (file != null) {
                Operations.save(file);
            }
        }
    }

    /**
     * Saves the present file to the file passed in
     *
     * @param file the file to save the drawing to
     */
    public static void save(File file) {
        properties.setPath(file.getPath());
        properties.save();

        try {
            BufferedWriter ot = new BufferedWriter(new FileWriter(file));
            try (PrintWriter output = new PrintWriter(ot)) {
                output.println(version);
                output.println(canvas.getSize().getWidth() + ":" + canvas.getSize().getHeight());

                int r = canvas.getBackground().getRed();
                int g = canvas.getBackground().getGreen();
                int b = canvas.getBackground().getBlue();
                if (r >= 128) {
                    output.print("X" + (char) (r - 128) + ":");
                } else {
                    output.print((char) r + ":");
                }
                if (g >= 128) {
                    output.print("X" + (char) (g - 128) + ":");
                } else {
                    output.print((char) g + ":");
                }
                if (b >= 128) {
                    output.println("X" + (char) (b - 128));
                } else {
                    output.println((char) b);
                }

                for (Shape shape : shapes) {
                    output.println(shape.toText());
                }

                fileName = file.getName();
                filePath = file.getPath();
                changed = false;
                PaintBox.setName(getName());
            }
        } catch (IOException e) {
        }
    }

    /**
     * Exports a shape
     *
     * @param shape the shape to export
     */
    public static void exportShape(Shape shape) {
        File file = Resources.getFile("Save", "shape files (*.shp)", "shp");
        if (file != null) {
            try {
                properties.setPath(file.getPath());
                properties.save();

                BufferedWriter ot = new BufferedWriter(new FileWriter(file));
                try (PrintWriter output = new PrintWriter(ot)) {
                    output.println(shape.toGeneral());
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Imports a shape
     *
     * @param file  the file from which to import the shape from
     * @param point the point from which to base the shape
     * @return a Shape object created from the imported file
     */
    public static Shape importShape(File file, Point point) {
        Shape result = null;

        try {
            Scanner s = new Scanner(file);
            String line = s.nextLine();

            Scanner scan = new Scanner(line);
            scan.useDelimiter(":");

            String start = scan.next();
            String[] data = new String[0];

            while (scan.hasNext()) {
                String[] temp = new String[data.length + 1];
                System.arraycopy(data, 0, temp, 0, data.length);

                temp[temp.length - 1] = scan.next();
                data = temp;
            }

            if (start.equals("l")) {
                result = new Line(0, 0, borderColor);
                result.fromGeneral(data, point);
            }
            if (start.equals("o")) {
                result = new Oval(0, 0, borderColor);
                result.fromGeneral(data, point);
            }
            if (start.equals("g")) {
                result = new Polygon(0, 0, borderColor);
                result.fromGeneral(data, point);
            }
            if (start.equals("p")) {
                result = new Polyline(0, 0, borderColor);
                result.fromGeneral(data, point);
            }
            if (start.equals("r")) {
                result = new Rectangle(0, 0, borderColor);
                result.fromGeneral(data, point);
            }
        } catch (FileNotFoundException e) {
        }

        return result;
    }

    /**
     * Opens a project from file
     *
     * @param file the file to open a drawing from
     */
    public static void open(File file) {
        open(file, false);
    }

    /**
     * Opens a project from file
     *
     * @param splash: whether this is opening the splash screen
     */
    private static void open(File file, boolean splash) {
        shapes = new ArrayList<>();
        mouseMode = properties.getMode();
        tools.getMode().doClick();
        selected = null;
        tools.setEnabled(true);
        detailPanel.setEnabled(true);
        menu.setEnabled(true);

        if (!splash) {
            properties.setPath(file.getPath());
            properties.save();
        }

        try {
            Scanner scan = new Scanner(file);

            boolean old = false;
            if (scan.hasNext()) {
                String str = scan.nextLine();
                try {
                    double v = Double.parseDouble(str);
                    if (Math.abs(v - version) < 0.001) //It can be opened
                    {
                    } else {
                        old = true;
                        open_old(file, splash);
                    }
                } catch (NumberFormatException e) {
                    old = true;
                    open_old(file, splash);
                }
            }

            if (!old) {
                String size = scan.nextLine();
                int width = (int) Float.parseFloat(size.substring(0, size.indexOf(":")));
                int height = (int) Float.parseFloat(size.substring(size.indexOf(":") + 1));
                canvas.setPreferredSize(new Dimension(width, height));
                frame.pack();

                boolean back_set = false;
                while (scan.hasNext()) {
                    String line = scan.nextLine();
                    Scanner scanSec = new Scanner(line);
                    scanSec.useDelimiter(":");

                    if (scanSec.hasNext()) {
                        String start = scanSec.next();
                        String[] data = new String[0];

                        while (scanSec.hasNext()) {
                            String[] n = new String[data.length + 1];
                            System.arraycopy(data, 0, n, 0, data.length);

                            String part = scanSec.next();

                            if (part.length() > 0) {
                                n[data.length] = part;
                            }

                            data = n;
                        }

                        if (!back_set) {
                            int r = (int) start.charAt(0);
                            int g = (int) data[0].charAt(0);
                            int b = (int) data[1].charAt(0);

                            if (start.length() == 2 && start.charAt(0) == 'X') {
                                r = 128 + start.charAt(1);
                            }
                            if (data[0].length() == 2 && data[0].charAt(0) == 'X') {
                                g = 128 + data[0].charAt(1);
                            }
                            if (data[1].length() == 2 && data[1].charAt(0) == 'X') {
                                b = 128 + data[1].charAt(1);
                            }

                            canvas.setBackground(new Color(r, g, b));
                            back_set = true;
                        }

                        if (start.equals("l")) {
                            Shape s = new Line(0, 0, Color.white);
                            s.fromText(data);
                            shapes.add(s);
                        }
                        if (start.equals("o")) {
                            Shape s = new Oval(0, 0, Color.white);
                            s.fromText(data);
                            shapes.add(s);
                        }
                        if (start.equals("g")) {
                            Shape s = new Polygon(0, 0, Color.white);
                            s.fromText(data);
                            shapes.add(s);
                        }
                        if (start.equals("p")) {
                            Shape s = new Polyline(0, 0, Color.white);
                            s.fromText(data);
                            shapes.add(s);
                        }
                        if (start.equals("r")) {
                            Shape s = new Rectangle(0, 0, Color.white);
                            s.fromText(data);
                            shapes.add(s);
                        }
                        if (start.equals("t")) {
                            Shape s = new Text("", 0, 0, Color.white);
                            s.fromText(data);
                            shapes.add(s);
                        }
                        if (start.equals("i")) {
                            Shape s = new Picture(0, 0, "");
                            s.fromText(data);
                            shapes.add(s);
                        }
                    }
                }

                fileName = file.getName();
                filePath = file.getPath();
                changed = false;
                PaintBox.setName(getName());

                canvas.repaint();
                tools.requestFocus();
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            if (e instanceof FileNotFoundException) {
                JOptionPane.showMessageDialog(frame, "Specified file could not be opened", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "The following error was encountered:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void open_old(File file, boolean splash) {
        try {
            shapes = new ArrayList<>();
            mouseMode = properties.getMode();
            tools.getMode().doClick();
            selected = null;
            tools.setEnabled(true);
            detailPanel.setEnabled(true);
            menu.setEnabled(true);

            if (!splash) {
                properties.setPath(file.getPath());
                properties.save();
            }

            Scanner scan = new Scanner(file);

            double v = 0.0;
            String str = scan.nextLine();
            try {
                v = Double.parseDouble(str);
            } catch (NumberFormatException e) {
            }

            if (v >= oldest_open) {
                String size = scan.nextLine();
                int width = (int) Float.parseFloat(size.substring(0, size.indexOf(":")));
                int height = (int) Float.parseFloat(size.substring(size.indexOf(":") + 1));
                canvas.setPreferredSize(new Dimension(width, height));
                frame.pack();

                boolean back_set = false;
                while (scan.hasNext()) {
                    String line = scan.nextLine();
                    Scanner scanSec = new Scanner(line);
                    scanSec.useDelimiter(":");

                    if (scanSec.hasNext()) {
                        String start = scanSec.next();
                        String[] data = new String[0];

                        while (scanSec.hasNext()) {
                            String[] n = new String[data.length + 1];
                            System.arraycopy(data, 0, n, 0, data.length);

                            String part = scanSec.next();

                            if (part.length() > 0) {
                                n[data.length] = part;
                            }

                            data = n;
                        }

                        if (!back_set) {
                            int r = (int) start.charAt(0);
                            int g = (int) data[0].charAt(0);
                            int b = (int) data[1].charAt(0);

                            if (start.length() == 2 && start.charAt(0) == 'X') {
                                r = 128 + start.charAt(1);
                            }
                            if (data[0].length() == 2 && data[0].charAt(0) == 'X') {
                                g = 128 + data[0].charAt(1);
                            }
                            if (data[1].length() == 2 && data[1].charAt(0) == 'X') {
                                b = 128 + data[1].charAt(1);
                            }

                            canvas.setBackground(new Color(r, g, b));
                            back_set = true;
                        }

                        if (start.equals("l")) {
                            Shape s = new Line(0, 0, Color.white);
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                        if (start.equals("o")) {
                            Shape s = new Oval(0, 0, Color.white);
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                        if (start.equals("g")) {
                            Shape s = new Polygon(0, 0, Color.white);
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                        if (start.equals("p")) {
                            Shape s = new Polyline(0, 0, Color.white);
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                        if (start.equals("r")) {
                            Shape s = new Rectangle(0, 0, Color.white);
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                        if (start.equals("t")) {
                            Shape s = new Text("", 0, 0, Color.white);
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                        if (start.equals("i")) {
                            Shape s = new Picture(0, 0, "");
                            s.fromTextOld(data);
                            shapes.add(s);
                        }
                    }
                }

                fileName = file.getName();
                filePath = file.getPath();
                changed = false;
                PaintBox.setName(getName());

                canvas.repaint();
                tools.requestFocus();
            }
        } catch (FileNotFoundException e) {
        }
    }

    private static String stripExtension(String str) {
        int pos = str.lastIndexOf(".");

        if (pos == -1) {
            return str;
        }

        return str.substring(0, pos);
    }
}
