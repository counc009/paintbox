package com.acc240.paintbox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

public class AlphaButton extends JButton {

    public AlphaButton(String text) {
        super(text);
    }

    public AlphaButton() {
        super();
    }

    @Override
    public void paintComponent(Graphics p) {
        Graphics2D page = (Graphics2D) p;

        int width = getWidth();
        int height = getHeight();

        page.setColor(Color.darkGray);
        page.fillRect(0, 0, width / 4, height / 2);
        page.fillRect(width / 2, 0, width / 4, height / 2);
        page.fillRect(width / 4, height / 2, width / 4, height / 2);
        page.fillRect(3 * width / 4, height / 2, width / 4, height / 2);

        page.setColor(Color.lightGray);
        page.fillRect(width / 4, 0, width / 4, height / 2);
        page.fillRect(3 * width / 4, 0, width / 4, height / 2);
        page.fillRect(0, height / 2, width / 4, height / 2);
        page.fillRect(width / 2, height / 2, width / 4, height / 2);

        page.setColor(getBackground());
        page.fillRect(0, 0, width, height);

        Color background = getBackground();
        setBackground(new Color(255, 255, 255, 0));
        super.paintComponent(page);
        setBackground(background);
    }
}
