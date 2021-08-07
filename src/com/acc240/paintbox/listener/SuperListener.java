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
package com.acc240.paintbox.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class SuperListener implements ActionListener, MouseListener, MouseMotionListener, WindowListener, KeyListener, ChangeListener, ComponentListener, AdjustmentListener {

    @Override
    public void actionPerformed(ActionEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseDragged(MouseEvent event) {
    }

    @Override
    public void windowIconified(WindowEvent event) {
    }

    @Override
    public void windowActivated(WindowEvent event) {
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
    }

    @Override
    public void windowClosed(WindowEvent event) {
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
    }

    @Override
    public void windowClosing(WindowEvent event) {
    }

    @Override
    public void windowOpened(WindowEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void stateChanged(ChangeEvent event) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
    }
}
