package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Case{
    private ArrayList<Movable> movablesOnCase = new ArrayList<>();
    private Wall wall;
    private Bonus bonus;
    private Bomb bomb;

	/**
     * Kill movables in the Case and give points to the player
     * @return new point count of the player
     * @param player: Original player that placed the bomb
     */
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
			bomb.setFuse(true);
		}
		return pointsCount;
	}

	/**
	 * * Function that Kill players in the Case
	 * 	 * @param monster
     */
	public void killPlayers() {
		Iterator<Movable> iter = movablesOnCase.iterator();
		while(iter.hasNext()) {
			Movable m = iter.next();
			if (m instanceof Player && ((Player)m).isAlive()) {
				((Player)m).setAlive(false);
				iter.remove();
			}
		}
	}

	/**
     * Return true if there are other players in the Case
     * @param p: original player
     */
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

	/**
     * Return true if there are players in the Case
     */
	public boolean hasPlayers() {
		return hasPlayers(null);
	}

	/**
     * Return players in the Case
     * @return players on case
     */
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
	
	/**
	 * add a movable object to the arraylist
	 * @param movable movable
	 */
	public void addMovableOnCase(Movable movable) {
		this.movablesOnCase.add(movable);
	}
	/**
	 * Remove movable object from the arraylist
	 * @param movable movable
	 */
	public void deleteMovableOnCase(Movable movable) {
		this.movablesOnCase.remove(movable);
	}
	/**
	 * @return movables on case
	 */
	public ArrayList<Movable> getMovablesOnCase(){
		return this.movablesOnCase;
	}
	/** 
	 * @return bonus on the case
	*/
	public Bonus getBonus() {
		return bonus;
	}
	/**
	 * set the bonus on the case
	 * @param bonus bonus
	 */
	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}
	/**
	 * @return variable wall
	 */
	public Wall getWall() {
		return wall;
	}
	
	/**
	 * @return bomb
	 */
	public Bomb getBomb() {
		return this.bomb;
	}

	/**
	 * set an object bomb to the variable bomb
	 * @param bomb bomb
	 */
	public void setBomb(Bomb bomb) {
		this.bomb = bomb;
	}

	/**
	 * set an object wall to the variable wall
	 * @param wall wall
	 */
	public void setWall(Wall wall) {
		this.wall = wall;
	}


}
