package com.acc240.paintbox.details;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.geom.CornerShape;
import com.acc240.paintbox.geom.Picture;
import com.acc240.paintbox.geom.Shape;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ShapePanel extends JPanel {

    public static final int CornerShape = 0;
    public static final int Line = 1;
    public static final int Picture = 2;
    public static final int Poly = 3;
    public static final int Text = 4;

    private Shape shape;
    private JTextField rotation;

    public ShapePanel(int type, Shape caller) {
        reload(type, caller);
    }

    public final void reload(int type, Shape caller) {
        removeAll();
        ArrayList<ArrayList<JTextField>> fields;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ShapePanelListener listener = new ShapePanelListener(this, type);

        JPanel rot = new JPanel();
        rot.setLayout(new BoxLayout(rot, BoxLayout.X_AXIS));
        if (Operations.properties.useRadians()) {
            rot.add(new JLabel("Rotation (rad): "));
            rotation = new JTextField("" + caller.getRotation());
        } else {
            rot.add(new JLabel("Rotation (deg): "));
            rotation = new JTextField("" + Math.toDegrees(caller.getRotation()));
        }
        rotation.addActionListener(listener);
        rotation.setMaximumSize(new Dimension(50, 20));
        listener.setRotation(rotation);
        rot.add(rotation);
        add(rot);

        switch (type) {
            case CornerShape:
                fields = new ArrayList<>();
                fields.add(new ArrayList<>());
                fields.add(new ArrayList<>());

                JPanel corner = new JPanel();
                corner.setLayout(new BoxLayout(corner, BoxLayout.X_AXIS));
                corner.add(new JLabel("First Corner: ("));
                fields.get(0).add(new JTextField("" + ((com.acc240.paintbox.geom.CornerShape) caller).getTopCorner().x));
                fields.get(0).get(0).addActionListener(listener);
                fields.get(0).get(0).setMaximumSize(new Dimension(50, 20));
                fields.get(0).add(new JTextField("" + ((CornerShape) caller).getTopCorner().y));
                fields.get(0).get(1).addActionListener(listener);
                fields.get(0).get(1).setMaximumSize(new Dimension(50, 20));
                corner.add(fields.get(0).get(0));
                corner.add(new JLabel(", "));
                corner.add(fields.get(0).get(1));
                corner.add(new JLabel(")"));
                add(corner);

                JPanel width = new JPanel();
                width.setLayout(new BoxLayout(width, BoxLayout.X_AXIS));
                width.add(new JLabel("Width: "));
                fields.get(1).add(new JTextField("" + ((CornerShape) caller).getWidth()));
                fields.get(1).get(0).addActionListener(listener);
                fields.get(1).get(0).setMaximumSize(new Dimension(50, 20));
                width.add(fields.get(1).get(0));
                add(width);

                JPanel height = new JPanel();
                height.setLayout(new BoxLayout(height, BoxLayout.X_AXIS));
                height.add(new JLabel("Height: "));
                fields.get(1).add(new JTextField("" + ((CornerShape) caller).getHeight()));
                fields.get(1).get(1).addActionListener(listener);
                fields.get(1).get(1).setMaximumSize(new Dimension(50, 20));
                height.add(fields.get(1).get(1));
                add(height);
                add(Box.createVerticalGlue());

                listener.setPoints(fields);
                break;
            case Line:
                fields = new ArrayList<>();
                fields.add(new ArrayList<>());
                fields.add(new ArrayList<>());

                JPanel pointOne = new JPanel();
                pointOne.setLayout(new BoxLayout(pointOne, BoxLayout.X_AXIS));
                pointOne.add(new JLabel("First Point: ("));
                fields.get(0).add(new JTextField("" + caller.getPoints()[0][0]));
                fields.get(0).get(0).addActionListener(listener);
                fields.get(0).get(0).setMaximumSize(new Dimension(50, 20));
                fields.get(0).add(new JTextField("" + caller.getPoints()[1][0]));
                fields.get(0).get(1).addActionListener(listener);
                fields.get(0).get(1).setMaximumSize(new Dimension(50, 20));
                pointOne.add(fields.get(0).get(0));
                pointOne.add(new JLabel(", "));
                pointOne.add(fields.get(0).get(1));
                pointOne.add(new JLabel(")"));
                add(pointOne);

                JPanel pointTwo = new JPanel();
                pointTwo.setLayout(new BoxLayout(pointTwo, BoxLayout.X_AXIS));
                pointTwo.add(new JLabel("Second Point: ("));
                fields.get(1).add(new JTextField("" + caller.getPoints()[0][1]));
                fields.get(1).get(0).addActionListener(listener);
                fields.get(1).get(0).setMaximumSize(new Dimension(50, 20));
                fields.get(1).add(new JTextField("" + caller.getPoints()[1][1]));
                fields.get(1).get(1).addActionListener(listener);
                fields.get(1).get(1).setMaximumSize(new Dimension(50, 20));
                pointTwo.add(fields.get(1).get(0));
                pointTwo.add(new JLabel(", "));
                pointTwo.add(fields.get(1).get(1));
                pointTwo.add(new JLabel(")"));
                add(pointTwo);

                listener.setPoints(fields);
                break;
            case Picture:
                fields = new ArrayList<>();
                fields.add(new ArrayList<>());

                JPanel source = new JPanel();
                source.setLayout(new BoxLayout(source, BoxLayout.X_AXIS));
                JButton button = new JButton("Select Image");
                button.addActionListener(listener);
                source.add(button);
                add(source);

                JPanel widthPanel = new JPanel();
                widthPanel.setLayout(new BoxLayout(widthPanel, BoxLayout.X_AXIS));
                widthPanel.add(new JLabel("Image Width: "));
                fields.get(0).add(new JTextField("" + ((com.acc240.paintbox.geom.Picture) caller).getWidth()));
                fields.get(0).get(0).addActionListener(listener);
                fields.get(0).get(0).setMaximumSize(new Dimension(50, 20));
                widthPanel.add(fields.get(0).get(0));
                add(widthPanel);

                JPanel heightPanel = new JPanel();
                heightPanel.setLayout(new BoxLayout(heightPanel, BoxLayout.X_AXIS));
                heightPanel.add(new JLabel("Image Height: "));
                fields.get(0).add(new JTextField("" + ((Picture) caller).getHeight()));
                fields.get(0).get(1).addActionListener(listener);
                fields.get(0).get(1).setMaximumSize(new Dimension(50, 20));
                heightPanel.add(fields.get(0).get(1));
                add(heightPanel);

                listener.setPoints(fields);
                break;
            case Poly:
                fields = new ArrayList<>();
                fields.add(new ArrayList<>());
                fields.add(new ArrayList<>());
                fields.add(new ArrayList<>());

                ArrayList<JPanel> panels = new ArrayList<>();
                String[] points = new String[caller.getPoints()[0].length];
                for (int i = 0; i < caller.getPoints()[0].length; i++) {
                    points[i] = "" + (i + 1);
                    JPanel pointPanel = new JPanel();
                    pointPanel.setLayout(new BoxLayout(pointPanel, BoxLayout.X_AXIS));
                    pointPanel.add(new JLabel("Point " + (i + 1) + ": ("));

                    JTextField x = new JTextField("" + caller.getPoints()[0][i]);
                    x.addActionListener(listener);
                    x.setMaximumSize(new Dimension(50, 20));
                    pointPanel.add(x);
                    fields.get(0).add(x);

                    pointPanel.add(new JLabel(", "));

                    JTextField y = new JTextField("" + caller.getPoints()[1][i]);
                    y.addActionListener(listener);
                    y.setMaximumSize(new Dimension(50, 20));
                    pointPanel.add(y);
                    fields.get(1).add(y);

                    pointPanel.add(new JLabel(")"));
                    panels.add(pointPanel);
                }

                JPanel changePanel = new JPanel();
                changePanel.setLayout(new BoxLayout(changePanel, BoxLayout.Y_AXIS));

                ArrayList<JButton> buttons = new ArrayList<>();
                ArrayList<JComboBox> combos = new ArrayList<>();

                JButton add = new JButton("Add Point At End");
                add.addActionListener(listener);
                buttons.add(add);
                changePanel.add(add);

                JPanel removePanel = new JPanel();
                removePanel.setLayout(new BoxLayout(removePanel, BoxLayout.X_AXIS));

                JButton remove = new JButton("Remove Point");
                remove.addActionListener(listener);
                buttons.add(remove);
                removePanel.add(remove);
                JComboBox removeIndex = new JComboBox(points);
                combos.add(removeIndex);
                removePanel.add(removeIndex);
                changePanel.add(removePanel);

                JPanel insertPanel = new JPanel();
                insertPanel.setLayout(new BoxLayout(insertPanel, BoxLayout.X_AXIS));

                JButton insert = new JButton("Insert Point Before Point");
                insert.addActionListener(listener);
                buttons.add(insert);
                insertPanel.add(insert);
                JComboBox insertIndex = new JComboBox(points);
                combos.add(insertIndex);
                insertPanel.add(insertIndex);
                changePanel.add(insertPanel);

                JPanel randomizePanel = new JPanel();
                randomizePanel.setLayout(new BoxLayout(randomizePanel, BoxLayout.X_AXIS));

                JButton randomize = new JButton("Randomize By Maximum Of");
                randomize.addActionListener(listener);
                buttons.add(randomize);
                randomizePanel.add(randomize);
                JTextField randomizeAmt = new JTextField("5");
                randomizeAmt.setMaximumSize(new Dimension(50, 20));
                fields.get(2).add(randomizeAmt);
                randomizePanel.add(randomizeAmt);
                randomizePanel.add(new JLabel("Pixels"));
                changePanel.add(randomizePanel);

                add(new PointsScrollPanel(panels));
                add(Box.createVerticalGlue());
                add(changePanel);

                listener.setPoints(fields);
                listener.setButtons(buttons);
                listener.setCombos(combos);
                break;
            case Text:
                fields = new ArrayList<>();
                fields.add(new ArrayList<>());

                add(new FontPanel());

                JPanel text = new JPanel();
                text.setLayout(new BoxLayout(text, BoxLayout.X_AXIS));
                text.add(new JLabel("Text: "));
                fields.get(0).add(new JTextField("" + ((com.acc240.paintbox.geom.Text) caller).getText()));
                fields.get(0).get(0).addActionListener(listener);
                fields.get(0).get(0).setMaximumSize(new Dimension(50, 20));
                text.add(fields.get(0).get(0));
                add(text);

                listener.setPoints(fields);
                break;
        }
    }
}
