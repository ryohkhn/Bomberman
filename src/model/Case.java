package model;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	public void deleteMovableOnCase(Movable movable) {
		this.movablesOnCase.remove(movable);
	}

	public ArrayList<Movable> getMovablesOnCase(){
		return this.movablesOnCase;
	}

	public Bonus getBonus() {
		return bonus;
	}
	
	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	public Wall getWall() {
		return wall;
	}
	
	public Bomb getBomb() {
		return this.bomb;
	}

	public void setBomb(Bomb bomb) {
		if(bomb == null) this.bomb = null;
		else this.bomb = bomb;
	}

	public int killMoveables(Board board) {
		int pointsCount = 0;
		Iterator<Movable> iterator = movablesOnCase.iterator();
		while(iterator.hasNext()) {
			Movable m = iterator.next();
			if (m instanceof Player) {
				pointsCount += 100;
				((Player)m).setAlive(false);
				iterator.remove();
			}
			if(m instanceof Wall && ((Wall) m).isBreakable()){
				pointsCount += 10;
			}
		}
		if (bomb != null) {
			bomb.setFuse(1);
		}
		return pointsCount;
	}


	@Override
	public String toString() {
		return "Case{" +
				"movablesOnCase=" + movablesOnCase +
				", wall=" + wall +
				", bonus=" + bonus +
				", bomb=" + bomb +
				'}';
	}
}
