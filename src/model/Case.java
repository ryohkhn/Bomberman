package model;

import java.util.ArrayList;

public class Case{
    private ArrayList<Movable> movablesOnCase = new ArrayList<Movable>();
    private Wall wall;
    private Bonus bonus;
    private Bomb bomb;
    
	public void setWall(Wall wall) {
		this.wall = wall;
	}

	public void addMovableOnCase(Movable movable) {
		this.movablesOnCase.add(movable);
	}
	
	public ArrayList<Movable> getMovablesOnCase(){
		return this.movablesOnCase;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public Wall getWall() {
		return wall;
	}
}
