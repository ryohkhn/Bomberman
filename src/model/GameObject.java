package model;

/**
 * GameObject stocks the sizes of guiboard and is a superclass
 * of class Player,Bomb and Monster.
 * It stocks also the positions of players and bombs.
 */
public class GameObject {
    protected static int sizeX; // sizeX of guiboard
	protected static int sizeY; // sizeY
    protected Vector2f position ; // coordinates of object is stocked in the fields of a Vector2f object

    /**
     * Constructor of gameobject is called by players objects
     * @param x position x
     * @param y position y
     */
    public GameObject(float x, float y) {
    	position = new Vector2f(x, y);
    }

    // setters - getters

    public static void setSizeX(int x) {
        GameObject.sizeX = x;
    }

    public static void setSizeY(int sizeY) {
        GameObject.sizeY = sizeY;
    }
      
    public float getPositionX() {
    	return position.x;
    }
      
    public float getPositionY() {
    	return position.y;
    }

    /**
     * Set coordinates of current object in a vector2f object
     * @param x position x
     * @param y position y
     */
    public void setPosition(float x, float y) {
    	this.position.x = x;
    	this.position.y = y;
    }

    /**
     * Vector class
     */
    static class Vector2f {
        public float x;
        public float y;
        
        public Vector2f(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
