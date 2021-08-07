package com.acc240.paintbox.geom;

import com.acc240.paintbox.Operations;

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
