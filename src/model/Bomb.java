package model;

public class Bomb extends GameObject{
    private double startTime;

    public Bomb(int x, int y){
        super(null,x,y);
        startTime= System.currentTimeMillis();
    }
}
