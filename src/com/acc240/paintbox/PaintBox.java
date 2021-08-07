package com.acc240.paintbox;

import com.acc240.paintbox.canvas.Canvas;
import com.acc240.paintbox.canvas.CanvasPanel;
import com.acc240.paintbox.details.DetailPanel;
import com.acc240.paintbox.tools.ToolShelf;
import com.acc240.paintbox.tools.MenuBar;

import javax.swing.*;

public class PaintBox {

    private static JFrame frame;
    private static final String version = "PaintBox 1.11";

    public static void main(String[] args) {
        Operations.setup();

        frame = new JFrame(version + " (untitled)");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        PaintBoxListener listener = new PaintBoxListener(frame);
        frame.addWindowListener(listener);

        MenuBar menu = new MenuBar();
        ToolShelf tools = new ToolShelf(panel);
        Canvas canvas = new Canvas();
        DetailPanel detailPanel = new DetailPanel();

        Operations.setFrame(frame);
        Operations.setCanvas(canvas);
        Operations.setTools(tools);
        Operations.setMenu(menu);
        Operations.setDetailPanel(detailPanel);

        panel.add(menu);
        CanvasPanel canvasPanel = new CanvasPanel(canvas, tools, detailPanel);
        panel.add(canvasPanel);

        Operations.setCanvasPanel(canvasPanel);
        Operations.splash();

        frame.getRootPane().setDefaultButton(tools.getDefaultButton());
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        if (args.length > 0 && args[0].contains(".drw")) {
            Operations.open(new java.io.File(args[0]));
        }
    }

    public static void setName(String fileName) {
        if (!fileName.equals("")) {
            if (fileName.charAt(0) != ' ') {
                frame.setTitle(version + ": " + fileName);
            } else {
                frame.setTitle(version + " " + fileName);
            }
            if (Operations.isChanged()) {
                frame.setTitle(frame.getTitle() + "*");
            }
        } else {
            frame.setTitle(version + " (untitled)");
            if (Operations.isChanged()) {
                frame.setTitle(frame.getTitle() + "*");
            }
        }
    }
}
