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
