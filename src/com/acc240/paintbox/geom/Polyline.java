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

public class Polyline extends Shape implements Poly {

    protected int[] xs, ys;

    public Polyline(Point point, Color col) {
        this((int) point.getX(), (int) point.getY(), col);
    }

    public Polyline(Point point, Color col, float strokeWidth) {
        this(point, point, col, strokeWidth);
    }

    public Polyline(int x1, int y1, Color col) {
        this(x1, y1, x1, y1, col, 1.0f);
    }

    public Polyline(Point point1, Point point2, Color col) {
        this((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY(), col, 1.0f);
    }

    public Polyline(Point point1, Point point2, Color col, float strokeWidth) {
        this((int) point1.getX(), (int) point1.getY(), (int) point1.getX(), (int) point1.getY(), col, strokeWidth);
    }

    public Polyline(int x1, int y1, int x2, int y2, Color col, float strokeWidth) {
        super("Polyline", strokeWidth);

        xs = new int[2];
        ys = new int[2];

        xs[0] = x1;
        ys[0] = y1;
        xs[1] = x2;
        ys[1] = y2;

        borderColor = col;
        letterName = "p";
    }

    @Override
    public void move(int x, int y) {
        for (int i = 0; i < xs.length; i++) {
            xs[i] += x;
        }

        for (int i = 0; i < ys.length; i++) {
            ys[i] += y;
        }
    }

    @Override
    public void scale(double scaleX, double scaleY) {
        for (int i = 0; i < xs.length; i++) {
            xs[i] *= scaleX;
        }

        for (int i = 0; i < ys.length; i++) {
            ys[i] *= scaleY;
        }
    }

    @Override
    public Shape copy() {
        Polyline result = new Polyline(xs[0], ys[0], xs[1], ys[1], borderColor, 1.0f);
        for (int i = 2; i < xs.length; i++) {
            result.addPoint(xs[i], ys[i]);
        }
        Resources.addCopy(result, this);
        return result;
    }

    @Override
    public void moveTo(int[][] points) {
        xs = new int[points[0].length];
        ys = new int[points[0].length];

        for (int i = 0; i < points[0].length; i++) {
            xs[i] = points[0][i];
            ys[i] = points[1][i];
        }
    }

    @Override
    public Point getCenter() {
        double cx = 0.0;
        double cy = 0.0;

        for (int i = 0; i < xs.length; i++) {
            cx += xs[i];
            cy += ys[i];
        }

        cx /= xs.length;
        cy /= ys.length;

        return new Point((int) cx, (int) cy);
    }

    @Override
    public int[][] getPoints() {
        int[][] result = new int[2][xs.length];

        for (int i = 0; i < xs.length; i++) {
            result[0][i] = xs[i];
            result[1][i] = ys[i];
        }

        return result;
    }

    @Override
    public void draw(Graphics p) {
        Graphics2D page = (Graphics2D) p;
        page.setStroke(new BasicStroke(strokeWidth));
        page.setColor(borderColor);

        double cx = 0.0;
        double cy = 0.0;

        for (int i = 0; i < xs.length; i++) {
            cx += xs[i];
            cy += ys[i];
        }

        cx /= xs.length;
        cy /= ys.length;

        page.rotate(rotation, cx, cy);
        page.drawPolyline(xs, ys, xs.length);
        page.rotate(-1 * rotation, cx, cy);
    }

    @Override
    public void drawBox(Graphics p, Color color) {
        Graphics2D page = (Graphics2D) p;
        draw(page);
        page.setStroke(new BasicStroke());

        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

        for (int i = 0; i < xs.length; i++) {
            if (x1 == -1 || xs[i] < x1) {
                x1 = xs[i];
            }
            if (x2 == -1 || xs[i] > x2) {
                x2 = xs[i];
            }

            if (y1 == -1 || ys[i] < y1) {
                y1 = ys[i];
            }
            if (y2 == -1 || ys[i] > y2) {
                y2 = ys[i];
            }
        }

        page.setColor(color);
        double l = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double cx = (x1 + x2) / 2.0;
        double cy = (y1 + y2) / 2.0;
        page.drawOval((int) (cx - (l * 0.5)), (int) (cy - (l * 0.5)), (int) l, (int) l);
    }

    @Override
    public boolean inBox(Point spot) {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

        for (int i = 0; i < xs.length; i++) {
            if (x1 == -1 || xs[i] < x1) {
                x1 = xs[i];
            }
            if (x2 == -1 || xs[i] > x2) {
                x2 = xs[i];
            }

            if (y1 == -1 || ys[i] < y1) {
                y1 = ys[i];
            }
            if (y2 == -1 || ys[i] > y2) {
                y2 = ys[i];
            }
        }

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
        xs[xs.length - 1] = x;
        ys[ys.length - 1] = y;
    }

    public void removeEnd() {
        int[] xt = new int[xs.length - 1];
        int[] yt = new int[ys.length - 1];

        for (int i = 0; i < xs.length - 1; i++) {
            xt[i] = xs[i];
            yt[i] = ys[i];
        }

        xs = xt;
        ys = yt;
    }

    public void addPoint(Point point) {
        addPoint((int) point.getX(), (int) point.getY());
    }

    @Override
    public void addPoint(int x, int y) {
        int[] xt = new int[xs.length + 1];
        int[] yt = new int[ys.length + 1];

        for (int i = 0; i < xs.length; i++) {
            xt[i] = xs[i];
            yt[i] = ys[i];
        }

        xt[xs.length] = x;
        yt[ys.length] = y;

        xs = xt;
        ys = yt;
    }

    public void insert(Point point, int index) {
        insert((int) point.getX(), (int) point.getY(), index);
    }

    @Override
    public void insert(int index, int x, int y) {
        int[] xt = new int[xs.length + 1];
        int[] yt = new int[ys.length + 1];

        int offset = 0;
        for (int i = 0; i < xs.length; i++) {
            xt[i + offset] = xs[i];
            yt[i + offset] = ys[i];

            if (i == index) {
                xt[i + offset + 1] = x;
                yt[i + offset + 1] = y;

                offset++;
            }
        }

        xs = xt;
        ys = yt;
    }

    @Override
    public void delete(int index) {
        int[] xt = new int[xs.length - 1];
        int[] yt = new int[ys.length - 1];

        int offset = 0;
        for (int i = 0; i < xs.length; i++) {
            if (i == index) {
                offset--;
            } else {
                xt[i + offset] = xs[i];
                yt[i + offset] = ys[i];
            }
        }

        xs = xt;
        ys = yt;
    }

    @Override
    public void randomize(int max) {
        for (int i = 0; i < xs.length; i++) {
            xs[i] += (int) (Math.random() * 2 * max) - max;
            ys[i] += (int) (Math.random() * 2 * max) - max;
        }
        Operations.setChanged();
        Operations.update();
    }

    @Override
    public String toText() {
        String result = letterName;
        for (int i = 0; i < xs.length; i++) {
            result += ":" + xs[i] + ":" + ys[i];
        }

        result += ":-1:" + rotation + ":";
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
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

        for (int i = 0; i < xs.length; i++) {
            if (x1 == -1 || xs[i] < x1) {
                x1 = xs[i];
            }
            if (x2 == -1 || xs[i] > x2) {
                x2 = xs[i];
            }

            if (y1 == -1 || ys[i] < y1) {
                y1 = ys[i];
            }
            if (y2 == -1 || ys[i] > y2) {
                y2 = ys[i];
            }
        }

        String result = letterName;

        for (int i = 0; i < xs.length; i++) {
            result += ":" + (xs[i] - x1) + ":" + (ys[i] - y1);
        }

        return result;
    }

    @Override
    public void fromText(String[] data) {
        int[] numbers = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                numbers[i] = Integer.parseInt(data[i]);
            } catch (NumberFormatException e) {
                break;
            }
        }

