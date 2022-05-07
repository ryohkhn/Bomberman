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

	public int killMoveables(Player player) {
		Iterator<Movable> iterator = movablesOnCase.iterator();
		int pointsCount = 0;
		while(iterator.hasNext()) {
			Movable m = iterator.next();
			if (m instanceof Player && (!player.getCoop() || m == player)) {
				if (player != m) pointsCount += 1;
				((Player)m).setAlive(false);
				iterator.remove();
			}
			if (m instanceof Monster) {
				if (m instanceof FlyingMonster) pointsCount += 40;
				if (m instanceof WalkingMonster) pointsCount += 30;
				((Monster)m).setAlive(false);
				iterator.remove();
			}
			if(m instanceof Wall && ((Wall) m).isBreakable()){
				pointsCount += 1;
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

	public boolean hasPlayers(Player p) {
		Iterator<Movable> iter = movablesOnCase.iterator();
		while(iter.hasNext()) {
			Movable m = iter.next();
			if (m instanceof Player && m != p) {
				return true;
			}
		}
		return false;
	}

	public Player getPlayerOnCase(Player p) {
		Iterator<Movable> iter = movablesOnCase.iterator();
		while(iter.hasNext()) {
			Movable m = iter.next();
			if (m instanceof Player && m != p) {
				return (Player)m;
			}
		}
		return null;
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
