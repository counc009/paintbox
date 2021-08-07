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
package com.acc240.paintbox.geom;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.details.ShapePanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

public class Text extends Shape {

    public Font font;
    public String text;
    public int bottomX, bottomY;

    private FontMetrics metrics;

    public Text() {
        this("Message");
    }

    public Text(String message) {
        this(message, 0, 0);
    }

    public Text(String message, Point point) {
        this(message, (int) point.getX(), (int) point.getY());
    }

    public Text(String message, int x, int y) {
        this(message, x, y, Color.black);
    }

    public Text(String message, Point point, Color color) {
        this(message, (int) point.getX(), (int) point.getY(), color);
    }

    public Text(String message, int x, int y, Color color) {
        this(message, x, y, color, new Font("Times New Roman", Font.BOLD, 26));
    }

    public Text(String message, int x, int y, Color color, Font format) {
        super("Text");
        text = message;
        font = format;
        borderColor = color;

        bottomX = x;
        bottomY = y;

        letterName = "t";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public Point getCenter() {
        return new Point((int) (bottomX + 0.5 * metrics.stringWidth(text)), (int) (bottomY - 0.5 * metrics.getHeight()));
    }

    @Override
    public void draw(Graphics p) {
        Graphics2D page = (Graphics2D) p;
        page.setStroke(new BasicStroke(strokeWidth));

        page.setFont(font);
        page.setColor(borderColor);

        metrics = p.getFontMetrics(font);

        ((Graphics2D) p).rotate(rotation, bottomX + 0.5 * metrics.stringWidth(text), bottomY - 0.5 * metrics.getHeight());
        page.drawString(text, bottomX, bottomY);
        ((Graphics2D) p).rotate(-1 * rotation, bottomX + 0.5 * metrics.stringWidth(text), bottomY - 0.5 * metrics.getHeight());

    }

    @Override
    public void drawBox(Graphics p, Color color) {
        Graphics2D page = (Graphics2D) p;
        draw(page);
        page.setStroke(new BasicStroke());

        metrics = p.getFontMetrics(font);

        page.setColor(color);
        double l = Math.sqrt(Math.pow(metrics.stringWidth(text), 2) + Math.pow(metrics.getHeight(), 2));
        double cx = (2.0 * bottomX + metrics.stringWidth(text)) / 2.0;
        double cy = (2.0 * bottomY - metrics.getHeight()) / 2.0;
        page.drawOval((int) (cx - (l * 0.5)), (int) (cy - (l * 0.5)), (int) l, (int) l);
    }

    @Override
    public void move(int x, int y) {
        bottomX += x;
        bottomY += y;
    }

    @Override
    public void scale(double scaleX, double scaleY) {
        bottomX *= scaleX;
        bottomY *= scaleY;
        font = new Font(font.getFamily(), font.getStyle(), (int) (font.getSize() * Math.min(scaleX, scaleY)));
    }

    @Override
    public Shape copy() {
        Text result = new Text(text, bottomX, bottomY, borderColor, font);
        Resources.addCopy(result, this);
        return result;
    }

    public void moveTo(int x, int y) {
        bottomX = x;
        bottomY = y;
    }

    @Override
    public int[][] getPoints() {
        int[][] points = new int[2][1];
        points[0][0] = bottomX;
        points[1][0] = bottomY;
        return points;
    }

    @Override
    public void moveTo(int[][] points) {
        bottomX = points[0][0];
        bottomY = points[1][0];
    }

    @Override
    public boolean inBox(Point point) {
        int x = (int) point.getX(), y = (int) point.getY();
        double l = Math.sqrt(Math.pow(metrics.stringWidth(text), 2) + Math.pow(metrics.getHeight(), 2));
        double cx = (2.0 * bottomX + metrics.stringWidth(text)) / 2.0;
        double cy = (2.0 * bottomY - metrics.getHeight()) / 2.0;
        return Math.abs(Math.sqrt(Math.pow(cx - x, 2) + Math.pow(cy - y, 2))) <= l / 2.0;
    }

    @Override
    public String toText() {
        String result = letterName + ":" + bottomX + ":" + bottomY + ":" + text + ":" + rotation + ":";
        if (borderColor.getRed() >= 128) {
            result += "X" + (char) (borderColor.getRed() - 128) + ":";
        } else {
            result += (char) borderColor.getRed() + ":";
        }
        if (borderColor.getGreen() >= 128) {
            result += "X" + (char) (borderColor.getGreen() - 128) + ":";
        } else {
            result += (char) borderColor.getGreen() + ":";
        }
        if (borderColor.getBlue() >= 128) {
            result += "X" + (char) (borderColor.getBlue() - 128) + ":";
        } else {
            result += (char) borderColor.getBlue() + ":";
        }
        if (borderColor.getAlpha() >= 128) {
            result += "X" + (char) (borderColor.getAlpha() - 128) + ":";
        } else {
            result += (char) borderColor.getAlpha() + ":";
        }
        result += font.getFamily() + ":" + font.getStyle() + ":" + font.getSize() + ":" + strokeWidth;
        return result;
    }

    @Override
    public String toGeneral() {
        return toText();
    }

    @Override
    public void fromText(String[] data) {
        bottomX = Integer.parseInt(data[0]);
        bottomY = Integer.parseInt(data[1]);
        text = data[2];
        rotation = Double.parseDouble(data[3]);

        int r = (int) (data[4].charAt(0));
        int g = (int) (data[5].charAt(0));
        int b = (int) (data[6].charAt(0));
        int a = (int) (data[7].charAt(0));

        if (data[4].length() == 2 && data[4].charAt(0) == 'X') {
            r = 128 + data[4].charAt(1);
        }
        if (data[5].length() == 2 && data[5].charAt(0) == 'X') {
            g = 128 + data[5].charAt(1);
        }
        if (data[6].length() == 2 && data[6].charAt(0) == 'X') {
            b = 128 + data[6].charAt(1);
        }
        if (data[7].length() == 2 && data[7].charAt(0) == 'X') {
            a = 128 + data[7].charAt(1);
        }

        borderColor = new Color(r, g, b, a);

        font = new Font(data[8], Integer.parseInt(data[9]), Integer.parseInt(data[10]));
        strokeWidth = Float.parseFloat(data[11]);
    }

    @Override
    public void fromTextOld(String[] data) {
        bottomX = Integer.parseInt(data[0]);
        bottomY = Integer.parseInt(data[1]);
        text = data[2];

        int r = (int) (data[3].charAt(0));
        int g = (int) (data[4].charAt(0));
        int b = (int) (data[5].charAt(0));
        int a = (int) (data[6].charAt(0));

        if (data[3].length() == 2 && data[3].charAt(0) == 'X') {
            r = 128 + data[3].charAt(1);
        }
        if (data[4].length() == 2 && data[4].charAt(0) == 'X') {
            g = 128 + data[4].charAt(1);
        }
        if (data[5].length() == 2 && data[5].charAt(0) == 'X') {
            b = 128 + data[5].charAt(1);
        }
        if (data[6].length() == 2 && data[6].charAt(0) == 'X') {
            a = 128 + data[6].charAt(1);
        }

        borderColor = new Color(r, g, b, a);

        font = new Font(data[7], Integer.parseInt(data[8]), Integer.parseInt(data[9]));
        strokeWidth = Float.parseFloat(data[10]);
    }

    @Override
    public void fromGeneral(String[] data, Point point) {
        fromText(data);
    }

    private int[][] getRectangleValues() {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
        int textheight = (int) (font.getStringBounds(text, frc).getHeight());

        int[][] points = new int[2][2];
        points[0][0] = bottomX;
        points[1][0] = bottomY - textheight;
        points[0][1] = textwidth;
        points[1][1] = textheight;

        return points;
    }

    @Override
    public void center() {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
        int textheight = (int) (font.getStringBounds(text, frc).getHeight());

        int mx = bottomX + textwidth / 2;
        int my = bottomY - textheight / 2;
        int dx = (Operations.getCanvas().getWidth() / 2) - mx;
        int dy = (Operations.getCanvas().getHeight() / 2) - my;
        move(dx, dy);
        Operations.setChanged();
        Operations.update();
    }

    @Override
    public JPanel getPointPanel() {
        return new ShapePanel(ShapePanel.Text, this);
    }
}
