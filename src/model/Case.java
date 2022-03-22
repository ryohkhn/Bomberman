package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Case{
    private ArrayList<Movable> movablesOnCase = new ArrayList<Movable>();
    private Wall wall;
    private Bonus bonus;
    private Bomb bomb;
    private boolean exploding;
    
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

	public void killMoveables(Board board) {
		//bonus = null; devrait effacer le bonus si celui-ci est sur la portée des bombes.
		Iterator<Movable> iterator = movablesOnCase.iterator();
		while(iterator.hasNext()) {
			Movable m = iterator.next();
			if (m instanceof Player) {
				((Player)m).setAlive(false);
				iterator.remove();
			}
		}
	}

	public boolean isExploding() {
		return exploding;
	}

	public void setExploding(boolean exploding) {
		this.exploding = exploding;
	}

	@Override
	public String toString() {
		return "Case{" +
				"movablesOnCase=" + movablesOnCase +
				", wall=" + wall +
				", bonus=" + bonus +
				", bomb=" + bomb +
				", exploding=" + exploding +
				'}';
	}
}
