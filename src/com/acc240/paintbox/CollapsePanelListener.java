package com.acc240.paintbox;

import com.acc240.paintbox.listener.SuperListener;
import java.awt.event.ActionEvent;

public class CollapsePanelListener extends SuperListener {

    private final CollapsePanel panel;

    public CollapsePanelListener(CollapsePanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        panel.clicked();
    }
}
