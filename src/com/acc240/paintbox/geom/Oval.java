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

import com.acc240.paintbox.Resources;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Oval extends CornerShape {

    public Oval(int x, int y, Color col) {
        this(x, y, x, y, col, 1.0f);
    }

    public Oval(Point point, Color col) {
        this(point, point, col);
    }

    public Oval(Point point, Color col, float strokeWidth) {
        this(point, point, col, strokeWidth);
    }

    public Oval(Point point1, Point point2, Color col, float strokeWidth) {
        this((int) point1.getX(), (int) point1.getY(), (int) point1.getX(), (int) point1.getY(), col, strokeWidth);
    }

    public Oval(Point point1, Point point2, Color col) {
        this((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY(), col, 1.0f);
    }

    public Oval(int x1, int y1, int x2, int y2, Color col, float strokeWidth) {
        super("Oval", strokeWidth);
        borderColor = col;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        letterName = "o";
    }

    @Override
    public Shape copy() {
        Oval result = new Oval(x1, y1, x2, y2, borderColor, strokeWidth);
        result.filled = filled;
        result.filling = filling;
        result.ox = ox;
        result.oy = oy;
        result.op = op;
        Resources.addCopy(result, this);
        return result;
    }

    @Override
    public void draw(Graphics p) {
        Graphics2D page = (Graphics2D) p;
        page.setStroke(new BasicStroke(strokeWidth));

        if (filled && filling != null) {
            page.setColor(filling);
            if (x2 > x1 && y2 > y1) {
                page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
                page.fillOval(x1, y1, x2 - x1, y2 - y1);
                page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            } else if (x2 > x1) {
                page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
                page.fillOval(x1, y2, x2 - x1, y1 - y2);
                page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            } else if (y2 > y1) {
                page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
                page.fillOval(x2, y1, x1 - x2, y2 - y1);
                page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            } else {
                page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
                page.fillOval(x2, y2, x1 - x2, y1 - y2);
                page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            }
        }

        page.setColor(borderColor);
        if (x2 > x1 && y2 > y1) {
            page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            page.drawOval(x1, y1, x2 - x1, y2 - y1);
            page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
        } else if (x2 > x1) {
            page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            page.drawOval(x1, y2, x2 - x1, y1 - y2);
            page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
        } else if (y2 > y1) {
            page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            page.drawOval(x2, y1, x1 - x2, y2 - y1);
            page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
        } else {
            page.rotate(rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            page.drawOval(x2, y2, x1 - x2, y1 - y2);
            page.rotate(-1 * rotation, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
        }
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
}
