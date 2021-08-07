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

import com.acc240.paintbox.listener.SuperListener;

import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PaintBoxListener extends SuperListener {

    private final JFrame frame;

    public PaintBoxListener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (Operations.isChanged()) {
            int reply = JOptionPane.showConfirmDialog(null, "Unsaved changes, do you wish to save?");
            if (reply == JOptionPane.YES_OPTION) {
                if (Operations.getFileName().equals("")) {
                    File file = Resources.getFile("Save", "draw files (*.drw)", "drw");
                    if (file != null) {
                        Operations.save(file);
                    }
                } else {
                    Operations.save();
                }
                frame.dispose();
                System.exit(0);
            } else if (reply == JOptionPane.CANCEL_OPTION) {
            } else {
                frame.dispose();
                System.exit(0);
            }
        } else {
            frame.dispose();
            System.exit(0);
        }
    }
}
