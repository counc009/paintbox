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
package acc240.paintbox.geom;

import acc240.paintbox.Operations;

import java.awt.Color;

public abstract class FillableShape extends Shape {

    protected boolean filled = false;
    protected Color filling = null;

    public FillableShape(String name, float strokeWidth) {
        super(name, strokeWidth);
        if (Operations.getFilled()) {
            filled = true;
            filling = Operations.getFill();
            Operations.update();
        }
    }

    public final boolean getFilled() {
        return filled;
    }

    public final Color getFilling() {
        return filling;
    }

    public final void setFilling(Color color) {
        filled = true;
        filling = color;
    }

    public final void setUnfilled() {
        filled = false;
    }

    public final void setFilled() {
        filled = true;
    }
}
