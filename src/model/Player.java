package model;

import java.util.HashMap;

public class Player implements Movable{
    private int id;
    private int bombCount;
    private HashMap<Bonus,Integer> bonusMap;
	private boolean alive;
    
    public Player(int id) {
    	this.id = id;
    	this.alive = true;
    }
    
	public int getId() {
		return id;
	}

	public int getBombCount() {
		return bombCount;
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public void setAlive(boolean b) {
		this.alive = b;
	}
}
