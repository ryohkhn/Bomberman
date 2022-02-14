package model;

public class Wall{
	private boolean breakable;
	
    public Wall(boolean b) {
		this.breakable = b;
	}

	public boolean isBreakable() {
		return breakable;
	}

}
