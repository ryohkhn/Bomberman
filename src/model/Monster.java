package model;

public abstract class Monster extends GameObject implements AI,Movable{
    protected boolean isset = true;
    public Monster(float x, float y) {
        super(x, y);
        //TODO Auto-generated constructor stub
    }

    public boolean isSet() {
        return isset;
    }

    public void setMonster(float x,float y) {
		this.setAttributs(x,y);
		isset = true;
	}

    abstract void killPlayers();
}
