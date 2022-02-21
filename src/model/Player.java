package model;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.awt.event.KeyEvent;
import view.Gui;

public class Player extends GameObject implements Movable,Runnable{
    private int id;
    private float speed = (float)0.2;
    private int centerRow;
    private int centerCol;
    private int keyUp, keyDown, keyLeft, keyRight,keyAction;
    private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false;
    private float velz, velq, vels, veld;
    private BufferedImage[][] walkFrames;
    private BufferedImage currentFrame;
    private float preX;
    private float preY;
    
    public Player(BufferedImage image,int id,float x,float y) {
        super(image,x,y);
    	this.id = id;
        //centerRow = (int)(((this.getPositionY() + Player.sizeY/2 )/Gui.height)*Board.sizeRow);
		//centerCol = (int)(((this.getPositionX()+ Player.sizeX/2)/Gui.width)*Board.sizeCol);
		/*
		walkFrames = new BufferedImage[2][4];
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {
				walkFrames[i][j] = image.getSubimage(j*Player.sizeX, i*Player.sizeY, Player.sizeX, Player.sizeY);
			}
		}
		
        currentFrame = walkFrames[0][0];
		*/
		currentFrame = image;
    }
    
	public int getId() {
		return id;
	}

    public void bindKeys(int up, int down, int left, int right,int action) {
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		keyAction = action;
	}

    public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == keyUp) {
			pressUp = true;
		}else if(k == keyDown) {
			pressDown = true;
		}else if(k == keyRight) {
			pressRight = true;
			System.out.print("Player" + id + "Pressed: " + e.getKeyChar() + "\n");
		}else if(k == keyLeft) {
			pressLeft = true;
			System.out.print("Player" + id + "Pressed: " + e.getKeyChar() + "\n");
	    } else if(k == keyAction) {
			//bomber
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
			System.out.print("Player" + id +" Released: " + e.getKeyChar() + "\n");
		}else if(k == keyRight) {
			veld = 0;
			pressRight = false;
			System.out.print("Player" + id + "Released: " + e.getKeyChar() + "\n");
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
		//Board.cases[centerRow][centerCol].deleteMovableOnCase(this);
		
		if(pressDown || pressUp || pressLeft || pressRight) {
			/*
			if(pressDown){
				detectCollisionDown(position.y + speed);
				// x en 0.8 et 0.2
			}
			if(pressUp) {
				detectCollisionUp(position.y - speed);
				//haut et bas en 0.8
			}
			if(pressLeft) {
				detectCollisionLeft(position.x - speed);
			}
			*/
			if(pressRight) {
				detectCollisionRight();
            }
    		//this.translate(velq + veld, velz + vels);
		} 
        //Board.cases[centerRow][centerCol].addMovableOnCase(this);
	
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
	
	private void detectCollisionRight() {
		/*
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
		*/
		if (position.x % 1  >= 0.8F) {
			// check les murs
			int currentx = (int)position.x;
			int nextx = currentx+1;
			int nexty = (int)position.y;
			if (nextx < Board.cases.length) {
				if(Board.cases[nextx][nexty] != null){
					position.x+= speed;
					Board.cases[currentx][(int)position.y].deleteMovableOnCase(this);
					Board.cases[nextx][nexty].addMovableOnCase(this);
				}
			}
			System.out.println(position.x);
		}else {
			position.x += speed;
		}
		System.out.println(position.x);
	}

	public void setPlayer(BufferedImage a,int ind,float x,float y) {
		this.image = a;
		this.id = ind;
		this.setAttributs(a,x,y);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
