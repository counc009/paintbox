package com.acc240.paintbox.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

public abstract class Shape {

    protected Color borderColor;
    protected String letterName;
    protected String name;
    protected float strokeWidth;
    protected double rotation;

    public Shape(String name) {
        this(name, 1.0f);
    }

    public Shape(String name, float strokeWidth) {
        this.name = name;
        this.strokeWidth = strokeWidth;
        this.rotation = 0;
    }

    public abstract void draw(Graphics page);

    public abstract void drawBox(Graphics page, Color color);

    public abstract void move(int x, int y);

    public abstract void scale(double scaleX, double scaleY);

    public abstract void center();

    public abstract Point getCenter();

    public abstract Shape copy();

    public abstract int[][] getPoints();

    public abstract void moveTo(int[][] points);

    public final void drawBox(Graphics page) {
        drawBox(page, Color.black);
    }

    public final Color getColor() {
        return borderColor;
    }

    public final void setColor(Color color) {
        borderColor = color;
    }

    public final float getStrokeWidth() {
        return strokeWidth;
    }

    public final void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public abstract boolean inBox(Point spot);

    public abstract String toText();

    public abstract String toGeneral();

    public abstract void fromText(String[] data);

    public abstract void fromTextOld(String[] data);

    public abstract void fromGeneral(String[] data, Point point);

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public abstract JPanel getPointPanel();
}
