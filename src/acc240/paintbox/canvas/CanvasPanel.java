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
package acc240.paintbox.canvas;

import acc240.paintbox.CollapsePanel;
import acc240.paintbox.Operations;
import acc240.paintbox.Properties;
import acc240.paintbox.tools.ToolShelf;
import acc240.paintbox.details.DetailPanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class CanvasPanel extends JPanel {

    private final Canvas canvas;
    private final ToolShelf tools;
    private final DetailPanel details;
    private final CollapsePanel collapse;
    private final JPanel subpanel;

    public CanvasPanel(Canvas canvas, ToolShelf tools, DetailPanel details) {
        this.canvas = canvas;
        this.tools = tools;
        this.details = details;

        subpanel = new JPanel();
        if (Operations.properties.getDetailsLocation() == Properties.DETAILS_LEFT) {
            subpanel.add(details);
            subpanel.add(canvas);
        } else {
            subpanel.add(canvas);
            subpanel.add(details);
        }

        collapse = new CollapsePanel(canvas, details, subpanel, false, Operations.properties.getDetailsLocation());
        Operations.setDetailCollapse(collapse);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        if (Operations.properties.getToolsLocation() == Properties.TOOLS_TOP) {
            add(tools);
            add(subpanel);
        } else {
            add(subpanel);
            add(tools);
        }
    }

    public void update() {
        removeAll();
        if (Operations.properties.getToolsLocation() == Properties.TOOLS_TOP) {
            add(tools);
            add(subpanel);
        } else {
            add(subpanel);
            add(tools);
        }
    }
}
