package model;

import java.awt.*;
import java.util.Random;

public interface Movable{
    Point point=new Point();
    void detectCollisionRight(double d);
    void detectCollisionUp(double d);
    void detectCollisionLeft(double d);
    void detectCollisionDown(double d);
    boolean detectDiagonalCollisionRightLeft(int line,int nextColumn);
    boolean detectDiagonalCollisionUpDown(int nextLine,int column);
    float roundFloat(float f);
    Random randi = new Random();
    final float hitboxWidthLeft=0.28F;
	final float hitboxWidthRight=0.38F;
	final float hitboxHeightTop=0.28F;
	final float hitboxHeightBottom=0.55F;
    void update(double d);
    
}