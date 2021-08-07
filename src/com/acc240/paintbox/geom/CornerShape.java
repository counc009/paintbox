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
import com.acc240.paintbox.details.ShapePanel;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JPanel;

public abstract class CornerShape extends FillableShape {

    protected int x1, x2, y1, y2;
    protected int ox, oy; //original x and y
    protected int op; //pair number of originals

    public CornerShape(String name, float strokeWidth) {
        super(name, strokeWidth);
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

    public Point getTopCorner() {
        return new Point(x1, y1);
    }

    public void setTopCorner(Point point) {
        x2 = (int) point.getX() + (x2 - x1);
        y2 = (int) point.getY() + (y2 - y1);
        x1 = (int) point.getX();
        y1 = (int) point.getY();
    }

    public Point getBottomCorner() {
        return new Point(x2, y2);
    }

    public void setBottomCorner(Point point) {
        x2 = (int) point.getX();
        y2 = (int) point.getY();
    }

    public void setWidth(int width) {
        x2 = x1 + width;
    }

    public int getWidth() {
        return x2 - x1;
    }

    public void setHeight(int height) {
        y2 = y1 + height;
    }

    public int getHeight() {
        return y2 - y1;
    }

    @Override
    public String toText() {
        if (borderColor == null) {
            borderColor = Color.black;
        }

        String result = letterName + ":" + x1 + ":" + y1 + ":" + x2 + ":" + y2 + ":" + ox + ":" + oy + ":" + op + ":" + rotation + ":";
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

        if (filled && filling != null) {
            if (filling.getRed() >= 128) {
                result += "X" + (char) (filling.getRed() - 128) + ":";
            } else {
                result += (char) filling.getRed() + ":";
            }
            if (filling.getGreen() >= 128) {
                result += "X" + (char) (filling.getGreen() - 128) + ":";
            } else {
                result += (char) filling.getGreen() + ":";
            }
            if (filling.getBlue() >= 128) {
                result += "X" + (char) (filling.getBlue() - 128) + ":";
            } else {
                result += (char) filling.getBlue() + ":";
            }
            if (filling.getAlpha() >= 128) {
                result += "X" + (char) (filling.getAlpha() - 128) + ":";
            } else {
                result += (char) filling.getAlpha() + ":";
            }
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
        ox = Integer.parseInt(data[4]);
        oy = Integer.parseInt(data[5]);
        op = Integer.parseInt(data[6]);

        rotation = Double.parseDouble(data[7]);
        int r = (int) (data[8].charAt(0));
        int g = (int) (data[9].charAt(0));
        int b = (int) (data[10].charAt(0));
        int a = (int) (data[11].charAt(0));

        if (data[8].length() == 2 && data[8].charAt(0) == 'X') {
            r = 128 + data[8].charAt(1);
        }
        if (data[9].length() == 2 && data[9].charAt(0) == 'X') {
            g = 128 + data[9].charAt(1);
        }
        if (data[10].length() == 2 && data[10].charAt(0) == 'X') {
            b = 128 + data[10].charAt(1);
        }
        if (data[11].length() == 2 && data[11].charAt(0) == 'X') {
            a = 128 + data[11].charAt(1);
        }

        borderColor = new Color(r, g, b, a);

        if (data.length >= 15) {
            filled = true;

            r = (int) (data[12].charAt(0));
            g = (int) (data[13].charAt(0));
            b = (int) (data[14].charAt(0));
            a = (int) (data[15].charAt(0));

            if (data[12].length() == 2 && data[12].charAt(0) == 'X') {
                r = 128 + data[12].charAt(1);
            }
            if (data[13].length() == 2 && data[13].charAt(0) == 'X') {
                g = 128 + data[13].charAt(1);
            }
            if (data[14].length() == 2 && data[14].charAt(0) == 'X') {
                b = 128 + data[14].charAt(1);
            }
            if (data[15].length() == 2 && data[15].charAt(0) == 'X') {
                a = 128 + data[15].charAt(1);
            }

            filling = new Color(r, g, b, a);
            strokeWidth = Float.parseFloat(data[16]);
        } else {
            filled = false;
            filling = null;
            strokeWidth = Float.parseFloat(data[13]);
        }
    }

    @Override
    public void fromTextOld(String[] data) {
        x1 = Integer.parseInt(data[0]);
        y1 = Integer.parseInt(data[1]);
        x2 = Integer.parseInt(data[2]);
        y2 = Integer.parseInt(data[3]);
        ox = Integer.parseInt(data[4]);
        oy = Integer.parseInt(data[5]);
        op = Integer.parseInt(data[6]);

        int r = (int) (data[7].charAt(0));
        int g = (int) (data[8].charAt(0));
        int b = (int) (data[9].charAt(0));
        int a = (int) (data[10].charAt(0));

        if (data[7].length() == 2 && data[7].charAt(0) == 'X') {
            r = 128 + data[7].charAt(1);
        }
        if (data[8].length() == 2 && data[8].charAt(0) == 'X') {
            g = 128 + data[8].charAt(1);
        }
        if (data[9].length() == 2 && data[9].charAt(0) == 'X') {
            b = 128 + data[9].charAt(1);
        }
        if (data[10].length() == 2 && data[10].charAt(0) == 'X') {
            a = 128 + data[10].charAt(1);
        }

        borderColor = new Color(r, g, b, a);

        if (data.length >= 14) {
            filled = true;

            r = (int) (data[11].charAt(0));
            g = (int) (data[12].charAt(0));
            b = (int) (data[13].charAt(0));
            a = (int) (data[14].charAt(0));

            if (data[11].length() == 2 && data[11].charAt(0) == 'X') {
                r = 128 + data[11].charAt(1);
            }
            if (data[12].length() == 2 && data[12].charAt(0) == 'X') {
                g = 128 + data[12].charAt(1);
            }
            if (data[13].length() == 2 && data[13].charAt(0) == 'X') {
                b = 128 + data[13].charAt(1);
            }
            if (data[14].length() == 2 && data[14].charAt(0) == 'X') {
                a = 128 + data[14].charAt(1);
            }

            filling = new Color(r, g, b, a);
            strokeWidth = Float.parseFloat(data[15]);
        } else {
            filled = false;
            filling = null;
            strokeWidth = Float.parseFloat(data[12]);
        }
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

        ox = x1;
        oy = y1;
        op = 1;
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
        return new ShapePanel(ShapePanel.CornerShape, this);
    }
}
