package model;

import java.util.Iterator;

/**
 * FlyingMonster is a monster which moves in the board by columns and rows.
 * It can fly through breakable walls.
 * directions. It can target an enemy if it is nearby.
 *  
 */
public class FlyingMonster extends Monster implements AI {
	private static final int TYPE = 1;
	public static float speed = 0.3F; // speed of walking monster
	/**
	 * Constructor of object FlyingMonster
	 * @param x position x
	 * @param y position y
	 * @param board board
	 */
    public FlyingMonster(float x, float y,Board board) {
        super(x, y);
        this.board = board;
		this.isAlive = true;
		this.thinkTime = 50; // time of the bot to think a new action
		this.spriteTimer = 0;
    }
	/**
	 * get the TYPE of object
	 */
	public int getType() {
		return TYPE;
	}

	/**
	 * get the speed of the object
	 * @return a float
	 */
	public static float getSpeed() {
		return speed;
	}

	/**
	 * set the speed of the object
	 * @param speed
	 */
	public static void setSpeed(float speed) {
		FlyingMonster.speed = speed;
	}


	// TODO: 12/05/2022 commenter kieran
	public void update(double deltaTime) {
		if (isAlive() && isset) { // if the monster is alive and was set on board
			spriteTimer += speed;
			if (spriteTimer >= 5) {
                spriteIndex++; // update the current image according to a spriteTImer
                spriteTimer = 0;
            }
			if (spriteIndex >= 3) {
				spriteIndex = 0;
			}
			killPlayers(); // kill players on current board
			nextInvoke = (++nextInvoke)%thinkTime;
			if(nextInvoke == 1 && !move) { // calculate whenever we get to choose the next move
        		chooseDirection();
				move = true;
        	}
			if (!move) spriteIndex = 0;
			// check collisions while moving
			if(direction == 0) {
				detectCollisionDown(deltaTime);
			}
			else if (direction == 1) {
				detectCollisionUp(deltaTime);
			}
			else if (direction == 2) {
				detectCollisionLeft(deltaTime);
			}
			else if (direction == 3) {
				detectCollisionRight(deltaTime);
			}
		}
		if (!isAlive() && isset) { // if the monste isn't alive
			if (spriteTimer++ >= 15) { // death animations
                spriteIndex++;
				if (spriteIndex == 4) {
					spriteIndex = 0;
					isset =false;
					dead = true; // end of death animations
				}
			}
		}
	}

	/**
	 * move the player right if there's no wall,considering the hitbox
	 * @param d is used to establish speedDelta.
	 */
    @Override
    public void detectCollisionRight(double d) {
        double speedDelta=speed/d;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
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
		else if(!(board.getCases()[line][nextColumn].getWall() !=null && !board.getCases()[line][nextColumn].getWall().isBreakable())){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y+=speedDelta;
				position.y=roundFloat(position.y);
			} else {
				stop();
			}
		} 
		else{
			position.y=nextColumn-hitboxWidthRight-0.01F;
			position.y=roundFloat(position.y);
			stop();
		}
		if (board.getCases()[line][nextColumn].getBomb() != null) {
			direction = 2;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
    }
	/**
	 * move the player up if there's no wall,considering the hitbox
	 * @param d is used to establish speedDelta.
	 */
    @Override
    public void detectCollisionUp(double d) {
        double speedDelta=speed/d;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line-1;
		double nextX=position.x-speedDelta;
		// detect if the player is still on the same case after moving
		if((nextX-hitboxHeightTop)>line){
			position.x-=speedDelta;
			position.x=roundFloat(position.x);
		}
		else if(!(board.getCases()[nextLine][column].getWall() !=null && !board.getCases()[nextLine][column].getWall().isBreakable())){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x-=speedDelta;
				position.x=roundFloat(position.x);
			} else {
				stop();
			}
		} else {
			position.x=line+hitboxHeightTop+0.01F;
			position.x=roundFloat(position.x);
			stop();
		}
		if (board.getCases()[nextLine][column].getBomb() != null) {
			direction = 0;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }
	/**
	 * move the player left if there's no wall,considering the hitbox
	 * @param d is used to establish speedDelta.
	 */
    @Override
    public void detectCollisionLeft(double d) {
        double speedDelta=speed/d;
		board.getCases()[(int) position.x][(int) position.y].deleteMovableOnCase(this);
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
		else if(!(board.getCases()[line][nextColumn].getWall() !=null && !board.getCases()[line][nextColumn].getWall().isBreakable())){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y-=speedDelta;
				position.y=roundFloat(position.y);
			} else {
				stop();
			}
		} else {
			position.y=column+hitboxWidthLeft+0.01F;
			position.y=roundFloat(position.y);
			stop();
		}
		if (board.getCases()[line][nextColumn].getBomb() != null) {
			direction = 3;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }
	
