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
