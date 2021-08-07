package com.acc240.paintbox.details;

import com.acc240.paintbox.Operations;
import com.acc240.paintbox.Resources;
import com.acc240.paintbox.geom.CornerShape;
import com.acc240.paintbox.geom.Line;
import com.acc240.paintbox.geom.Poly;
import com.acc240.paintbox.geom.Text;
import com.acc240.paintbox.listener.SuperListener;
import com.acc240.paintbox.geom.Picture;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ShapePanelListener extends SuperListener {

    private ArrayList<ArrayList<JTextField>> points;
    private ArrayList<JButton> buttons;
    private ArrayList<JComboBox> combos;
    private final int mode;
    private final ShapePanel panel;
    private JTextField rotation;

    public ShapePanelListener(ShapePanel panel, int mode) {
        this.panel = panel;
        this.mode = mode;
    }

    public void setPoints(ArrayList<ArrayList<JTextField>> points) {
        this.points = points;
    }

    public void setRotation(JTextField rotation) {
        this.rotation = rotation;
    }

    public void setButtons(ArrayList<JButton> buttons) {
        this.buttons = buttons;
    }

    public void setCombos(ArrayList<JComboBox> combos) {
        this.combos = combos;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JTextField) {
            if (event.getSource() == rotation) {
                if (Operations.properties.useRadians()) {
                    Operations.getSelected().setRotation(Double.parseDouble(rotation.getText()));
                } else {
                    Operations.getSelected().setRotation(Math.toRadians(Double.parseDouble(rotation.getText())));
                }
                Operations.setChanged();
                Operations.update();
            } else {
                int r = -1, c = -1;
                for (int i = 0; i < points.size(); i++) {
                    if (points.get(i).indexOf(event.getSource()) != -1) {
                        r = i;
                        c = points.get(i).indexOf(event.getSource());
                    }
                }
                if (r == -1 || c == -1) {
                    throw (new RuntimeException("Unrecognized action performed"));
                }
                switch (mode) {
                    case ShapePanel.CornerShape:
                        if (r == 0) {
                            ((CornerShape) Operations.getSelected()).setTopCorner(new Point(Integer.parseInt(points.get(0).get(0).getText()), Integer.parseInt(points.get(0).get(1).getText())));
                        } else if (r == 1) {
                            if (c == 0) {
                                ((CornerShape) Operations.getSelected()).setWidth(Integer.parseInt(points.get(1).get(0).getText()));
                            } else if (c == 1) {
                                ((CornerShape) Operations.getSelected()).setHeight(Integer.parseInt(points.get(1).get(1).getText()));
                            }
                        }
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case ShapePanel.Line:
                        if (r == 0) {
                            ((Line) Operations.getSelected()).setStart(Integer.parseInt(points.get(0).get(0).getText()), Integer.parseInt(points.get(0).get(1).getText()));
                        } else if (r == 1) {
                            ((Line) Operations.getSelected()).setEnd(Integer.parseInt(points.get(1).get(0).getText()), Integer.parseInt(points.get(1).get(1).getText()));
                        }
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case ShapePanel.Picture:
                        if (c == 0) {
                            ((Picture) Operations.getSelected()).setWidth(Integer.parseInt(points.get(0).get(0).getText()));
                        } else if (c == 1) {
                            ((Picture) Operations.getSelected()).setHeight(Integer.parseInt(points.get(0).get(1).getText()));
                        }
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case ShapePanel.Poly:
                        ((Poly) Operations.getSelected()).setPoint(c, Integer.parseInt(points.get(0).get(c).getText()), Integer.parseInt(points.get(1).get(c).getText()));
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case ShapePanel.Text:
                        ((Text) Operations.getSelected()).setText(points.get(0).get(0).getText());
                        Operations.setChanged();
                        Operations.update();
                        break;
                }
            }
        } else {
            if (event.getSource() instanceof JButton && mode == ShapePanel.Picture) {
                ((Picture) Operations.getSelected()).setImage(Resources.getFile("Select Image", "Image Files", "png", "jpg", "gif").getAbsolutePath());
                Operations.setChanged();
                Operations.update();
            } else if (event.getSource() instanceof JButton && mode == ShapePanel.Poly) {
                switch (buttons.indexOf(event.getSource())) {
                    case 0:
                        ((Poly) Operations.getSelected()).addPoint(Operations.getSelected().getPoints()[0][Operations.getSelected().getPoints()[0].length - 1],
                                Operations.getSelected().getPoints()[1][Operations.getSelected().getPoints()[0].length - 1]);
                        panel.reload(mode, Operations.getSelected());
                        Operations.getDetailPanel().repaint();
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case 1:
                        ((Poly) Operations.getSelected()).delete(combos.get(0).getSelectedIndex());
                        panel.reload(mode, Operations.getSelected());
                        Operations.getDetailPanel().repaint();
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case 2:
                        ((Poly) Operations.getSelected()).insert(combos.get(1).getSelectedIndex(),
                                Operations.getSelected().getPoints()[0][combos.get(1).getSelectedIndex()],
                                Operations.getSelected().getPoints()[1][combos.get(1).getSelectedIndex()]);
                        panel.reload(mode, Operations.getSelected());
                        Operations.getDetailPanel().repaint();
                        Operations.setChanged();
                        Operations.update();
                        break;
                    case 3:
                        ((Poly) Operations.getSelected()).randomize(Integer.parseInt(points.get(2).get(0).getText()));
                        panel.reload(mode, Operations.getSelected());
                        Operations.getDetailPanel().repaint();
                        Operations.setChanged();
                        Operations.update();
                }
            }
        }
    }
}
