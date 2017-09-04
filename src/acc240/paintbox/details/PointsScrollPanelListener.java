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
package acc240.paintbox.details;

import acc240.paintbox.Operations;
import acc240.paintbox.listener.SuperListener;
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
