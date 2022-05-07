package model;

public class GameObject {
    protected static int sizeX;
	protected static int sizeY;
	
    protected Vector2f position ;
      
    public GameObject(float x, float y) {
    	position = new Vector2f(x,y);
    }

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
      
    public void setPosition(float x, float y) {
    	this.position.x = x;
    	this.position.y = y;
    }

    protected void setAttributs(float x, float y) {
        position.x = x;
        position.y = y;
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
