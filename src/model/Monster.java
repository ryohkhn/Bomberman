package model;

import java.util.Random;

public abstract class Monster extends GameObject implements Movable{
    protected boolean isset = false;
    protected boolean isAlive = false;
    protected Board board;
    protected float speed = 1F;
    protected int direction;
    protected boolean move = false;
	protected int spriteIndex;
    protected int nextInvoke;
	protected int thinkTime = 75;
    protected Monster(float x, float y) {
        super(x, y);
        //TODO Auto-generated constructor stub
    }

    public boolean isSet() {
        return isset;
    }

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

    abstract void killPlayers();
}
