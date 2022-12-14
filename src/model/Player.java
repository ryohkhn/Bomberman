package model;

import java.util.ArrayList;


public class Player extends GameObject implements Movable{
    private final int id;
    private int bombCount;

    private ArrayList<Bomb> bombList = new ArrayList<>();
	private boolean alive;
    private float speed = 1F;
    private int keyUp, keyDown, keyLeft, keyRight,keyAction;
    private boolean pressDown = false, pressUp = false, pressLeft = false, pressRight = false, pressAction = false;
	private int spriteTimer;
	private int spriteIndex;
	private int direction;
	private int points=0;

	private int ammo = 1;
	private boolean kick = false;
	private boolean pierce = false;
	private boolean isset;
	private int firepower = 1;
	private boolean coop;

	private final Board board;
	private boolean dead;

	/**
	 * Constructor of an object player
	 * @param id the id of player
	 * @param x the row position 
	 * @param y the column position
	 * @param board the board of the game
	 * @param isAi boolean if the object player is a bot (unfinished)
	 */
    public Player(int id,float x,float y, Board board, boolean isAi) {
        super(x,y);
    	this.id = id;
    	this.alive = true;
        position.x = x;
        position.y = y;
		this.board=board;
		this.direction = 0;
		this.spriteIndex = 0;
		this.setPosition(x, y);
    }

