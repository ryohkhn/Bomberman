package model;

/**
 * GameObject stocks the sizes of guiboard and is a superclass
 * of class Player,Bomb and Monster.
 * It stocks also the positions of players and bombs.
 */
public class GameObject {
    protected static int sizeX; // sizeX of guiboard
	protected static int sizeY; // sizeY
	
    protected Vector2f position ;
    /**
     * Constructor of gameobject is called by players objects
     * @param x cooridinates
     * @param y
     */
    public GameObject(float x, float y) {
    	position = new Vector2f(x,y);
    }
    // setters - getters
    public static void setSizeX(int x) {
        GameObject.sizeX = x;
    }

    public static void setSizeY(int sizeY) {
        GameObject.sizeY = sizeY;
    }

    public static int getSizeX() {
        return sizeX;
    }
    public static int getSizeY() {
        return sizeY;
    }
      
    public float getPositionX() {
    	return position.x;
    }
      
    public float getPositionY() {
    	return position.y;
    }
      
    public Vector2f getPosition() {
    	return position;
    }
    
    /**
     * Set coordinates of current object in a vector2f object
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
    	this.position.x = x;
    	this.position.y = y;
    }
    class Vector2f {
        public float x;
        public float y;
        
        public Vector2f() {
            x = 0; y=0;
        }
        
        public Vector2f(float x, float y) {
            this.x = x;
            this.y = y;
        }
        
        public Vector2f(Vector2f vector) {
            this(vector.x, vector.y);
        }
        
    }
}
