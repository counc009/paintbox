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
package com.acc240.paintbox.colorSelect;

import com.acc240.paintbox.listener.SuperListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class MemoryColorChooserListener extends SuperListener {

    private JButton ok, cancel;
    private final MemoryColorChooser chooser;
    private JTextField html;
    private JTextField counterR, counterG, counterB, counterA;

    public MemoryColorChooserListener(MemoryColorChooser chooser) {
        this.chooser = chooser;
    }

    public void setOk(JButton button) {
        ok = button;
    }

    public void setCancel(JButton button) {
        cancel = button;
    }

    public void setHTML(JTextField field) {
        html = field;
    }

    public void setR(JTextField r) {
        counterR = r;
    }

    public void setG(JTextField g) {
        counterG = g;
    }

    public void setB(JTextField b) {
        counterB = b;
    }

    public void setA(JTextField a) {
        counterA = a;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == ok) {
            chooser.setDone();
        } else if (event.getSource() == cancel) {
            chooser.cancel();
        } else if (event.getSource() == html) {
            chooser.updateHTML();
        } else if (event.getSource() == counterR || event.getSource() == counterG || event.getSource() == counterB || event.getSource() == counterA) {
            chooser.updateCounters();
        } else {
            Color color = ((JButton) event.getSource()).getBackground();
            chooser.setColor(color);
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        if (!chooser.changing()) {
            chooser.update();
        }
    }

    @Override
    public void windowClosing(WindowEvent event) {
        chooser.cancel();
    }
}
