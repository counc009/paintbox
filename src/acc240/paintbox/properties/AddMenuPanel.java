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
package acc240.paintbox.properties;

import acc240.paintbox.Operations;
import acc240.paintbox.Properties;

import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AddMenuPanel {

    private Properties properties;

    private final AddMenuListener listener = new AddMenuListener(this);
    private JFrame frame;
    private ArrayList<JCheckBox> deletes;
    private JButton addNew;
    private JButton delete;

    public AddMenuPanel() {
        refresh();
    }

    public JFrame getFrame() {
        return frame;
    }

    public final void refresh() {
        if (frame != null) {
            frame.dispose();
        }
        frame = new JFrame();

        properties = Operations.properties;

        ArrayList<String> names = properties.getShapeNames();
        deletes = new ArrayList<>();

        for (String name : names) {
            deletes.add(new JCheckBox());
        }

        JPanel options = new JPanel();
        addNew = new JButton("Add New");
        addNew.addActionListener(listener);
        delete = new JButton("Delete Selected");
        delete.addActionListener(listener);
        options.add(addNew);
        options.add(delete);

        listener.setAdd(addNew);
        listener.setDelete(delete);
        listener.setDeletes(deletes);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int i = 0; i < names.size(); i++) {
            JPanel temp = new JPanel();
            temp.add(new JLabel(names.get(i)));
            temp.add(deletes.get(i));
            panel.add(temp);
        }

        panel.add(options);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(listener);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void add(String name, String path) {
        properties.addShape(name, path);
    }

    public void delete(int index) {
        properties.removeShape(properties.getShapeNames().get(index));
    }
}
