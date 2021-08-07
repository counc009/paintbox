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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Rectangle extends CornerShape {

    private boolean rounded;
    private int rndAmt;

    public Rectangle(int x, int y, Color col) {
        this(x, y, x, y, col, false, 0, 1.0f);
    }

    public Rectangle(Point point, Color col) {
        this(point, point, col, false, 0);
    }

    public Rectangle(Point point, Color col, float strokeWidth) {
        this(point, point, col, strokeWidth);
    }

    public Rectangle(Point point1, Point point2, Color col, float strokeWidth) {
        this((int) point1.getX(), (int) point1.getY(), (int) point1.getX(), (int) point1.getY(), col, false, 0, strokeWidth);
    }

    public Rectangle(Point point1, Point point2, Color col, boolean rounded, int rndAmt) {
        this((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY(), col, rounded, rndAmt, 1.0f);
    }

    public Rectangle(int x1, int y1, int x2, int y2, Color col, boolean rounded, int rndAmt, float strokeWidth) {
        super("Rectangle", strokeWidth);

        if (col != null) {
            borderColor = col;
        } else {
            borderColor = Operations.properties.getBorder();
        }
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        ox = x1;
        oy = y1;
        op = 1;
        letterName = "r";

        this.rounded = rounded;
        this.rndAmt = rndAmt;
    }

    public boolean getRounded() {
        return rounded;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }

    public int getRndAmt() {
        return rndAmt;
    }

    public void setRndAmt(int rndAmt) {
        this.rndAmt = rndAmt;
    }

    @Override
    public Shape copy() {
        Rectangle result = new Rectangle(x1, y1, x2, y2, borderColor, rounded, rndAmt, strokeWidth);
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
                fillSelf(page, x1, y1, x2 - x1, y2 - y1);
            } else if (x2 > x1) {
                fillSelf(page, x1, y2, x2 - x1, y1 - y2);
            } else if (y2 > y1) {
                fillSelf(page, x2, y1, x1 - x2, y2 - y1);
            } else {
                fillSelf(page, x2, y2, x1 - x2, y1 - y2);
            }
        }

        page.setColor(borderColor);
        if (x2 > x1 && y2 > y1) {
            drawSelf(page, x1, y1, x2 - x1, y2 - y1);
        } else if (x2 > x1) {
            drawSelf(page, x1, y2, x2 - x1, y1 - y2);
        } else if (y2 > y1) {
            drawSelf(page, x2, y1, x1 - x2, y2 - y1);
        } else {
            drawSelf(page, x2, y2, x1 - x2, y1 - y2);
        }
    }

    private void fillSelf(Graphics page, int x, int y, int length, int width) //length = x, width = y
    {
        if (rounded) {
            ((Graphics2D) page).rotate(rotation, x + length / 2.0, y + width / 2.0);
            page.fillRoundRect(x, y, length, width, rndAmt, rndAmt);
            ((Graphics2D) page).rotate(-1 * rotation, x + length / 2.0, y + width / 2.0);
        } else {
            ((Graphics2D) page).rotate(rotation, x + length / 2.0, y + width / 2.0);
            page.fillRect(x, y, length, width);
            ((Graphics2D) page).rotate(-1 * rotation, x + length / 2.0, y + width / 2.0);
        }
    }

    private void drawSelf(Graphics page, int x, int y, int length, int width) //length = x, width = y
    {
        if (rounded) {
            ((Graphics2D) page).rotate(rotation, x + length / 2.0, y + width / 2.0);
            page.drawRoundRect(x, y, length, width, rndAmt, rndAmt);
            ((Graphics2D) page).rotate(-1 * rotation, x + length / 2.0, y + width / 2.0);
        } else {
            ((Graphics2D) page).rotate(rotation, x + length / 2.0, y + width / 2.0);
            page.drawRect(x, y, length, width);
            ((Graphics2D) page).rotate(-1 * rotation, x + length / 2.0, y + width / 2.0);
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

    @Override
    public String toText() {
        String res = super.toText();
        if (rounded) {
            res += ":" + rndAmt;
        }
        return res;
    }

    @Override
    public String toGeneral() {
        String res = super.toGeneral();
        if (rounded) {
            res += ":" + rndAmt;
        }
        return res;
    }

    @Override
    public void fromText(String[] data) {
        super.fromText(data);
        rounded = data.length > 17;
        if (rounded) {
            rndAmt = Integer.parseInt(data[17]);
        } else {
            rndAmt = 0;
        }
    }

    @Override
    public void fromTextOld(String[] data) {
        super.fromTextOld(data);
        rounded = data.length > 16;
        if (rounded) {
            rndAmt = Integer.parseInt(data[16]);
        } else {
            rndAmt = 0;
        }
    }

    @Override
    public void fromGeneral(String[] data, Point point) {
        super.fromGeneral(data, point);
        rounded = data.length > 2;
        if (rounded) {
            rndAmt = Integer.parseInt(data[2]);
        } else {
            rndAmt = 0;
        }
    }
}
