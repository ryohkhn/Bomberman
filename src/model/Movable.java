package model;

import java.awt.*;

public interface Movable{
    Point point=new Point();
    public void moveUp(boolean b);
    public void moveDown(boolean b);
    public void moveLeft(boolean b);
    public void moveRight(boolean b);
}