	/**
	 * Model updater for players. This function calls collisions detector and change player's sprite index.
	 * @param deltaTime is used to establish speedDelta.
	 */
	@Override
	public void update(double deltaTime) {
		if (alive && isset) { // if player is alive and was set on board
			if ((spriteTimer += speed) >= 12) {
                spriteIndex++; // update the current image according to a spriteTimer
                spriteTimer = 0;
            }
            if ((!pressUp && !pressDown && !pressLeft && !pressRight) || (this.spriteIndex >= 3)) {
				// if the player doesn't move/ is standing
                spriteIndex = 0;
            }
			// checking collisions for each movements
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
		if (!alive && isset) { // if not alive, show death animations
			if (spriteTimer++>=12) {
                spriteIndex++;
				if (spriteIndex==8) {
					spriteIndex=0; // no more death animations
					isset=false;
					dead = true;
					return;
				}
                spriteTimer=0;
			}
		}
	}

	/**
	 * Stock inputs into class attributes
	 * @param up up key
	 * @param down down key
	 * @param left left key
	 * @param right right key
	 * @param action action key (to drop bomb)
	 */
    public void bindKeys(int up, int down, int left, int right, int action) {
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		keyAction = action;
	}

	/**
	 * Move the player down if there's no wall, considering hitbox
	 * @param deltaTime is used to establish speedDelta.
	 */
	@Override
    public void detectCollisionDown(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 0;
		Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line+1;
		double nextX=position.x+speedDelta;
		// detect if the player is still on the same case after moving
		if((nextX+hitboxHeightBottom)<nextLine){
			position.x+=speedDelta;
			position.x=roundFloat(position.x);
		}
		// detect if there isn't a wall on the next case
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
		else{
			position.x=nextLine-hitboxHeightBottom-0.01F;
			position.x=roundFloat(position.x);
		}
		if(Board.cases[nextLine][column].getBomb()!=null && this.kick && Board.cases[nextLine][column].getBomb().getSpriteIndex() == -1) {
			Board.cases[nextLine][column].getBomb().setKicked(true,KickDirection.FromTop);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}

	/**
	 * Move the player up if there's no wall, considering hitbox
	 * @param deltaTime is used to establish speedDelta.
	 */
	@Override
	public void detectCollisionUp(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 3;
		Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line-1;
		double nextX=position.x-speedDelta;
		// detect if the player is still on the same case after moving
		if((nextX-hitboxHeightTop)>line){
			position.x-=speedDelta;
			position.x=roundFloat(position.x);
		}
		// detect if there isn't a wall on the next case
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
		else{
			position.x=line+hitboxHeightTop+0.01F;
			position.x=roundFloat(position.x);
		}
		if(Board.cases[nextLine][column].getBomb()!=null && this.kick && Board.cases[nextLine][column].getBomb().getSpriteIndex() == -1) {
			Board.cases[nextLine][column].getBomb().setKicked(true,KickDirection.FromBottom);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}
	
	/**
	 * Move the player left if there's no wall, considering hitbox
	 * @param deltaTime is used to establish speedDelta.
	 */
	@Override
	public void detectCollisionLeft(double deltaTime){
		double speedDelta=speed/deltaTime;
		direction=2;
		Board.cases[(int) position.x][(int) position.y].deleteMovableOnCase(this);
		int line=(int) position.x;
		int column=(int) position.y;
		int nextColumn=column-1;
		double nextY=position.y-speedDelta;
		// detect if the player is still on the same case after moving
		if((nextY-hitboxWidthLeft)>column){
			position.y-=speedDelta;
			position.y=roundFloat(position.y);
		}
		// detect if there isn't a wall on the next case
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
		else{
			position.y=column+hitboxWidthLeft+0.01F;
			position.y=roundFloat(position.y);
		}
		if(Board.cases[line][nextColumn].getBomb()!=null && this.kick && Board.cases[line][nextColumn].getBomb().getSpriteIndex()==-1){
			Board.cases[line][nextColumn].getBomb().setKicked(true, KickDirection.FromRight);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}

	/**
	 * Move the player right if there's no wall, considering hitbox
	 * @param deltaTime is used to establish speedDelta.
	 */
	@Override
	public void detectCollisionRight(double deltaTime) {
		double speedDelta=speed/deltaTime;
		direction = 1;
		Board.cases[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextColumn=column+1;
		double nextY=position.y+speedDelta;
		// detect if the player is still on the same case after moving
		if((nextY+hitboxWidthRight)<nextColumn){
			position.y+=speedDelta;
			position.y=roundFloat(position.y);
		}
		// detect if there isn't a wall on the next case
		else if(Board.cases[line][nextColumn].getWall()==null && Board.cases[line][nextColumn].getBomb() == null){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y+=speedDelta;
				position.y=roundFloat(position.y);
			}
			if(Board.cases[line][nextColumn].getBonus()!=null){
				Board.cases[line][nextColumn].getBonus().grantBonus(this);
				Board.cases[line][nextColumn].setBonus(null);
			}
		}
		else{
			position.y=nextColumn-hitboxWidthRight-0.01F;
			position.y=roundFloat(position.y);
		}
		if(Board.cases[line][nextColumn].getBomb()!=null && this.kick && Board.cases[line][nextColumn].getBomb().getSpriteIndex() == -1) {
			Board.cases[line][nextColumn].getBomb().setKicked(true,KickDirection.FromLeft);
		}
		Board.cases[(int)position.x][(int)position.y].addMovableOnCase(this);
	}

	/**
	 * 	Detect diagonal collision when player is between two cases
	 */
	@Override
	public boolean detectDiagonalCollisionUpDown(int nextLine,int column){
		// detect if the diagonal left case is empty
		if(position.y%1<=hitboxWidthLeft && Board.cases[nextLine][column-1].getWall()==null){
			return true;
		}
		// detect if the diagonal right case is empty
		else if(position.y%1>=(1-hitboxWidthRight) && Board.cases[nextLine][column+1].getWall()==null){
			return true;
		}
		// detect if the player is in the center of the case
		else if(position.y%1>=hitboxWidthLeft && position.y%1<=(1-hitboxWidthRight)){
			return true;
		}
		return false;
	}

	/**
	 * 	Detect diagonal collision when player is between two cases
	 */
	@Override
	public boolean detectDiagonalCollisionRightLeft(int line,int nextColumn){
		// detect if the diagonal top case is empty
		if(position.x%1<=hitboxHeightTop && Board.cases[line-1][nextColumn].getWall()==null){
			return true;
		}
		// detect if the diagonal bottom case is empty
		else if(position.x%1>=(1-hitboxHeightBottom) && Board.cases[line+1][nextColumn].getWall()==null){
			return true;
		}
		// detect if the player is in the center of the case
		else if(position.x%1>=hitboxHeightTop && position.x%1<=(1-hitboxHeightBottom)){
			return true;
		}
		return false;
	}

	@Override
	public float roundFloat(float f){
		return (float)(Math.round((f)*100.0)/100.0);
	}

	/**
	 * Adds dropped bomb to player bombs list and increase the count.
	 */
	protected void dropBomb() {
		if(bombCount < this.ammo && (this.board.getCases()[(int)position.x][(int)position.y].getBomb()==null)){
			bombList.add(new Bomb((int)position.x,(int)position.y, this, board));
			bombCount += 1;
		}
	}

	/**
	 * Update bombs :
	 * change the sprite index for the explosion.
	 * give points when bombs kill a player.
	 * operate kick action.
	 * @return the counts of bombs who exploded
	 */
	protected int bombUpdate() {
		int bombsExploded = 0;
		ArrayList<Bomb> valueToRemove=new ArrayList<>();
		for(Bomb b : bombList){
			double bombTimer = System.currentTimeMillis() - b.getStartTime();
			// bomb sprite index updater
			// after 2 seconds, the sprite index change each 0,1 seconds
			if(bombTimer > 2900) {
				b.setFuse(false);
				board.getCases()[(int)b.position.x][(int)b.position.y].setBomb(null);
				valueToRemove.add(b);
				bombCount -= 1; //  decrement bomb count
			}
			else if(bombTimer > 2800){
				b.setSpriteIndex(0);
			}
			else if(bombTimer > 2700){
				b.setSpriteIndex(1);
			}
			else if(bombTimer > 2600){
				b.setSpriteIndex(2);
			}
			else if(bombTimer > 2500){
				b.setSpriteIndex(3);
			}
			else if(bombTimer > 2400){
				b.setSpriteIndex(4);
			}
			else if(bombTimer > 2300){
				b.setSpriteIndex(3);
			}
			else if(bombTimer > 2200){
				b.setSpriteIndex(2);
			}
			else if(bombTimer > 2100){
				b.setSpriteIndex(1);
			}
			else if(bombTimer > 2000){
				b.explode();
				bombsExploded += 1;
				b.setSpriteIndex(0);
			}

			// fuse bomb if an exploding bomb touches it
			if (b.getFuse()) {
				b.setStartTime(System.currentTimeMillis() - Math.max(bombTimer, 1999));
				b.setFuse(false);

			}

			//set points
			if (bombTimer > 2000 && bombTimer < 2800) { // call kill functions while bomb is exploding
				setPoints(getPoints() + b.kill());
			}

			// kick
			if(b.isKicked()) {
				if ((int) (b.position.x + b.getKick().getVelocity().x) >= 13 || (int) (b.position.y + b.getKick().getVelocity().y) >= 15) {
					b.stopKick();
				} else {
					// case where the bomb hit a wall.
					if (board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getWall() != null) {
						b.stopKick();
					}
					// case where the bomb hit a player who is not himself.
					else if (board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().size() > 0 && (board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().size() != 1 || !board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].getMovablesOnCase().get(0).equals(this))) {
						b.setStartTime(System.currentTimeMillis() - 2000);
						b.stopKick();
					}
					// case where the bomb doesn't reach any obstacles already.
					else if ((int) b.position.x + b.getKick().getVelocity().x != (int) b.position.x || (int) b.position.y + b.getKick().getVelocity().y != (int) b.position.y) {
						board.getCases()[(int) b.position.x][(int) b.position.y].setBomb(null);
						board.getCases()[(int) (b.position.x + b.getKick().getVelocity().x)][(int) (b.position.y + b.getKick().getVelocity().y)].setBomb(b);
					}
					b.setPosition(b.position.x + b.getKick().getVelocity().x, +b.position.y + b.getKick().getVelocity().y);
				}
			}
		}
		// suppressing bombs who already exploded
		bombList.removeAll(valueToRemove);

		return bombsExploded;
	}

	// Getters - Setters

	public void setAlive(boolean b) {
		if (!b) {
			spriteIndex = 0;
			this.direction = 4;
		}
		this.alive = b;
	}

	public void addFirepower(boolean max) {
		this.firepower += (max)?2:1;
		if(this.firepower>6)this.firepower=6;
	}

	public void setPlayer(boolean c) {
		isset = true;
		coop = c;
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

	public int getPositionXasInt() {
		return (int)super.getPositionX();
	}

	public int getPositionYasInt() {
		return (int)super.getPositionY();
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

	public boolean getCoop(){
		return coop;
	}

	public int getId() {
		return id;
	}

	public Board getBoard() {
		return board;
	}
    
    public void addAmmo() {
    	this.ammo += 1;
    }
    
    public void addSpeed() {
    	this.speed += 0.2;
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

	public boolean isAlive() {
		return this.alive;
	}

	public int getFirepower() {
		return this.firepower;
	}

	public int getPoints(){
		return points;
	}

	public ArrayList<Bomb> getBombList() {
		return this.bombList;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}