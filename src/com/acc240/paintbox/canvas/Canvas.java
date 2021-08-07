package com.acc240.paintbox.canvas;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.Shape;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JPanel;

public class Canvas extends JPanel {

    public Canvas() {
        setBackground(Operations.properties.getBackground());
        setPreferredSize(Operations.properties.getDefaultSize());

        CanvasListener listener = new CanvasListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    @Override
    public void paintComponent(Graphics page) {
        super.paintComponent(page);

        if (Operations.getMode() != Operations.SELECT) {
            for (Shape shape : Operations.getShapes()) {
                shape.draw(page);
            }
        } else {
            for (Shape shape : Operations.getShapes()) {
                if (Operations.getSelected() != shape) {
                    shape.drawBox(page);
                } else {
                    shape.drawBox(page, Color.red);
                }
            }
        }
    }

    public void exportPNG(File file) {
        Dimension size = getSize();
        BufferedImage image = (BufferedImage) createImage(size.width, size.height);
        Graphics g = image.getGraphics();
        paint(g);
        g.dispose();
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
        }
    }

    public void exportJPG(File file) {
        Dimension size = getSize();
        BufferedImage image = (BufferedImage) createImage(size.width, size.height);
        Graphics g = image.getGraphics();
        paint(g);
        g.dispose();
        try {
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
        }
    }

    public void exportGIF(File file) {
        Dimension size = getSize();
        BufferedImage image = (BufferedImage) createImage(size.width, size.height);
        Graphics g = image.getGraphics();
        paint(g);
        g.dispose();
        try {
            ImageIO.write(image, "gif", file);
        } catch (IOException e) {
        }
    }

    public void print() {
        try {
            Dimension size = getSize();
            BufferedImage image = (BufferedImage) createImage(size.width, size.height);
            Graphics g = image.getGraphics();
            paint(g);
            g.dispose();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", output);
            byte[] data = output.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            PrintRequestAttributeSet request = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
            PrinterJob job = PrinterJob.getPrinterJob();
            boolean state = job.printDialog(request);
            if (state) {
                PrintService service = job.getPrintService();
                DocPrintJob printJob = service.createPrintJob();
                DocAttributeSet attributeSet = new HashDocAttributeSet();
                Doc document = new SimpleDoc(input, flavor, attributeSet);
                printJob.print(document, request);
            }
        } catch (IOException | PrintException ex) {
        }
    }
}
