package model;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.event.KeyEvent;
import view.Gui;

public class Player extends GameObject implements Movable{
    private int id;
    private int bombCount;
    private HashMap<Bonus,Integer> bonusMap;
	private boolean alive;
    private float x;
    private float y;
    private float speed = 2;
    private int centerRow;
    private int centerCol;
    private int keyUp, keyDown, keyLeft, keyRight;
    private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false;
    private float velz, velq, vels, veld;
    private BufferedImage[][] walkFrames;
    private BufferedImage currentFrame;
    private float preX;
    private float preY;
    
    public Player(BufferedImage image,int id,float x,float y) {
        super(image,x,y);
    	this.id = id;
    	this.alive = true;
        this.x = x;
        this.y = y;
        centerRow = (int)(((this.getPositionY() + Player.sizeY/2 )/Gui.height)*Board.sizeRow);
		centerCol = (int)(((this.getPositionX()+ Player.sizeX/2)/Gui.width)*Board.sizeCol);
		
		walkFrames = new BufferedImage[2][4];
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				if(image == null) continue;
				walkFrames[i][j] = image.getSubimage(j*Player.sizeX, i*Player.sizeY, Player.sizeX, Player.sizeY);
			}
		}
        currentFrame = walkFrames[0][0];
    }
    
	public int getId() {
		return id;
	}

	public int getBombCount() {
		return bombCount;
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public void setAlive(boolean b) {
		this.alive = b;
	}
    public void bindKeys(int up, int down, int left, int right) {
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
	}

    public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == keyUp) {
			pressUp = true;
		}else if(k == keyDown) {
			pressDown = true;
		}else if(k == keyRight) {
			pressRight = true;
		}else if(k == keyLeft) {
			pressLeft = true;
	    }
    }
		
	public void keyReleased(KeyEvent e) {	
		int k = e.getKeyCode();
        if(k == keyUp) {
			velz = 0;
			pressUp = false;
		}else if(k == keyDown) {
			vels = 0;
			pressDown = false;
		}else if(k == keyLeft) {
			velq = 0;
				pressLeft = false;
		}else if(k == keyRight) {
			veld = 0;
			pressRight = false;
		}	
		
	}

    
	public boolean isPressDown() {
		return pressDown;
	}

	public void moveDown(boolean pressDown) {
		this.pressDown = pressDown;
		if(!pressDown)
			vels = 0;
	}

	public boolean isPressUp() {
		return pressUp;
	}

	public void moveUp(boolean pressUp) {
		this.pressUp = pressUp;
		if(!pressUp)
			velz = 0;
	}

	public boolean isPressLeft() {
		return pressLeft;
	}
    @Override
	public void moveLeft(boolean pressLeft) {
		this.pressLeft = pressLeft;
		if(!pressLeft)
			velq = 0;
	}

	public boolean isPressRight() {
		return pressRight;
	}

	public void moveRight(boolean pressRight) {
		this.pressRight = pressRight;
		if(!pressRight)
			veld = 0;
	}

    @Override
    public BufferedImage getImage() {
		return currentFrame;
	}
	
	
	public float getvelq() {
		return velq;
	}
	
	public float getVeld() {
		return veld;
	}
	
	public float getVelw() {
		return velz;
	}
	
	public float getVels() {
		return vels;
	}
	
	public float getSpeed() {
		return speed;
	}

    public int getCenterRow() {
		return this.centerRow;
	}
	
	public int getCenterCol() {
		return this.centerCol;
	}
	
	public void setCenterRow(int row) {
		centerRow = row;
	}
	public void setCenterCol(int col) {
		centerCol = col;
	}

    public void update() {

        preX = position.x;
		preY = position.y;
	
		Board.cases[centerRow][centerCol].deleteMovableOnCase(this);
		if(pressDown || pressUp || pressLeft || pressRight) {
			
			if(pressDown){
				detectCollisionDown(position.y + speed);
			}
			if(pressUp) {
				detectCollisionUp(position.y - speed);
			}
			if(pressLeft) {
				detectCollisionLeft(position.x - speed);
			}
			if(pressRight) {
				detectCollisionRight(position.x + speed);
            }
    		this.translate(velq + veld, velz + vels);
		} 
        Board.cases[centerRow][centerCol].addMovableOnCase(this);
		
	
	}

    private void detectCollisionDown(float newPosY) {
		int newMatrixY = (int)(((newPosY + Player.sizeY) / Gui.height)*Board.sizeCol);
		int oldMatrixXLeft = (int)(((preX) / Gui.width)*Board.sizeRow) ;
		int oldMatrixXRight = (int)(((preX + Player.sizeX) / Gui.width)*Board.sizeRow);
		if(Board.cases[newMatrixY][oldMatrixXLeft] != null || Board.cases[newMatrixY][oldMatrixXRight] != null) { //walls
			vels = 0;
			this.position.y = newMatrixY * GameObject.sizeY - Player.sizeY - 1;
		}else {
			vels = speed;
		}
		centerRow = (int)(((this.position.y + Player.sizeY/2) / Gui.height)*Board.sizeRow);
      
	}
	
	private void detectCollisionUp(float newPosY) {
		int newMatrixY = (int)(((newPosY) / Gui.height)*Board.sizeCol);
		int oldMatrixXLeft = (int)(((preX) / Gui.width)*Board.sizeRow);
		int oldMatrixXRight = (int)(((preX + Player.sizeX) / Gui.width)*Board.sizeRow);
		if(Board.cases[newMatrixY][oldMatrixXLeft] != null || Board.cases[newMatrixY][oldMatrixXRight] != null) {
			velz = 0;
			this.position.y = newMatrixY * GameObject.sizeY + GameObject.sizeY + 1;
		}else {
			velz = -speed;
		}
		centerRow = (int)(((this.position.y + Player.sizeY/2) / Gui.height)*Board.sizeRow);
	}
	
	private void detectCollisionLeft(float newPosX) {
		int newMatrixX = (int)(((newPosX) / Gui.width)*Board.sizeRow);
		int oldMatrixYUp = (int)(((preY) / Gui.height)*Board.sizeCol);
		int oldMatrixYDown = (int)(((preY + Player.sizeY) / Gui.height)*Board.sizeCol);
        if(Board.cases[oldMatrixYUp][newMatrixX] != null || Board.cases[oldMatrixYDown][newMatrixX] != null) {
			velq = 0;
			this.position.x  = newMatrixX * GameObject.sizeX + GameObject.sizeX + 1;
		}else {
			velq = -speed;
		}
		centerCol = (int)(((this.position.x + Player.sizeX/2)/Gui.width)*Board.sizeCol);
	}
	
	private void detectCollisionRight(float newPosX) {
		int newMatrixX = (int)(((newPosX+Player.sizeX)/Gui.width)*Board.sizeRow);
		int oldMatrixYUp = (int)(((preY) / Gui.height)*Board.sizeCol);
		int oldMatrixYDown = (int)(((preY + Player.sizeY)/Gui.height)*Board.sizeCol);
		if(Board.cases[oldMatrixYUp][newMatrixX] != null || Board.cases[oldMatrixYDown][newMatrixX] != null) {
			veld = 0;
			this.position.x = newMatrixX * GameObject.sizeX - Player.sizeX - 1; // vérifier
		}else {
			veld = speed;
		}
		centerCol = (int)(((this.position.x + Player.sizeX/2)/Gui.width)*Board.sizeCol);
	}
}
