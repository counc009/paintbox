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
package acc240.paintbox.tools;

import acc240.paintbox.colorSelect.MemoryColorChooser;
import acc240.paintbox.Operations;
import acc240.paintbox.Resources;
import acc240.paintbox.geom.FillableShape;
import acc240.paintbox.listener.SuperListener;
import acc240.paintbox.properties.PropertiesPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBarListener extends SuperListener {

    private static MenuBar menu;

    public MenuBarListener(MenuBar menu) {
        MenuBarListener.menu = menu;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (menu.getComponentNumber((JComponent) event.getSource())) {
            case MenuBar.NEW:
                New.action(event);
                break;
            case MenuBar.SAVE:
                Save.action(event);
                break;
            case MenuBar.SAVE_AS:
                SaveAs.action(event);
                break;
            case MenuBar.EXPORT:
                Export.action(event);
                break;
            case MenuBar.PRINT:
                Print.action(event);
                break;
            case MenuBar.OPEN:
                Open.action(event);
                break;
            case MenuBar.PROPERTIES:
                Properties.action(event);
                break;
            case MenuBar.SCALE:
                Scale.action(event);
                break;
            case MenuBar.BORDER:
                Border.action(event);
                break;
            case MenuBar.FILL:
                Fill.action(event);
                break;
            case MenuBar.MOVE:
                Move.action(event);
                break;
            case MenuBar.BACKGROUND:
                Background.action(event);
                break;
            case MenuBar.TEXT:
                Text.action(event);
                break;
            case MenuBar.PICTURE:
                Picture.action(event);
                break;
            case MenuBar.SQUARE:
                Square.action(event);
                break;
            case MenuBar.CIRCLE:
                Circle.action(event);
                break;
            case MenuBar.TRIANGLE:
                Triangle.action(event);
                break;
            case MenuBar.CUSTOM:
                Import.custom(event, menu);
                break;
            case MenuBar.IMPORT:
                Import.imp(event);
                break;
        }
    }

    private static class New {

        public static void action(ActionEvent event) {
            Operations.clear();
        }
    }

    private static class Save {

        public static void action(ActionEvent event) {
            Operations.save();
        }
    }

    private static class SaveAs {

        public static void action(ActionEvent event) {
            File file = Resources.getFile("Save", "draw files (*.drw)", "drw");
            if (file != null) {
                Operations.save(file);
            }
        }
    }

    private static class Export {

        public static void action(ActionEvent event) {
            switch (((JMenuItem) event.getSource()).getText()) {
                case "PNG":
                    png();
                    break;
                case "JPG":
                    jpg();
                    break;
                case "GIF":
                    gif();
                    break;
            }
        }

        public static void png() {
            File file = Resources.getFile("Export", "png files (*.png)", "png");
            if (file != null) {
                Operations.getCanvas().exportPNG(file);
            }
        }

        public static void jpg() {
            File file = Resources.getFile("Export", "jpg files (*.jpg)", "jpg");
            if (file != null) {
                Operations.getCanvas().exportJPG(file);
            }
        }

        public static void gif() {
            File file = Resources.getFile("Export", "gif files (*.gif)", "gif");
            if (file != null) {
                Operations.getCanvas().exportGIF(file);
            }
        }
    }

    private static class Print {

        public static void action(ActionEvent evnet) {
            Operations.getCanvas().print();
        }
    }

    private static class Open {

        public static void action(ActionEvent event) {
            if (Operations.closeSave()) {
                File file = Resources.getFile("Open", "draw files (*.drw)", "drw");
                if (file != null) {
                    Operations.open(file);
                }
            }
        }
    }

    private static class Properties {

        public static void action(ActionEvent event) {
            new PropertiesPanel(Operations.getFrame());
        }
    }

    private static class Scale {

        public static void action(ActionEvent event) {
            double scaleX, scaleY;

            scaleX = Double.parseDouble(JOptionPane.showInputDialog(Operations.getFrame(), "Horizontal Scale", "Scale", JOptionPane.INFORMATION_MESSAGE));
            scaleY = Double.parseDouble(JOptionPane.showInputDialog(Operations.getFrame(), "Vertical Scale", "Scale", JOptionPane.INFORMATION_MESSAGE));

            Operations.scale(scaleX, scaleY);
        }
    }

    private static class Border {

        public static void action(ActionEvent event) {
            Resources.getColor("Pick Border Color", Operations.getBorder(), MemoryColorChooser.BORDER, (JComponent) event.getSource());
        }
    }

    private static class Fill {

        public static void action(ActionEvent event) {
            System.out.println("AAA");
            if (!((FillableShape) Operations.getSelected()).getFilled()) {
                ((FillableShape) Operations.getSelected()).setFilled();
                Resources.getColor("Pick Fill Color", new Color(255, 255, 255, 0), MemoryColorChooser.FILL, (JComponent) event.getSource());
            } else {
                ((FillableShape) Operations.getSelected()).setUnfilled();
            }

            Operations.update();
        }
    }

    private static class Move {

        public static void action(ActionEvent event) {
            if (Operations.getSelected() != null) {
                Operations.enableMove();
            }
        }
    }

    private static class Background {

        public static void action(ActionEvent event) {
            Resources.getColor("Pick Background Color", Operations.getCanvas().getBackground(), MemoryColorChooser.BACKGROUND, null);
        }
    }

    private static class Text {

        public static void action(ActionEvent event) {
            Operations.setMode(Operations.TEXT);
        }
    }

    private static class Picture {

        public static void action(ActionEvent event) {
            Operations.setMode(Operations.PICTURE);
        }
    }

    private static class Square {

        public static void action(ActionEvent event) {
            Operations.setMode(Operations.SQUARE);
        }
    }

    private static class Circle {

        public static void action(ActionEvent event) {
            Operations.setMode(Operations.CIRCLE);
        }
    }

    private static class Triangle {

        public static void action(ActionEvent event) {
            Operations.setMode(Operations.TRIANGLE);
        }
    }

    private static class Import {

        public static void custom(ActionEvent event, MenuBar menu) {
            int index = menu.getCustoms().indexOf(event.getSource());
            Operations.setCustom(index);
            Operations.setChanged();
        }

        public static void imp(ActionEvent event) {
            Operations.setMode(Operations.IMPORT);
            Operations.setChanged();
        }
    }
}