	/**
	 * move the player down if there's no wall, considering hitbox
	 * @param d is used to establish speedDelta.
	 */
    @Override
    public void detectCollisionDown(double d) {
        double speedDelta=speed/d;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line+1;
		double nextX=position.x+speedDelta;
		// detect if player stay in the same case after moving
		if((nextX+hitboxHeightBottom)<nextLine){
			position.x+=speedDelta;
			position.x=roundFloat(position.x);
		}
		// detect if there isn't a wall on the next case
		else if(!(board.getCases()[nextLine][column].getWall() !=null && !board.getCases()[nextLine][column].getWall().isBreakable())){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x+=speedDelta;
				position.x=roundFloat(position.x);
			} else {
				stop();
			}
		} else {
			position.x=nextLine-hitboxHeightBottom-0.01F;
			position.x=roundFloat(position.x);
			stop();
		}
		if (board.getCases()[nextLine][column].getBomb() != null) {
			direction = 1;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

	/**
	 * set moving to false
	 */
    @Override
    public void stop() {
    	move = false;
    }

	/**
	 * Determine if the player should move on the 
	 * row direction, col direction or randomly
	 * Stock direction in a variable direction
	 */
    @Override
    public void chooseDirection() {
		if (direction == -1) {
			direction = randi.nextInt(4);
		}
		else{
			int v;
			int c;
			int d=randi.nextInt(2);
			if (d == 0) {
				v = calculateRowDirection();
				if(v != -1){
					direction = v;
				}
				else if((c=calculateColDirection())!=-1){
					direction=c;
				}
				else{
					direction=randi.nextInt(4);
				}
			}
			else{
				c = calculateColDirection();
				if(c != -1){
					direction = c;
				}
				else if((v=calculateRowDirection())!=-1){
					direction = v;
				}
				else{
					direction=randi.nextInt(4);
				}
			}
		}
	}

	/**
	 * Check if the monster can move in any columns
	 * @return the direction
	 */
	protected int calculateColDirection() {
		int x = (int)position.x;
		int y = (int)position.y;
		int xx;
		for(int i = 1; i < board.getCases().length; i++){
			xx = x + i;
			if(xx < 0 || xx >= board.getCases().length) break;
			Case t = board.getCases()[xx][y];
			Iterator<Movable> iter = t.getMovablesOnCase().iterator();
			while(iter.hasNext()) {
				Movable m = iter.next();
				if (m instanceof Player) {
					return 0;
				}
			}
		}
		for(int i = 1; i < board.getCases().length; i++){
			xx = x - i;
			if(xx < 0 || xx >= board.getCases().length) break;
			Case t = board.getCases()[xx][y];
			Iterator<Movable> iter = t.getMovablesOnCase().iterator();
			while(iter.hasNext()) {
				Movable m = iter.next();
				if (m instanceof Player) {
					return 1;
				}
			}

		}
		return -1;
	}
	/**
	 * Check if the monster can move in rows
	 * @return direction
	 */
	protected int calculateRowDirection() {
		int x = (int)position.x;
		int y = (int)position.y;
		int yy;
		Case t;
		for(int i = 1; i < board.getCases()[0].length; i++){
			yy = y + i;
			if(yy < 0 || yy >= board.getCases()[0].length) break;
			t = board.getCases()[x][yy];
			Iterator<Movable> iter = t.getMovablesOnCase().iterator();
			while(iter.hasNext()) {
				Movable m = iter.next();
				if (m instanceof Player) {
					return 2;
				}
			}

		}
		for(int i = 1; i < board.getCases()[0].length; i++){
			yy = y - i;
			if(yy < 0 || yy >= board.getCases()[0].length) break;
			t = board.getCases()[x][yy];
			Iterator<Movable> iter = t.getMovablesOnCase().iterator();
			while(iter.hasNext()) {
				Movable m = iter.next();
				if (m instanceof Player) {
					return 3;
				}
			}

		}
		return -1;
	}


    @Override
    public boolean detectDiagonalCollisionRightLeft(int line, int nextColumn) {
		// detect if the diagonal left case is empty
		if(position.x%1<hitboxHeightTop && !(board.getCases()[line-1][nextColumn].getWall()!=null && !board.getCases()[line-1][nextColumn].getWall().isBreakable())){
			return true;
		}
		// detect if the diagonal right case is empty
		else if(position.x%1>1-hitboxHeightBottom && !(board.getCases()[line+1][nextColumn].getWall()!=null && !board.getCases()[line+1][nextColumn].getWall().isBreakable())){
			return true;
		}
		// detect if the player is in the center of the case
		else if(position.x%1>hitboxHeightTop && position.x%1<1-hitboxHeightBottom){
			return true;
		}
		return false;
    }

	/**
	 * 	detect diagonal collision when player is between two cases
	 */
	@Override
	public boolean detectDiagonalCollisionUpDown(int nextLine, int column) {
		// detect if the diagonal top case is empty
		if(position.y%1<hitboxWidthLeft && !(board.getCases()[nextLine][column-1].getWall()!=null && !board.getCases()[nextLine][column-1].getWall().isBreakable())){
			return true;
		}
		// detect if the diagonal bottom case is empty
		else if(position.y%1>1-hitboxWidthRight && !(board.getCases()[nextLine][column+1].getWall()!=null && !board.getCases()[nextLine][column+1].getWall().isBreakable())){
			return true;
		}
		// detect if the player is in the center of the case
		else if(position.y%1>hitboxWidthLeft && position.y%1<1-hitboxWidthRight){
			return true;
		}
		return false;
    }

}
