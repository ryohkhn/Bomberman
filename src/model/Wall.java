package model;

public class Wall{
	private final boolean breakable;
	
	/**
	 * Constructor for Wall
	 * @param b true if wall is breakable
	 */
    public Wall(boolean b) {
		this.breakable = b;
	}

    /**
     * Getter for breakable
     * @return boolean breakable
     */
	public boolean isBreakable() {
		return breakable;
	}

}