        int last = 0;
        xs = new int[0];
        ys = new int[0];

        for (int i = 0; i < numbers.length - 1; i += 2) {
            if (numbers[i] == -1) {
                last = i;
                break;
            } else {
                addPoint(numbers[i], numbers[i + 1]);
            }
        }

        rotation = Double.parseDouble(data[last + 1]);
        int r = (int) (data[last + 2].charAt(0));
        int g = (int) (data[last + 3].charAt(0));
        int b = (int) (data[last + 4].charAt(0));
        int a = (int) (data[last + 5].charAt(0));

        if (data[last + 2].length() == 2 && data[last + 2].charAt(0) == 'X') {
            r = 128 + data[last + 2].charAt(1);
        }
        if (data[last + 3].length() == 2 && data[last + 3].charAt(0) == 'X') {
            g = 128 + data[last + 3].charAt(1);
        }
        if (data[last + 4].length() == 2 && data[last + 4].charAt(0) == 'X') {
            b = 128 + data[last + 4].charAt(1);
        }
        if (data[last + 5].length() == 2 && data[last + 5].charAt(0) == 'X') {
            a = 128 + data[last + 5].charAt(1);
        }

        borderColor = new Color(r, g, b);
        strokeWidth = Float.parseFloat(data[last + 6]);
    }

    @Override
    public void fromTextOld(String[] data) {
        int[] numbers = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                numbers[i] = Integer.parseInt(data[i]);
            } catch (NumberFormatException e) {
                break;
            }
        }

        int last = 0;
        xs = new int[0];
        ys = new int[0];

        for (int i = 0; i < numbers.length - 1; i += 2) {
            if (numbers[i] == -1) {
                last = i;
                break;
            } else {
                addPoint(numbers[i], numbers[i + 1]);
            }
        }

        int r = (int) (data[last + 1].charAt(0));
        int g = (int) (data[last + 2].charAt(0));
        int b = (int) (data[last + 3].charAt(0));
        int a = (int) (data[last + 4].charAt(0));

        if (data[last + 1].length() == 2 && data[last + 1].charAt(0) == 'X') {
            r = 128 + data[last + 1].charAt(1);
        }
        if (data[last + 2].length() == 2 && data[last + 2].charAt(0) == 'X') {
            g = 128 + data[last + 2].charAt(1);
        }
        if (data[last + 3].length() == 2 && data[last + 3].charAt(0) == 'X') {
            b = 128 + data[last + 3].charAt(1);
        }
        if (data[last + 4].length() == 2 && data[last + 4].charAt(0) == 'X') {
            a = 128 + data[last + 4].charAt(1);
        }

        borderColor = new Color(r, g, b);
        strokeWidth = Float.parseFloat(data[last + 5]);
    }

    @Override
    public void fromGeneral(String[] data, Point point) {
        xs = new int[0];
        ys = new int[0];

        int[] numbers = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            numbers[i] = Integer.parseInt(data[i]);
        }

        int x = (int) point.getX();
        int y = (int) point.getY();

        for (int i = 0; i < numbers.length; i += 2) {
            addPoint(new Point(x + numbers[i], y + numbers[i + 1]));
        }
    }

    @Override
    public void center() {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

        for (int i = 0; i < xs.length; i++) {
            if (x1 == -1 || xs[i] < x1) {
                x1 = xs[i];
            }
            if (x2 == -1 || xs[i] > x2) {
                x2 = xs[i];
            }

            if (y1 == -1 || ys[i] < y1) {
                y1 = ys[i];
            }
            if (y2 == -1 || ys[i] > y2) {
                y2 = ys[i];
            }
        }

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
        return new ShapePanel(ShapePanel.Poly, this);
    }

    @Override
    public void setPoint(int index, int x, int y) {
        xs[index] = x;
        ys[index] = y;
    }
}
