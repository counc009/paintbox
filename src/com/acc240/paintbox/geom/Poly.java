package com.acc240.paintbox.geom;

public interface Poly {

    void setPoint(int index, int x, int y);

    void addPoint(int x, int y);

    void insert(int index, int x, int y);

    void delete(int index);

    void randomize(int max);
}
