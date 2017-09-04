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

import acc240.paintbox.Operations;
import acc240.paintbox.listener.SuperListener;
import acc240.paintbox.geom.CornerShape;
import acc240.paintbox.geom.Line;
import acc240.paintbox.geom.Oval;
import acc240.paintbox.geom.Picture;
import acc240.paintbox.geom.Polygon;
import acc240.paintbox.geom.Polyline;
import acc240.paintbox.geom.Rectangle;
import acc240.paintbox.geom.Shape;
import acc240.paintbox.geom.Text;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.SwingUtilities;

public class CanvasListener extends SuperListener {

    private static Shape presentShape;

    @Override
    public void mouseClicked(MouseEvent event) {
        switch (Operations.getMode()) {
            case Operations.SELECT:
                if (Operations.getMove() == Operations.NO_MOVE && Operations.getRotation() == Operations.NO_ROTATE) {
                    SelectMode.click(event);
                } else if(Operations.getRotation()!= Operations.NO_ROTATE) {
                    RotateMode.click(event);
                } else {
                    MoveMode.click(event);
                }
                break;
            case Operations.RECTANGLE:
                RectangleMode.click(event);
                break;
            case Operations.SQUARE:
                SquareMode.click(event);
                break;
            case Operations.OVAL:
                OvalMode.click(event);
                break;
            case Operations.CIRCLE:
                CircleMode.click(event);
                break;
            case Operations.TRIANGLE:
                TriangleMode.click(event);
                break;
            case Operations.LINE:
                LineMode.click(event);
                break;
            case Operations.POLYLINE:
                PolylineMode.click(event);
                break;
            case Operations.POLYGON:
                PolygonMode.click(event);
                break;
            case Operations.TEXT:
                TextMode.click(event);
                break;
            case Operations.PICTURE:
                PictureMode.click(event);
                break;
            case Operations.IMPORT:
                ImportMode.click(event);
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        switch (Operations.getMode()) {
            case Operations.SELECT:
                if (Operations.getMove() == Operations.MOVING) {
                    MoveMode.moved(event);
                } else if (Operations.getRotation() == Operations.ROTATING) {
                    RotateMode.moved(event);
                }
                break;
            case Operations.RECTANGLE:
                RectangleMode.moved(event);
                break;
            case Operations.SQUARE:
                SquareMode.moved(event);
                break;
            case Operations.OVAL:
                OvalMode.moved(event);
                break;
            case Operations.CIRCLE:
                CircleMode.moved(event);
                break;
            case Operations.TRIANGLE:
                TriangleMode.moved(event);
                break;
            case Operations.LINE:
                LineMode.moved(event);
                break;
            case Operations.POLYLINE:
                PolylineMode.moved(event);
                break;
            case Operations.POLYGON:
                PolygonMode.moved(event);
                break;
        }
    }

    private static class SelectMode {

        public static void click(MouseEvent event) {
            for (int i = Operations.getShapes().size() - 1; i >= 0; i--) {
                if (Operations.getShapes().get(i).inBox(event.getPoint())) {
                    if (Operations.getSelected() != Operations.getShapes().get(i)) {
                        Operations.setSelected(Operations.getShapes().get(i));
                    } else {
                        Operations.setSelected(null);
                    }
                    Operations.update();
                    break;
                }
            }
        }
    }

    private static class MoveMode {

        private static int ix, iy;

        public static void click(MouseEvent event) {
            if (Operations.getMove() == Operations.CAN_MOVE) {
                ix = (int) event.getPoint().getX();
                iy = (int) event.getPoint().getY();
                Operations.setMoving();
                Operations.setChanged();
            } else {
                ix = -1;
                iy = -1;
                Operations.setNoMove();
            }
        }

        private static void moved(MouseEvent event) {
            int tx = (int) event.getPoint().getX() - ix;
            int ty = (int) event.getPoint().getY() - iy;
            Operations.getSelected().move(tx, ty);
            ix = (int) event.getPoint().getX();
            iy = (int) event.getPoint().getY();
            Operations.update();
        }
    }
    
    private static class RotateMode {
        
        private static double ia;
        
        public static void click(MouseEvent event) {
            if (Operations.getRotation() == Operations.CAN_ROTATE) {
                ia = getAngle((int) event.getPoint().getX(), (int) event.getPoint().getY(), (int)Operations.getSelected().getCenter().getX(), (int)Operations.getSelected().getCenter().getY());
                Operations.setRotating();
                Operations.setChanged();
            } else {
                ia = 0;
                Operations.setNoRot();
            }
        }
        
        public static void moved(MouseEvent event) {
            double na = getAngle((int) event.getPoint().getX(), (int) event.getPoint().getY(), (int)Operations.getSelected().getCenter().getX(), (int)Operations.getSelected().getCenter().getY());
            Operations.getSelected().setRotation(ia - na);
            Operations.update();
        }
        
        private static double getAngle(int x, int y, int cx, int cy) {
            double res = Math.atan(((double)(y - cy)) / (cx - x));
            if(x >= cx && y <= cy) {
                return res;
            } else if(x < cx) {
                return Math.PI + res;
            } else {
              return 2 * Math.PI + res;
            }
        }
    }

    private static class RectangleMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Rectangle(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else {
                presentShape = null;
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                ((CornerShape) presentShape).setBottomCorner(event.getPoint());
                Operations.update();
            }
        }
    }

