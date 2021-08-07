package com.acc240.paintbox.details;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.listener.SuperListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

class PointsScrollPanelListener extends SuperListener {

    private final PointsScrollPanel panel;
    private final JScrollBar scroll;

    public PointsScrollPanelListener(PointsScrollPanel panel, JScrollBar scroll) {
        this.panel = panel;
        this.scroll = scroll;
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int value = scroll.getValue();

        if (panel.getContent().size() > value + 9) {
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            for (int i = value; i < value + 10; i++) {
                content.add(panel.getContent().get(i));
            }
            panel.setContentPanel(content);

            Operations.getFrame().revalidate();
            Operations.getFrame().repaint();
        }
    }

}
