package com.acc240.paintbox.canvas;

import com.acc240.paintbox.CollapsePanel;
import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Properties;
import com.acc240.paintbox.tools.ToolShelf;
import com.acc240.paintbox.details.DetailPanel;

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
