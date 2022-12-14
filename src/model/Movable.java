package model;

import java.util.Random;

/**
 * interface in order to implement a movable object
 */
public interface Movable{

    Random randi = new Random();
    float hitboxWidthLeft=0.25F;
    float hitboxWidthRight=0.25F;
    float hitboxHeightTop=0.15F;
    float hitboxHeightBottom=0.50F;

    // collision methods
    void detectCollisionRight(double d);
    void detectCollisionUp(double d);
    void detectCollisionLeft(double d);
    void detectCollisionDown(double d);
    boolean detectDiagonalCollisionRightLeft(int line,int nextColumn);
    boolean detectDiagonalCollisionUpDown(int nextLine,int column);

    float roundFloat(float f);
    void update(double d);
    
}