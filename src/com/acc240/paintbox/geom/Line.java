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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;

public class Line extends Shape {

    private int x1, x2, y1, y2;

    public Line(Point point, Color col) {
        this((int) point.getX(), (int) point.getY(), col);
    }

    public Line(Point point, Color col, float strokeWidth) {
        this(point, point, col, strokeWidth);
    }

    public Line(int x1, int y1, Color col) {
        this(x1, y1, x1, y1, col, 1.0f);
    }

    public Line(Point point1, Point point2, Color col) {
        this((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY(), col, 1.0f);
    }

    public Line(Point point1, Point point2, Color col, float strokeWidth) {
        this((int) point1.getX(), (int) point1.getY(), (int) point1.getX(), (int) point1.getY(), col, strokeWidth);
    }

    public Line(int x1, int y1, int x2, int y2, Color col, float strokeWidth) {
        super("Line", strokeWidth);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        borderColor = col;
        letterName = "l";
    }

    @Override
    public void move(int x, int y) {
        x1 += x;
        x2 += x;

        y1 += y;
        y2 += y;
    }

    @Override
    public void scale(double scaleX, double scaleY) {
        x1 *= scaleX;
        x2 *= scaleX;
        y1 *= scaleY;
        y2 *= scaleY;
    }

    @Override
    public Shape copy() {
        Line result = new Line(x1, y1, x2, y2, borderColor, strokeWidth);
        Resources.addCopy(result, this);
        return result;
    }

    @Override
    public void moveTo(int[][] points) {
        x1 = points[0][0];
        x2 = points[0][1];

        y1 = points[1][0];
        y2 = points[1][1];
    }

    @Override
    public Point getCenter() {
        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    @Override
    public int[][] getPoints() {
        int[][] result = new int[2][2];

        result[0][0] = x1;
        result[0][1] = x2;
        result[1][0] = y1;
        result[1][1] = y2;

        return result;
    }

    @Override
    public void draw(Graphics p) {
        Graphics2D page = (Graphics2D) p;
        page.setStroke(new BasicStroke(strokeWidth));

        page.setColor(borderColor);

        page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
        page.drawLine(x1, y1, x2, y2);
        page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
    }

    @Override
    public void drawBox(Graphics p, Color color) {
        Graphics2D page = (Graphics2D) p;
        draw(page);
        page.setStroke(new BasicStroke());

        page.setColor(color);
        double l = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double cx = (x1 + x2) / 2.0;
        double cy = (y1 + y2) / 2.0;
        page.drawOval((int) (cx - (l * 0.5)), (int) (cy - (l * 0.5)), (int) l, (int) l);
    }

    @Override
    public boolean inBox(Point spot) {
        int x = (int) spot.getX(), y = (int) spot.getY();
        double l = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double cx = (x1 + x2) / 2.0;
        double cy = (y1 + y2) / 2.0;
        return Math.abs(Math.sqrt(Math.pow(cx - x, 2) + Math.pow(cy - y, 2))) <= l / 2.0;
    }

    public void setEnd(Point point) {
        setEnd((int) point.getX(), (int) point.getY());
    }

    public void setEnd(int x, int y) {
        x2 = x;
        y2 = y;
    }

    public void setStart(int x, int y) {
        x1 = x;
        y1 = y;
    }

    @Override
    public String toText() {
        if (borderColor == null) {
            borderColor = Color.black;
        }
        String result = letterName + ":" + x1 + ":" + y1 + ":" + x2 + ":" + y2 + ":";
        result += rotation + ":";
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
        result += strokeWidth;
        return result;
    }

    @Override
    public String toGeneral() {
        return letterName + ":" + (x2 - x1) + ":" + (y2 - y1);
    }

    @Override
    public void fromText(String[] data) {
        x1 = Integer.parseInt(data[0]);
        y1 = Integer.parseInt(data[1]);
        x2 = Integer.parseInt(data[2]);
        y2 = Integer.parseInt(data[3]);
        rotation = Double.parseDouble(data[4]);

        int r = (int) (data[5].charAt(0));
        int g = (int) (data[6].charAt(0));
        int b = (int) (data[7].charAt(0));
        int a = (int) (data[8].charAt(0));

        if (data[5].length() == 2 && data[5].charAt(0) == 'X') {
            r = 128 + data[5].charAt(1);
        }
        if (data[6].length() == 2 && data[6].charAt(0) == 'X') {
            g = 128 + data[6].charAt(1);
        }
        if (data[7].length() == 2 && data[7].charAt(0) == 'X') {
            b = 128 + data[7].charAt(1);
        }
        if (data[8].length() == 2 && data[8].charAt(0) == 'X') {
            a = 128 + data[8].charAt(1);
        }

        borderColor = new Color(r, g, b, a);
        strokeWidth = Float.parseFloat(data[9]);
    }

    @Override
    public void fromTextOld(String[] data) {
        x1 = Integer.parseInt(data[0]);
        y1 = Integer.parseInt(data[1]);
        x2 = Integer.parseInt(data[2]);
        y2 = Integer.parseInt(data[3]);

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
        strokeWidth = Float.parseFloat(data[8]);
    }

    @Override
    public void fromGeneral(String[] data, Point point) {
        int[] numbers = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            numbers[i] = Integer.parseInt(data[i]);
        }

        x1 = (int) point.getX();
        y1 = (int) point.getY();
        x2 = numbers[0] + x1;
        y2 = numbers[1] + y1;
    }

    @Override
    public void center() {
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;
        int dx = (Operations.getCanvas().getWidth() / 2) - mx;
        int dy = (Operations.getCanvas().getHeight() / 2) - my;
        move(dx, dy);
        Operations.setChanged();
        Operations.update();
    }

    @Override
    public JPanel getPointPanel() {
        return new ShapePanel(ShapePanel.Line, this);
    }
}
