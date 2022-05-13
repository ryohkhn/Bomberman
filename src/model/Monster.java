package model;

/**
 * Abstract class of all monsters
 */
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
    protected boolean dead;

    protected Monster(float x, float y) {
        super(x, y);
    }

    // setters - getters

    /**
     * Return value of variable isset
     * @return isset
     */
    public boolean isSet() {
        return isset;
    }

    /**
     * Get the variable which represents the position of the image in the array of arrays of images
     * @return
     */
    public int getSpriteIndex() {
        return spriteIndex;
    }

    /**
     * Get the current direction
     */
    public int getDirection() {
        return direction;
    }
    
    public abstract int getType();

    /**
     * Initate Monster
     * @param x
     * @param y
     */
    public void setMonster(float x,float y) {
		this.setPosition(x, y);
		isset = true;
        direction = -1;
	}

    /**
     * @return dead
     */
    public boolean getDead() {
		return dead;
	}

    /**
     * Change the boolean variable isAlive
     * @param b alive boolean
     */
    public void setAlive(boolean b) {
		if (!b) { // spriteindex is reset
			spriteIndex = 0;
		}
		isAlive = b;
	}

    /**
     * Return the value of the variable isAlive
     * @return isAlive
     */
    public boolean isAlive() {
		return isAlive;
	}

    /**
     * Kill players who are present in the current case which the player is present.
     */
    public void killPlayers() {
		int line= (int)position.x;
		int column= (int)position.y;
		board.getCases()[line][column].killPlayers();
        
    }

    /**
     * Return the rounded number of a float
     */
    public float roundFloat(float f) {
        return (float)(Math.round((f)*100.0)/100.0);
    }
}
