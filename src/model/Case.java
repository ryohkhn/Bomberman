package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Case{
    private ArrayList<Movable> movablesOnCase = new ArrayList<>();
    private Wall wall;
    private Bonus bonus;
    private Bomb bomb;
	private int nav_group;
	private boolean nav_update;

	public void setWall(Wall wall) {
		this.wall = wall;
		nav_update = false;
		nav_group = 0;
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

	public void setNav_group(int nav_group) {
		this.nav_group = nav_group;
	}

	public int getNav_group() {
		return nav_group;
	}

	public void setNav_update(boolean nav_update) {
		this.nav_update = nav_update;
	}
	
	public boolean getNav_update() {
		return nav_update;
	}

	public int killMoveables(Player player) {
		Iterator<Movable> iterator = movablesOnCase.iterator();
		int pointsCount = 0;
		while(iterator.hasNext()) {
			Movable m = iterator.next();
			if (m instanceof Player && !player.getCoop()) {
				if (player != m) pointsCount += 100;
				((Player)m).setAlive(false);
				iterator.remove();
			}
			if (m instanceof Monster) {
				if (m instanceof FlyingMonster) pointsCount += 50;
				if (m instanceof WalkingMonster) pointsCount += 30;
				((Monster)m).setAlive(false);
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

	public void killPlayers() {
		Iterator<Movable> iter = movablesOnCase.iterator();
		while(iter.hasNext()) {
			Movable m = iter.next();
			if (m instanceof Player) {
				((Player)m).setAlive(false);
				iter.remove();
			}
		}
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
