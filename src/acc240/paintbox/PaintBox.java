/*----------------------------------------------------
 * PaintBox is a free open source painting program
 * Copyright (C) 2014 PaintBox Foundation
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
package acc240.paintbox;

import acc240.paintbox.canvas.Canvas;
import acc240.paintbox.canvas.CanvasPanel;
import acc240.paintbox.details.DetailPanel;
import acc240.paintbox.tools.ToolShelf;
import acc240.paintbox.tools.MenuBar;

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