    private static class SquareMode {

        private static Point center;

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Rectangle(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                center = event.getPoint();
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else {
                presentShape = null;
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                Point now = event.getPoint();
                int rad = Math.max(Math.abs(now.x - center.x), Math.abs(now.y - center.y));
                ((CornerShape) presentShape).setTopCorner(new Point(center.x - rad, center.y - rad));
                ((CornerShape) presentShape).setBottomCorner(new Point(center.x + rad, center.y + rad));
                Operations.update();
            }
        }
    }

    private static class OvalMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Oval(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else {
                presentShape = null;
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                ((CornerShape) presentShape).setBottomCorner(event.getPoint());
                Operations.update();
            }
        }
    }

    private static class CircleMode {

        private static Point center;

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Oval(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                center = event.getPoint();
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else {
                presentShape = null;
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                Point now = event.getPoint();
                int rad = (int) Math.sqrt(Math.pow(now.x - center.x, 2) + Math.pow(now.y - center.y, 2));
                ((CornerShape) presentShape).setTopCorner(new Point(center.x - rad, center.y - rad));
                ((CornerShape) presentShape).setBottomCorner(new Point(center.x + rad, center.y + rad));
                Operations.update();
            }
        }
    }
    
    private static class TriangleMode {
        
        private static Point center;
        
        public static void click(MouseEvent event) {
            if (presentShape == null) {
                center = event.getPoint();
                presentShape = new Polygon(new int[]{center.x, center.x, center.x}, new int[]{center.y, center.y, center.y}, Operations.getBorder(), Operations.getStrokeWidth());
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else {
                presentShape = null;
            }
        }
        
        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                Point now = event.getPoint();
                double d = Math.sqrt(Math.pow(center.x - now.x, 2) + Math.pow(center.y - now.y, 2));
                
                
                ((Polygon)presentShape).setPoint(0, center.x, (int) (center.y - d));
                ((Polygon)presentShape).setPoint(1, center.x + (int)(0.5 * Math.sqrt(3) * d), center.y + (int)(0.5 * d));
                ((Polygon)presentShape).setPoint(2, center.x - (int)(0.5 * Math.sqrt(3) * d), center.y + (int)(0.5 * d));
                Operations.update();
            }
        }
    }

    private static class LineMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Line(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else {
                presentShape = null;
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                ((Line) presentShape).setEnd(event.getPoint());
                Operations.update();
            }
        }
    }

    private static class PolylineMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Polyline(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else if (SwingUtilities.isLeftMouseButton(event)) {
                ((Polyline) presentShape).addPoint(event.getPoint());
            } else {
                ((Polyline) presentShape).removeEnd();
                presentShape = null;
                Operations.update();
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                ((Polyline) presentShape).setEnd(event.getPoint());
                Operations.update();
            }
        }
    }

    private static class PolygonMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Polygon(event.getPoint(), Operations.getBorder(), Operations.getStrokeWidth());
                Operations.addShape(presentShape);
                Operations.setChanged();
                Operations.update();
            } else if (SwingUtilities.isLeftMouseButton(event)) {
                ((Polygon) presentShape).addPoint(event.getPoint());
            } else {
                ((Polygon) presentShape).removeEnd();
                presentShape = null;
                Operations.update();
            }
        }

        public static void moved(MouseEvent event) {
            if (presentShape != null) {
                ((Polygon) presentShape).setEnd(event.getPoint());
                Operations.update();
            }
        }
    }

    private static class TextMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Text(Operations.getTempText(), event.getPoint(), Operations.getBorder());
                Operations.addShape(presentShape);
                presentShape = null;
                Operations.update();
                Operations.setMode(Operations.getLastMode());
            }
        }
    }

    private static class PictureMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = new Picture(event.getPoint(), Operations.getTempText());
                Operations.addShape(presentShape);
                presentShape = null;
                Operations.update();
                Operations.setMode(Operations.getLastMode());
            }
        }
    }

    private static class ImportMode {

        public static void click(MouseEvent event) {
            if (presentShape == null) {
                presentShape = Operations.importShape(new File(Operations.getTempText()), event.getPoint());
                Operations.addShape(presentShape);
                presentShape = null;
                Operations.update();
                Operations.setMode(Operations.getLastMode());
            }
        }
    }
}
