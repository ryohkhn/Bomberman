package model;

import java.util.Random;

public abstract class Monster extends GameObject implements Movable{
    protected boolean isset = false;
    protected boolean isAlive = false;
    protected Board board;
    protected int direction;
    protected boolean move = false;
	protected int spriteIndex;
    protected int nextInvoke;
	protected int thinkTime;
    protected double spriteTimer;
    protected Monster(float x, float y) {
        super(x, y);
        //TODO Auto-generated constructor stub
    }
    // setters - getters
    public boolean isSet() {
        return isset;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public int getDirection() {
        return direction;
    }
    public abstract int getType();

    public void setMonster(float x,float y) {
		this.setAttributs(x,y);
		isset = true;
        direction = -1;
	}

    public void setAlive(boolean b) {
		if (!b) {
			spriteIndex = 0;
		}
		isAlive = b;
	}

    public boolean isAlive() {
		// TODO Auto-generated method stub
		return isAlive;
	}

    public void killPlayers() {
		int line= (int)position.x;
		int column= (int)position.y;
		board.getCases()[line][column].killPlayers();
        
    }

    public float roundFloat(float f) {
        return (float)(Math.round((f)*100.0)/100.0);
    }
}
