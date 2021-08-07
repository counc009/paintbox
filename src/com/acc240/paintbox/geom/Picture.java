package com.acc240.paintbox.geom;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.details.ShapePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Picture extends Shape {

    private String image;
    private int x1, y1;
    private int width, height;
    private ImageIcon img;
    private Image scaled;

    public Picture(Point point, String image) {
        this((int) point.getX(), (int) point.getY(), image);
    }

    public Picture(int x, int y, String image) {
        super("Image", 1.0f);
        letterName = "i";
        this.x1 = x;
        this.y1 = y;
        this.image = image;
        img = new ImageIcon(image);
        width = img.getIconWidth();
        height = img.getIconHeight();
        scaled = img.getImage();
    }

    public Picture(int x, int y, int width, int height, String image) {
        super("Image", 1.0f);
        letterName = "i";
        this.x1 = x;
        this.y1 = y;
        this.image = image;
        img = new ImageIcon(image);
        this.width = width;
        this.height = height;
        scaled = img.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        img = new ImageIcon(image);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        scaled = img.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        scaled = img.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    }

    @Override
    public Point getCenter() {
        return new Point(x1 + width / 2, y1 + height / 2);
    }

    @Override
    public void draw(Graphics page) {
        ((Graphics2D) page).rotate(rotation, x1 + width / 2.0, y1 + height / 2.0);
        page.drawImage(scaled, x1, y1, width, height, null);
        ((Graphics2D) page).rotate(-1 * rotation, x1 + width / 2.0, y1 + height / 2.0);
    }

    @Override
    public void drawBox(Graphics page, Color color) {
        page.drawImage(scaled, x1, y1, width, height, null);

        page.setColor(color);
        double l = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double cx = (2.0 * x1 + width) / 2.0;
        double cy = (2.0 * y1 + height) / 2.0;
        page.drawOval((int) (cx - (l * 0.5)), (int) (cy - (l * 0.5)), (int) l, (int) l);
    }

    @Override
    public Shape copy() {
        return new Picture(x1, y1, image);
    }

    @Override
    public boolean inBox(Point spot) {
        int x = (int) spot.getX(), y = (int) spot.getY();
        double l = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double cx = (2.0 * x1 + width) / 2.0;
        double cy = (2.0 * y1 + height) / 2.0;
        return Math.abs(Math.sqrt(Math.pow(cx - x, 2) + Math.pow(cy - y, 2))) <= l / 2.0;
    }

    @Override
    public void move(int x, int y) {
        x1 += x;
        y1 += y;
    }

    @Override
    public void scale(double scaleX, double scaleY) {
        x1 *= scaleX;
        y1 *= scaleY;
        width *= scaleX;
        height *= scaleY;
    }

    @Override
    public int[][] getPoints() {
        return new int[][]{{x1, x1}, {y1, y1}};
    }

    @Override
    public void moveTo(int[][] points) {
        x1 = points[0][0];
        y1 = points[1][0];
    }

    @Override
    public String toText() {
        return letterName + ":" + x1 + ":" + y1 + ":" + rotation + ":" + image;
    }

    @Override
    public String toGeneral() {
        return letterName + ":" + image;
    }

    @Override
    public void fromText(String[] data) {
        x1 = Integer.parseInt(data[0]);
        y1 = Integer.parseInt(data[1]);
        rotation = Double.parseDouble(data[2]);
        image = data[3];
        int i = 4;
        while (data.length > i) {
            image += ":" + data[i];
            i++;
        }
        img = new ImageIcon(image);
    }

    @Override
    public void fromTextOld(String[] data) {
        x1 = Integer.parseInt(data[0]);
        y1 = Integer.parseInt(data[1]);
        image = data[2];
        int i = 3;
        while (data.length > i) {
            image += ":" + data[i];
            i++;
        }
        img = new ImageIcon(image);
    }

    @Override
    public void fromGeneral(String[] data, Point point) {
        x1 = (int) point.getX();
        y1 = (int) point.getY();
        image = data[0];
    }

    @Override
    public void center() {
        int mx = (x1 + x1 + img.getIconWidth()) / 2;
        int my = (y1 + y1 + img.getIconHeight()) / 2;
        int dx = (Operations.getCanvas().getWidth() / 2) - mx;
        int dy = (Operations.getCanvas().getHeight() / 2) - my;
        move(dx, dy);
        Operations.setChanged();
        Operations.update();
    }

    @Override
    public JPanel getPointPanel() {
        return new ShapePanel(ShapePanel.Picture, this);
    }
}
