package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.KeyEvent;
import view.Gui;

public class Player extends GameObject implements Movable{
    private int id;
    private int bombCount;

    private ArrayList<Bomb> bombList = new ArrayList<>();
    private HashMap<Bonus,Integer> bonusMap;
	private boolean alive;
    private float speed = 2;
    private int centerRow;
    private int centerCol;
    private int keyUp, keyDown, keyLeft, keyRight, keyAction;
    private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false;
    private BufferedImage[][] walkFrames;
    private BufferedImage currentFrame;
    private float preX;
    private float preY;
	private int spriteTimer;
	private int spriteIndex;
	private int direction;

	private Board board; // utile pour les bombes, possiblement temporaire

    public Player(BufferedImage image,int id,float x,float y, Board board) {
        super(image,x,y);
    	this.id = id;
    	this.alive = true;
        position.x = x;
        position.y = y;
        centerRow = (int)(((this.getPositionY() + Player.sizeY/2 )/Gui.height)*Board.sizeRow);
		centerCol = (int)(((this.getPositionX()+ Player.sizeX/2)/Gui.width)*Board.sizeCol);
		
		walkFrames = new BufferedImage[2][4];
		for(int i=0; i<2; i++) {
			for (int j = 0; j < 4; j++) {
				if (image == null) continue;
				walkFrames[i][j] = image.getSubimage(j * Player.sizeX, i * Player.sizeY, Player.sizeX, Player.sizeY);
			}
		}
		currentFrame = walkFrames[0][0];
		this.board=board;
    }


	public void update() {
		if (alive) {
			if ((spriteTimer += speed) >= 10) {
                spriteIndex++;
                spriteTimer = 0;
            }
            if ((!pressUp && !pressDown && !pressLeft && !pressRight) || (this.spriteIndex >= walkFrames[0].length)) {
                spriteIndex = 0;
            }
            image = this.walkFrames[this.direction][this.spriteIndex];
		}
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
    public void bindKeys(int up, int down, int left, int right, int action) {

		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		keyAction = action;
	}

	public float getSpeed() {
		return speed;
	}

	public int getKeyUp() {
		return keyUp;
	}
	public int getKeyDown() {
		return keyDown;
	}
	public int getKeyLeft() {
		return keyLeft;
	}
	public int getKeyRight() {
		return keyRight;
	}

	public int getKeyAction() {
		return keyAction;
	}

	public void setPressDown() {
		this.pressDown = false;
	}
	public void setPressUp() {
		this.pressUp = false;
	}
	public void setPressLeft() {
		this.pressLeft = false;
	}
	public void setPressRight() {
		this.pressRight = false;
	}


    public void detectCollisionDown() {
		pressDown = true;
		direction = 1;
		if (roundFloat(position.x % 1)>= 0.6F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line+1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+nextLine+" colonne: "+column);
			if(nextLine<Board.cases.length-1) {
				if(Board.cases[nextLine][column].getWall()==null){
					position.x+=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[nextLine][column].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
		}else {
			position.x+=speed;
			position.x=roundFloat(position.x);
		}
	}

	public void detectCollisionUp() {
		pressUp = true;
		direction = 0;
		if (roundFloat(position.x % 1)<= 0.4F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line-1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+nextLine+" colonne: "+column);
			if (nextLine>0) {
				if(Board.cases[nextLine][column].getWall()==null){
					position.x-=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[nextLine][column].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
			//System.out.println(position.x);
		}else {
			position.x-=speed;
			position.x=roundFloat(position.x);
		}
		//System.out.println(position.x);
	}
	
	public void detectCollisionLeft() {
		pressLeft = true;
		direction = 2;
		if (roundFloat(position.y % 1)<= 0.4F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column-1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+line+" colonne: "+nextColumn);
			if (nextColumn>0){
				if(Board.cases[line][nextColumn].getWall()==null){
					position.y-=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[line][nextColumn].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
			//System.out.println(position.y);
		}else {
			position.y-=speed;
			position.y=roundFloat(position.y);
		}
		//System.out.println(position.y);
	}
	
	public void detectCollisionRight() {
		pressRight = true;
		direction = 3;
		if (roundFloat(position.y % 1)>= 0.6F){
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column+1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+line+" colonne: "+nextColumn);
			if(nextColumn<Board.cases[0].length-1) {
				if(Board.cases[line][nextColumn].getWall()==null){
					position.y+=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[line][nextColumn].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
			//System.out.println(position.y);
		}else {
			position.y+=speed;
			position.y=roundFloat(position.y);
		}
	}



	public void setPlayer(BufferedImage spriteSheet,int ind,float x,float y,int spriteWidth,int spriteHeight) {
		int rows = spriteSheet.getHeight() / spriteHeight;
        int cols = spriteSheet.getWidth() / spriteWidth;
        walkFrames = new BufferedImage[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                walkFrames[row][col] = spriteSheet.getSubimage(col * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight);
            }
        }

		this.id = ind;
		this.setAttributs(walkFrames[1][0],x,y);
	}


	private float roundFloat(float f){
		return (float)(Math.round((f)*100.0)/100.0);
	}


	public void dropBomb() {
		if(bombCount <= 0){
			bombList.add(new Bomb((int)position.x,(int)position.y, 1, false, this, board)); // on ajoute la bombe aux coordonnées de la case (plus besoin du détail apres la virgule)
			bombCount += 1;
			centerCol = (int)(((this.position.x + Player.sizeX/2)/Gui.width)*Board.sizeCol);
		}else{
			System.out.println("vous avez deja posé une bombe");
		}
	}

	public void bombUpdate() {
		ArrayList<Bomb> valueToRemove=new ArrayList<>();
		for(Bomb b : bombList){
			if(System.currentTimeMillis() - b.getStartTime() > 3000){
				b.explode();
				b.killMovables();
				valueToRemove.add(b);
				bombCount -= 1;
				board.getCases()[(int)b.position.x][(int)b.position.y].setBomb(null);
				System.out.println("bomb delete");
			}
		}
		bombList.removeAll(valueToRemove);
	}

	public Board getBoard() {
		return board;
	}

	private int ammo = 1;
    private boolean kick = false;
    private boolean pierce = false;
    private int firepower = 1; //max 6
    
    public void addFirepower(boolean max) {
    	this.firepower = (max)?6:this.firepower+1;
    }
    
    public void addAmmo() {
    	this.ammo += 1;
    }
    
    public void addSpeed() {
    	this.speed += 1;
    }
    

	public void setPierce(boolean pierce) {
		this.pierce = pierce;
	}

	public void setKick(boolean kick) {
		this.kick = kick;
	}

	public void reduceTimer(int time) {
		
	}
}