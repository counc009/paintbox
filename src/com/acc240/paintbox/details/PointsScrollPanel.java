package com.acc240.paintbox.details;

import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class PointsScrollPanel extends JPanel {

    private final ArrayList<JPanel> content;
    private JPanel contentPanel;
    private final JScrollBar scroll;

    public PointsScrollPanel(ArrayList<JPanel> content) {
        this.content = content;

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < Math.min(10, content.size()); i++) {
            contentPanel.add(content.get(i));
        }

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(contentPanel);
        if (content.size() > 10) {
            scroll = new JScrollBar(JScrollBar.VERTICAL, 0, 10, 0, content.size());
            scroll.addAdjustmentListener(new PointsScrollPanelListener(this, scroll));
            add(scroll);
        } else {
            scroll = new JScrollBar(JScrollBar.VERTICAL, 0, content.size(), 0, content.size());
            scroll.addAdjustmentListener(new PointsScrollPanelListener(this, scroll));
            add(scroll);
        }
    }

    public ArrayList<JPanel> getContent() {
        return content;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        removeAll();
        add(contentPanel);
        add(scroll);
        repaint();
    }
}
