package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;


public class Player extends GameObject implements Movable{
    private int id;
    private int bombCount;

    private ArrayList<Bomb> bombList = new ArrayList<>();
    private HashMap<Bonus,Integer> bonusMap;
	private boolean alive;
    private float speed = 5F;
    private int keyUp, keyDown, keyLeft, keyRight,keyAction;
    private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false, pressAction = false;
    private BufferedImage[][] walkFrames;
    private float preX;
    private float preY;
	boolean ai;
	private int spriteTimer;
	//private int deathTimer;
	private int spriteIndex;
	private int direction;

	private Board board; // utile pour les bombes, possiblement temporaire

    public Player(BufferedImage image,int id,float x,float y, Board board) {
        super(image,x,y);
    	this.id = id;
    	this.alive = true;
        position.x = x;
        position.y = y;
		this.board=board;
    }

	public void update(double deltaTime) {
		if (alive) {
			if ((spriteTimer += speed) >= 25) {
                spriteIndex++;
                spriteTimer = 0;
            }
            if ((!pressUp && !pressDown && !pressLeft && !pressRight) || (this.spriteIndex >= walkFrames[0].length)) {
                spriteIndex = 0;
            }
            image = this.walkFrames[this.direction][this.spriteIndex];

			if(pressUp){
				detectCollisionUp(deltaTime);
			}
			else if(pressDown){
				detectCollisionDown(deltaTime);
			}
			else if(pressRight){
				detectCollisionRight(deltaTime);
			}
			else if(pressLeft){
				detectCollisionLeft(deltaTime);
			} else if (pressAction){
				dropBomb();
			}
		} else {
			if (spriteTimer++ >= 15) {
                spriteIndex++;
                if (spriteIndex < walkFrames[4].length) {
                    image = walkFrames[4][spriteIndex];
                    spriteTimer = 0;
                } else {
					image = null;
				}
			}
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
		if (!b) {
			spriteIndex = 0; 
			//deathTimer = 0;
		}
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

	public void setAction() {
		pressAction = true;
	}

	public void setReleasedDown() {
		this.pressDown = false;
	}
	public void setReleasedUp() {
		this.pressUp = false;
	}
	public void setReleasedLeft() {
		this.pressLeft = false;
	}
	public void setReleasedRight() {
		this.pressRight = false;
	}

	public void setReleasedAction() {
		this.pressAction = false;
	}

	public void setPressDown() {
		this.pressDown = true;
	}
	public void setPressUp() {
		this.pressUp = true;
	}
	public void setPressLeft() {
		this.pressLeft = true;
	}
	public void setPressRight() {
		this.pressRight = true;
	}

    public void detectCollisionDown(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 1;
		if (roundFloat(position.x % 1)>= 0.4F) {
			Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line+1;
			if(nextLine<Board.cases.length-1) {
				if(Board.cases[nextLine][column].getBomb()!=null && this.kick) {
					Board.cases[nextLine][column].getBomb().setKicked(true,KickDirection.FromTop);
				}
				if(Board.cases[nextLine][column].getBonus()!=null) {
					Board.cases[nextLine][column].getBonus().grantBonus(this);
					Board.cases[nextLine][column].setBonus(null);
				}
				if(Board.cases[nextLine][column].getWall()==null && roundFloat(position.y%1)<=0.4F){
					position.x+=speedDelta;
					position.x=roundFloat(position.x);
				}
			}
			Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
		}else {
			position.x+=speedDelta;
			position.x=roundFloat(position.x);
		}
	}

	public void detectCollisionUp(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 0;
		if (roundFloat(position.x % 1)<= 0.4F) {
			Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line-1;
			if (nextLine>0) {
				if(Board.cases[nextLine][column].getBomb()!=null && this.kick) {
					Board.cases[nextLine][column].getBomb().setKicked(true,KickDirection.FromBottom);
				}
				if(Board.cases[nextLine][column].getBonus()!=null) {
					Board.cases[nextLine][column].getBonus().grantBonus(this);
					Board.cases[nextLine][column].setBonus(null);
				}
				if(Board.cases[nextLine][column].getWall()==null && roundFloat(position.y%1)<=0.4F){
					position.x-=speedDelta;
					position.x=roundFloat(position.x);
				}
			}
			Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
		}else {
			position.x-=speedDelta;
			position.x=roundFloat(position.x);
		}
	}
	
	public void detectCollisionLeft(double deltaTime){
		double speedDelta=speed/deltaTime;
		direction = 2;
		if (roundFloat(position.y % 1)<= 0.4F) {
			Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column-1;
			if (nextColumn>0){
				if(Board.cases[line][nextColumn].getBomb()!=null && this.kick) {
					Board.cases[line][nextColumn].getBomb().setKicked(true,KickDirection.FromRight);
				}
				if(Board.cases[line][nextColumn].getBonus()!=null) {
					Board.cases[line][nextColumn].getBonus().grantBonus(this);
					Board.cases[line][nextColumn].setBonus(null);
				}
				if(Board.cases[line][nextColumn].getWall()==null && roundFloat(position.x%1)<=0.4F){
					position.y-=speedDelta;
					position.y=roundFloat(position.y);
				}
			}
			Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
		}else {
			position.y-=speedDelta;
			position.y=roundFloat(position.y);
		}
	}
	
	public void detectCollisionRight(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 3;
		if (roundFloat(position.y % 1)>= 0.4F){
			Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column+1;
			if(nextColumn<Board.cases[0].length-1) {
				if(Board.cases[line][nextColumn].getBomb()!=null && this.kick) {
					Board.cases[line][nextColumn].getBomb().setKicked(true,KickDirection.FromLeft);
				}
				if(Board.cases[line][nextColumn].getBonus()!=null) {
					Board.cases[line][nextColumn].getBonus().grantBonus(this);
					Board.cases[line][nextColumn].setBonus(null);
				}
				if(Board.cases[line][nextColumn].getWall()==null && roundFloat(position.x%1)<=0.4F){
					position.y+=speedDelta;
					position.y=roundFloat(position.y);
				}
			}
			Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
		}else {
			position.y+=speedDelta;
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
		this.direction = 1;
	}


	private float roundFloat(float f){
		return (float)(Math.round((f)*100.0)/100.0);
	}


	public void dropBomb() { // TODO: 20/03/2022 faire en sorte qu'on ne puisse pas poser une bombe avant un certain temps (a chaque fois qu'on appuie sur la touche, ça appelle la fonction 6-7 fois, le temps qu'un enleve le doigt 
		if(bombCount <= 0){
			bombList.add(new Bomb((int)position.x,(int)position.y, 1, false, this, board)); // on ajoute la bombe aux coordonnées de la case (plus besoin du détail apres la virgule)
			bombCount += 1;
		}else{
			System.out.println("vous avez deja posé une bombe");
		}
	}

	public void bombUpdate() {
		ArrayList<Bomb> valueToRemove=new ArrayList<>();
		for(Bomb b : bombList){
			if(System.currentTimeMillis() - b.getStartTime() > 3000){
				b.explode();
				valueToRemove.add(b);
				bombCount -= 1;
				board.getCases()[(int)b.position.x][(int)b.position.y].setBomb(null);
				System.out.println("bomb delete");
			}
			if(b.isKicked()) {
				if((int)(b.position.x + b.getKick().getVelocity().x) >= 13 || (int)(b.position.y + b.getKick().getVelocity().y) >= 15) {
					b.stopKick();
				}
				else {
					if(board.getCases()[(int)(b.position.x + b.getKick().getVelocity().x)][(int)(b.position.y + b.getKick().getVelocity().y)].getWall()!=null) {
						b.stopKick();
					}
					if(board.getCases()[(int)(b.position.x + b.getKick().getVelocity().x)][(int)(b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().size()>0 && (board.getCases()[(int)(b.position.x + b.getKick().getVelocity().x)][(int)(b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().size()!=1 || !board.getCases()[(int)(b.position.x + b.getKick().getVelocity().x)][(int)(b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().get(0).equals(this))) {
						b.explode();
						valueToRemove.add(b);
						bombCount -= 1;
						board.getCases()[(int)b.position.x][(int)b.position.y].setBomb(null);
						System.out.println("bomb delete");
					}
					else if((int) b.position.x + b.getKick().getVelocity().x !=  (int) b.position.x|| (int) b.position.y + b.getKick().getVelocity().y !=  (int) b.position.y) {
						board.getCases()[(int)b.position.x][(int)b.position.y].setBomb(null);
						board.getCases()[(int)(b.position.x + b.getKick().getVelocity().x)][(int)(b.position.y + b.getKick().getVelocity().y)].setBomb(b);
					}
					b.setPosition(b.position.x + b.getKick().getVelocity().x, + b.position.y + b.getKick().getVelocity().y);
				}
			}
		}
		bombList.removeAll(valueToRemove);
	}

	public Board getBoard() {
		return board;
	}

	private int ammo = 1;
    private boolean kick = true;
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

	public boolean isAlive() {
		return this.alive;
	}

	@Override
	public String toString() {
		return "Player{" +
				"id=" + id +
				'}';
	}
}