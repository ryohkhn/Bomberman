package model;

import java.util.HashMap;

public class Player implements Movable{
    private int id;
    private int bombCount;
    private HashMap<Bonus,Integer> bonusMap;
    
    public Player(int id) {
    	this.id = id;
    }
    
	public int getId() {
		return id;
	}
}
