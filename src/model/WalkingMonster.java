package model;

/**
 * WalkingMonster is a type of monster which can move fast.
 * It doesn't walk fly on walls.
 */
public class WalkingMonster extends Monster implements AI{
	static final int TYPE = 0; // id of a monster
	public static float speed = 0.75F; // speed of a walking monster
	/**
	 * Constructor of an walkingmonster object
	 * @param x its coordinates
	 * @param y
	 * @param board the board of the game
	 */
    public WalkingMonster(float x, float y,Board board) {
		// create an object as walking monster
        super(x, y);
        this.board = board;
		isAlive = true;
		nextInvoke = 0;
		thinkTime = 20; 
    }
	/**
	 * get the id type of the walking monster
	 */
	public int getType() {
		return TYPE;
	}
	/**
	 * get the speed of a monster
	 * @return the speed as float 
	 */
	public static float getSpeed() {
		return speed;
	}
	/**
	 * set the spee of the object
	 * @param speed
	 */
	public static void setSpeed(float speed) {
		FlyingMonster.speed = speed;
	}

	/**
	 * Main function of the program updating the monster 
	 */
	public void update(double deltaTime) {
		if (isAlive && isset) {
			killPlayers();
			spriteTimer += speed;
			if (spriteTimer >= 20) { // make animations fluid
                spriteIndex++; // update alive animations
                spriteTimer = 0;
            }
			if (spriteIndex >= 3) spriteIndex = 0;
			nextInvoke = (++nextInvoke)%thinkTime; 
			if (nextInvoke == 1 && !move) { // next time to invoke actions
				chooseDirection();
				move = true;
        	}
			if (!move) spriteIndex = 0;
			// detect collisions while moving
			if(direction == 0) { // down
				detectCollisionDown(deltaTime);
			}else if (direction == 1) { // up
				detectCollisionUp(deltaTime);
			} else if (direction == 2) { // left
				detectCollisionLeft(deltaTime);
			} else if (direction == 3) { // right
				detectCollisionRight(deltaTime);
			}
		}
		if (!isAlive && isset) {
			if (spriteTimer++ >= 15) { // update death animations
                spriteIndex++;
				if (spriteIndex == 4) { // end of death animation, proceeding to reset
					spriteIndex = 0;
					isset =false;
					dead = true;
					return;
				}
			}
		}
	}

    @Override
	// move the player right if there's no wall, considering hitbox
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
		else if(board.getCases()[line][nextColumn].getWall()==null && board.getCases()[line][nextColumn].getBomb() == null){
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

    @Override
	// move the player up if there's no wall, considering hitbox
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
		// detect if there isn't a wall on the next case
		else if(board.getCases()[nextLine][column].getWall()==null && board.getCases()[nextLine][column].getBomb() == null){
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

    @Override
	// move the player left if there's no wall, considering hitbox
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
		else if(board.getCases()[line][nextColumn].getWall()==null && board.getCases()[line][nextColumn].getBomb()==null){
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
		// detect if there isn't a wall on the next case
		} else if(board.getCases()[nextLine][column].getWall()==null && board.getCases()[nextLine][column].getBomb() == null){
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
	 * stops the movements of the monster
	 */
    @Override
    public void stop() {
    	move = false;
    }
	/**
	 * Determinate the new direction of the monster which is
	 * different from the current direction
	 */
    @Override
    public void chooseDirection() {
		if (direction == -1) {
			direction = randi.nextInt(4);
			return;
		}
		int d;
        do {
			d=randi.nextInt(4);
		} while ((direction == d && !move));
		direction=d;
    }


    @Override
    public boolean detectDiagonalCollisionRightLeft(int line, int nextColumn) {
		// detect if the diagonal top case is empty
		if(position.x%1<hitboxHeightTop &&  board.getCases()[line-1][nextColumn].getWall()==null){
			return true;
		}
		// detect if the diagonal bottom case is empty
		else if(position.x%1>1-hitboxHeightBottom && board.getCases()[line+1][nextColumn].getWall()==null){
			return true;
		}
		// detect if the player is in the center of the case
		else if(position.x%1>hitboxHeightTop && position.x%1<1-hitboxHeightBottom){
			return true;
		}
		return false;
    }

    @Override
    public boolean detectDiagonalCollisionUpDown(int nextLine, int column) {
		// detect if the diagonal top case is empty
		if(position.y%1<hitboxWidthLeft && board.getCases()[nextLine][column-1].getWall()==null){
			return true;
		}
		// detect if the diagonal bottom case is empty
		else if(position.y%1>1-hitboxWidthRight && board.getCases()[nextLine][column+1].getWall()==null){
			return true;
		}
		// detect if the player is in the center of the case
		else if(position.y%1>hitboxWidthLeft && position.y%1<1-hitboxWidthRight){
			return true;
		}
		return false;
    }

}
