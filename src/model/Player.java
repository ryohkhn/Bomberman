package model;

import java.util.ArrayList;


public class Player extends GameObject implements Movable{
    private int id;
    private int bombCount;

    private ArrayList<Bomb> bombList = new ArrayList<>();
	private boolean alive;
    private float speed = 1F;
    private int keyUp, keyDown, keyLeft, keyRight,keyAction;
    private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false, pressAction = false;
	boolean ai;
	private int spriteTimer;
	private int spriteIndex;
	private int direction;
	private int points=0;

	private int ammo = 1;
	private boolean kick = false;
	private boolean pierce = false;
	private boolean isset;
	private int firepower = 3; //max 6



	private Board board; // utile pour les bombes, possiblement temporaire

    public Player(int id,float x,float y, Board board) {
        super(x,y);
    	this.id = id;
    	this.alive = true;
        position.x = x;
        position.y = y;
		this.board=board;
		this.direction = 1;
		this.spriteIndex = 0;
    }

	public void update(double deltaTime) {
		if (alive && isset) {
			if ((spriteTimer += speed) >= 20) {
                spriteIndex++;
                spriteTimer = 0;
            }
            if ((!pressUp && !pressDown && !pressLeft && !pressRight) || (this.spriteIndex >= 4)) {
                spriteIndex = 0;
            }
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
		}
		if (!isAlive() && isset) {
			if (spriteTimer++ >= 15) {
                spriteIndex++;
                if (spriteIndex < 4) {
                    spriteTimer = 0;
                }
			} else {
				isset =false;
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
			this.direction = 4;
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
	@Override
    public void detectCollisionDown(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 1;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
		Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line+1;
		double nextX=position.x+speedDelta;
		// detect if player stay in the same case after moving
		if((nextX+hitboxHeightBottom)<nextLine){
			position.x+=speedDelta;
			position.x=roundFloat(position.x);
		}
		else if(Board.cases[nextLine][column].getWall()==null && Board.cases[nextLine][column].getBomb() == null){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x+=speedDelta;
				position.x=roundFloat(position.x);
			}
			if(Board.cases[nextLine][column].getBonus()!=null) {
				Board.cases[nextLine][column].getBonus().grantBonus(this);
				Board.cases[nextLine][column].setBonus(null);
			}
		}
		if(Board.cases[nextLine][column].getBomb()!=null && this.kick && Board.cases[nextLine][column].getBomb().getSpriteIndex() == -1) {
			Board.cases[nextLine][column].getBomb().setKicked(true,KickDirection.FromTop);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}

	public int getPositionXasInt() {
		// TODO Auto-generated method stub
		return (int)super.getPositionX();
	}

	public int getPositionYasInt() {
		// TODO Auto-generated method stub
		return (int)super.getPositionY();
	}

	

	public void detectCollisionUp(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 0;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
		Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line-1;
		double nextX=position.x-speedDelta;
		if((nextX-hitboxHeightTop)>line){
			position.x-=speedDelta;
			position.x=roundFloat(position.x);
		}
		else if(Board.cases[nextLine][column].getWall()==null && Board.cases[nextLine][column].getBomb() == null){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x-=speedDelta;
				position.x=roundFloat(position.x);
			}
			if(Board.cases[nextLine][column].getBonus()!=null) {
				Board.cases[nextLine][column].getBonus().grantBonus(this);
				Board.cases[nextLine][column].setBonus(null);
			}
		}
		if(Board.cases[nextLine][column].getBomb()!=null && this.kick && Board.cases[nextLine][column].getBomb().getSpriteIndex() == -1) {
			Board.cases[nextLine][column].getBomb().setKicked(true,KickDirection.FromBottom);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}
	
	public void detectCollisionLeft(double deltaTime){
		double speedDelta=speed/deltaTime;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
		direction=2;
		Board.cases[(int) position.x][(int) position.y].deleteMovableOnCase(this);
		int line=(int) position.x;
		int column=(int) position.y;
		int nextColumn=column-1;
		double nextY=position.y-speedDelta;
		if((nextY-hitboxWidthLeft)>column){
			position.y-=speedDelta;
			position.y=roundFloat(position.y);
		}
		else if(Board.cases[line][nextColumn].getWall()==null && Board.cases[line][nextColumn].getBomb()==null){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y-=speedDelta;
				position.y=roundFloat(position.y);
			}
			if(Board.cases[line][nextColumn].getBonus()!=null){
				Board.cases[line][nextColumn].getBonus().grantBonus(this);
				Board.cases[line][nextColumn].setBonus(null);
			}
		}
		if(Board.cases[line][nextColumn].getBomb()!=null && this.kick && Board.cases[line][nextColumn].getBomb().getSpriteIndex()==-1){
			Board.cases[line][nextColumn].getBomb().setKicked(true, KickDirection.FromRight);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}
	
	public void detectCollisionRight(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 3;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
		Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextColumn=column+1;
		double nextY=position.y+speedDelta;
		if((nextY+hitboxWidthRight)<nextColumn){
			position.y+=speedDelta;
			position.y=roundFloat(position.y);
		}
		else if(Board.cases[line][nextColumn].getWall()==null && Board.cases[line][nextColumn].getBomb() == null){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y+=speedDelta;
				position.y=roundFloat(position.y);
			}
		}
		else{
			position.y+=nextColumn-(nextY+hitboxWidthRight);
			position.y=roundFloat(position.y);
		}
		if(Board.cases[line][nextColumn].getBomb()!=null && this.kick && Board.cases[line][nextColumn].getBomb().getSpriteIndex() == -1) {
			Board.cases[line][nextColumn].getBomb().setKicked(true,KickDirection.FromLeft);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}


	// detect diagonal collision when player is between two case
	public boolean detectDiagonalCollisionUpDown(int nextLine,int column){
		if(position.y%1<hitboxWidthLeft && Board.cases[nextLine][column-1].getWall()==null){
			return true;
		}
		else if(position.y%1>1-hitboxWidthRight && Board.cases[nextLine][column+1].getWall()==null){
			return true;
		}
		else if(position.y%1>hitboxWidthLeft && position.y%1<1-hitboxWidthRight){
			return true;
		}
		return false;
	}

	// detect diagonal collision when player is between two case
	public boolean detectDiagonalCollisionRightLeft(int line,int nextColumn){
		if(position.x%1<hitboxHeightTop && Board.cases[line-1][nextColumn].getWall()==null){
			return true;
		}
		else if(position.x%1>1-hitboxHeightBottom && Board.cases[line+1][nextColumn].getWall()==null){
			return true;
		}
		else if(position.x%1>hitboxHeightTop && position.x%1<1-hitboxHeightBottom){
			return true;
		}
		return false;
	}

	public void setPlayer(int ind,float x,float y) {
		this.id = ind;
		this.setAttributs(x,y);
		isset = true;
	}

	public int getSpriteIndex() {
		return spriteIndex;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isSet() {
		return isset;
	}


	public float roundFloat(float f){
		return (float)(Math.round((f)*100.0)/100.0);
	}


	public void dropBomb() {
		if(bombCount < this.ammo && (this.board.getCases()[(int)position.x][(int)position.y].getBomb()==null)){
			//System.out.println("Ammo " + this.ammo + " Bombs " + bombCount);
			bombList.add(new Bomb((int)position.x,(int)position.y, this, board)); // on ajoute la bombe aux coordonnées de la case (plus besoin du détail apres la virgule)
			bombCount += 1;
		}
	}

	public int getAmmo() {
		return ammo;
	}

	public void bombUpdate() {
		ArrayList<Bomb> valueToRemove=new ArrayList<>();
		for(Bomb b : bombList){
			if(System.currentTimeMillis() - b.getStartTime() > 3900) {
				board.getCases()[(int)b.position.x][(int)b.position.y].setBomb(null);
				valueToRemove.add(b);
				System.out.println("bomb delete");
				bombCount -= 1;
				//TODO: bomb not disappearing after kick
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3800){
				b.setSpriteIndex(0);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3700){
				b.setSpriteIndex(1);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3600){
				b.setSpriteIndex(2);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3500){
				b.setSpriteIndex(3);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3400){
				b.setSpriteIndex(4);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3300){
				b.setSpriteIndex(3);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3200){
				b.setSpriteIndex(2);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3100){
				b.setSpriteIndex(1);
			}
			else if(System.currentTimeMillis() - b.getStartTime() > 3000){
				b.explode();
				b.setSpriteIndex(0);
			}
			if(b.isKicked()) {
				if ((int) (b.position.x + b.getKick().getVelocity().x) >= 13 || (int) (b.position.y + b.getKick().getVelocity().y) >= 15) {
					b.stopKick();
				} else {
					if (board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getWall() != null) {
						b.stopKick();
					}
					if (board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().size() > 0 && (board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().size() != 1 || !board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().get(0).equals(this))) {
						
						b.setStartTime(System.currentTimeMillis() - 3000);
						//board.getCases()[(int) b.position.x][(int) b.position.y].setBomb(null);
						//System.out.println("bomb delete");
					} else if ((int) b.position.x + b.getKick().getVelocity().x != (int) b.position.x || (int) b.position.y + b.getKick().getVelocity().y != (int) b.position.y) {
						board.getCases()[(int) b.position.x][(int) b.position.y].setBomb(null);
						board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].setBomb(b);
					}
					b.setPosition(b.position.x + b.getKick().getVelocity().x, +b.position.y + b.getKick().getVelocity().y);
				}
			}
		}
		bombList.removeAll(valueToRemove);
	}

	public Board getBoard() {
		return board;
	}
    
    public void addFirepower(boolean max) {
    	this.firepower = (max)?6:this.firepower+1;
    	if(this.firepower>6)this.firepower=6;
    }
    
    public void addAmmo() {
    	this.ammo += 1;
    }
    
    public void addSpeed() {
    	this.speed += 0.5;
    }
    

	public void setPierce(boolean pierce) {
		this.pierce = pierce;
	}
	
	public boolean getPierce() {
		return this.pierce;
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

	public int getFirepower() {
		return this.firepower;
	}

	public int getPoints(){
		return points;
	}
}