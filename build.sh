#!/bin/bash
javac -d ./build `find ./src -name '*.java'`
cd build
jar cfe paintbox.jar com.acc240.paintbox.PaintBox `find . -name '*.class'`
