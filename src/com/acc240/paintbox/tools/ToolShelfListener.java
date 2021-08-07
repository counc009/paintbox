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
package com.acc240.paintbox.tools;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.colorSelect.MemoryColorChooser;
import com.acc240.paintbox.geom.Shape;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.listener.SuperListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;

public class ToolShelfListener extends SuperListener {

    private static ToolShelf tools;

    private static boolean control = false;
    private static boolean shift = false;

    public ToolShelfListener(ToolShelf tools) {
        ToolShelfListener.tools = tools;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource();

        if (tools.getCursorModes().indexOf(source) != -1) {
            CursorModes.action(event);
        } else if (source == tools.getBorderButton()) {
            Border.action(event);
        } else if (source == tools.getMove()) {
            Move.action(event);
        } else if(source == tools.getRotate()) {
            Rotate.action(event);
        } else if (source == tools.getDelete()) {
            Delete.action(event);
        } else if (source == tools.getCopy()) {
            Copy.action(event);
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        HotKeys.pressed(event);
    }

    private static class CursorModes {

        public static void action(ActionEvent event) {
            JComponent source = (JComponent) event.getSource();
            source.setBackground(Color.yellow);

            for (JButton button : tools.getCursorModes()) {
                if (button != source) {
                    button.setBackground(tools.getDefaultColor());
                }
            }

            Operations.setMode(tools.getCursorModes().indexOf(source));

            Operations.update();

            if (Operations.getMode() != Operations.SELECT) {
                tools.getFill().setEnabled(false);
                tools.getMove().setEnabled(false);
            } else {
                tools.updateSelected();
            }
        }
    }

    public static class Border {

        public static void action(ActionEvent event) {
            Resources.getColor("Pick Border Color", Operations.getBorder(), MemoryColorChooser.BORDER, (JComponent) event.getSource());
        }
    }

    private static class Move {

        public static void action(ActionEvent event) {
            if (Operations.getSelected() != null) {
                Operations.enableMove();
            }
        }
    }
    
    private static class Rotate {
        public static void action(ActionEvent event) {
            if (Operations.getSelected() != null) {
                Operations.enableRotate();
            }
        }
    }

    private static class Delete {

        public static void action(ActionEvent event) {
            if (Operations.getSelected() != null) {
                Operations.deleteSelected();
            }
        }
    }

    private static class Copy {

        public static void action(ActionEvent event) {
            if (Operations.getSelected() != null) {
                Shape shape = Operations.getSelected().copy();
                Operations.setSelected(shape);
                Operations.update();
                Operations.enableMove();
            }
        }
    }

    private static class HotKeys {

        public static void pressed(KeyEvent event) {
            ArrayList<JButton> cursorModes = tools.getCursorModes();
            boolean nochange = false;

            switch (event.getKeyCode()) {
                case KeyEvent.VK_C:
                    if (control) {
                        tools.getCopy().doClick();
                    } else {
                        cursorModes.get(0).doClick();
                    }
                    break;
                case KeyEvent.VK_S:
                    if (control) {
                        if (shift) {
                            File file = Resources.getFile("Save", "draw files (*.drw)", "drw");
                            if (file != null) {
                                Operations.save(file);
                            }
                        } else {
                            Operations.save();
                        }
                    } else {
                        cursorModes.get(1).doClick();
                    }
                    break;
                case KeyEvent.VK_R:
                    cursorModes.get(2).doClick();
                    break;
                case KeyEvent.VK_O:
                    if (control) {
                        File file = Resources.getFile("Open", "draw files (*.drw)", "drw");
                        if (file != null) {
                            Operations.open(file);
                        }
                    } else {
                        cursorModes.get(3).doClick();
                    }
                    break;
                case KeyEvent.VK_L:
                    cursorModes.get(4).doClick();
                    break;
                case KeyEvent.VK_P:
                    cursorModes.get(5).doClick();
                    break;
                case KeyEvent.VK_G:
                    cursorModes.get(6).doClick();
                    break;
                case KeyEvent.VK_B:
                    tools.getBorderButton().doClick();
                    break;
                case KeyEvent.VK_M:
                    tools.getMove().doClick();
                    break;
                case KeyEvent.VK_D:
                    tools.getDelete().doClick();
                    break;
                case KeyEvent.VK_N:
                    if (control) {
                        Operations.clear();
                    }
                    break;
                case KeyEvent.VK_CONTROL:
                    control = true;
                    nochange = true;
                    break;
                case KeyEvent.VK_SHIFT:
                    shift = true;
                    nochange = true;
                    break;
            }

            if (!nochange) {
                control = false;
                shift = false;
            }
        }
    }
}
