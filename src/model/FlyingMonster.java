package model;


public class FlyingMonster extends Monster implements AI {
	private static final int TYPE = 1;
	public static float speed = 0.45F;

    public FlyingMonster(float x, float y,Board board) {
        super(x, y);
        this.board = board;
		this.isAlive = true;
		this.thinkTime = 50;
		this.spriteTimer = 0;
    }

	public int getType() {
		return TYPE;
	}

	public static float getSpeed() {
		return speed;
	}

	public static void setSpeed(float speed) {
		FlyingMonster.speed = speed;
	}

	public void update(double deltaTime) {
		//System.out.println("terminatior (x,y) :" + (int)position.x + "," + (int)position.y);
		if (isAlive() && isset) {
			spriteTimer += speed;
			//System.out.println("spritetimer : " + spriteIndex);
			if (spriteTimer >= 5) {
				//System.out.println("spriteindex : " + spriteIndex);
                spriteIndex++;
                spriteTimer = 0;
            }
			if (spriteIndex >= 3) {
				spriteIndex = 0;
			}
			killPlayers();
			nextInvoke = (++nextInvoke)%thinkTime;
			if(nextInvoke == 1 && !move) {
        		chooseDirection();
				move = true;
        	}
			if (!move) spriteIndex = 0;
			if(direction == 0) {
				detectCollisionDown(deltaTime);
			}else if (direction == 1) {
				detectCollisionUp(deltaTime);
			} else if (direction == 2) {
				detectCollisionLeft(deltaTime);
			} else if (direction == 3) {
				detectCollisionRight(deltaTime);
			}
		}
		if (!isAlive() && isset) {
			if (spriteTimer++ >= 15) {
                spriteIndex++;
				if (spriteIndex == 4) {
					spriteIndex = 0;
					isset =false;
					return;
				}
			}
		}
	}

    @Override
    public void detectCollisionRight(double d) {
        double speedDelta=speed/d;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextColumn=column+1;
		double nextY=position.y+speedDelta;
		if((nextY+hitboxWidthRight)<nextColumn){
			position.y+=speedDelta;
			position.y=roundFloat(position.y);
		}
		else if(!(board.getCases()[line][nextColumn].getWall() !=null && !board.getCases()[line][nextColumn].getWall().isBreakable())){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y+=speedDelta;
				position.y=roundFloat(position.y);
			} else {
				stop();
			}
		} 
		else{
			stop();
		}
		if (board.getCases()[line][nextColumn].getBomb() != null) {
			direction = 2;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
    }

    @Override
    public void detectCollisionUp(double d) {
        double speedDelta=speed/d;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextLine=line-1;
		double nextX=position.x-speedDelta;
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
			stop();
		}
		if (board.getCases()[nextLine][column].getBomb() != null) {
			direction = 0;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void detectCollisionLeft(double d) {
        double speedDelta=speed/d;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
		board.getCases()[(int) position.x][(int) position.y].deleteMovableOnCase(this);
		int line=(int) position.x;
		int column=(int) position.y;
		int nextColumn=column-1;
		double nextY=position.y-speedDelta;
		if((nextY-hitboxWidthLeft)>column){
			position.y-=speedDelta;
			position.y=roundFloat(position.y);
		}
		else if(!(board.getCases()[line][nextColumn].getWall() !=null && !board.getCases()[line][nextColumn].getWall().isBreakable())){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y-=speedDelta;
				position.y=roundFloat(position.y);
			} else {
				stop();
			}
		} else {
			stop();
		}
		if (board.getCases()[line][nextColumn].getBomb() != null) {
			direction = 3;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void detectCollisionDown(double d) {
        // TODO Auto-generated method stub
        double speedDelta=speed/d;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
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
		else if(!(board.getCases()[nextLine][column].getWall() !=null && !board.getCases()[nextLine][column].getWall().isBreakable())){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x+=speedDelta;
				position.x=roundFloat(position.x);
			} else {
				stop();
			}
		} else {
			stop();
		}
		if (board.getCases()[nextLine][column].getBomb() != null) {
			direction = 1;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void killPlayers() {
		int line= (int)position.x;
		int column= (int)position.y;
		board.getCases()[line][column].killPlayers();
        
    }

    @Override
    public void stop() {
    	move = false;
    }

    @Override
    public void chooseDirection() {
		int d = randi.nextInt(2);
		if (d == 1) {
			int v = calculateRowDirection();
			if(v != -1) direction = v;
			else direction = calculateColDirection();
		} else if (d == 2) {
			int h = calculateColDirection();
			if(h != -1)direction = h;
			else direction = calculateRowDirection();
		}

		if (direction == -1) {
			direction = randi.nextInt(3);
		}
	}

	protected int calculateColDirection() {
		int x = (int)position.x;
		int y = (int)position.y;
		int xx;
		for(int i = 1; i < board.getCases().length; i++){
			xx = x + i;
			if(xx < 0 || xx >= board.getCases().length) break;
			Case t = board.getCases()[xx][y];
			for (Movable m :t.getMovablesOnCase()) {
				if (m instanceof Player) {
					System.out.println("col bas victime (x,y) :" + xx + "," + y);
					return 0;
				}
			}

		}
		for(int i = 1; i < board.getCases().length; i++){
			xx = x - i;
			if(xx < 0 || xx >= board.getCases().length) break;
			Case t = board.getCases()[xx][y];
			for (Movable m :t.getMovablesOnCase()) {
				if (m instanceof Player) {
					//System.out.println("col haut victime (x,y) :" + xx + "," + y);
					return 1;
				}
			}

		}
		return -1;
	}

	protected int calculateRowDirection() {
		int x = (int)position.x;
		int y = (int)position.y;
		int yy;
		Case t;
		for(int i = 1; i < board.getCases()[0].length; i++){
			yy = y + i;
			if(yy < 0 || yy >= board.getCases()[0].length) break;
			t = board.getCases()[x][yy];
			for (Movable m :t.getMovablesOnCase()) {
				if (m instanceof Player) {
					System.out.println("ligne droite victime (x,y) :" + x + "," + yy);
					return 3;
				}
			}

		}
		for(int i = 1; i < board.getCases()[0].length; i++){
			yy = y - i;
			if(yy < 0 || yy >= board.getCases()[0].length) break;
			t = board.getCases()[x][yy];
			for (Movable m :t.getMovablesOnCase()) {
				if (m instanceof Player) {
					System.out.println("ligne gauche victime (x,y) :" + x + "," + yy);
					return 2;
				}
			}

		}
		return -1;
	}


    @Override
    public boolean detectDiagonalCollisionRightLeft(int line, int nextColumn) {
		if(position.x%1<hitboxHeightTop && !(board.getCases()[line-1][nextColumn].getWall()!=null && !board.getCases()[line-1][nextColumn].getWall().isBreakable())){
			return true;
		}
		else if(position.x%1>1-hitboxHeightBottom && !(board.getCases()[line+1][nextColumn].getWall()!=null && !board.getCases()[line+1][nextColumn].getWall().isBreakable())){
			return true;
		}
		else if(position.x%1>hitboxHeightTop && position.x%1<1-hitboxHeightBottom){
			return true;
		}
		return false;
    }

    @Override
    public boolean detectDiagonalCollisionUpDown(int nextLine, int column) {
		if(position.y%1<hitboxWidthLeft && !(board.getCases()[nextLine][column-1].getWall()!=null && !board.getCases()[nextLine][column-1].getWall().isBreakable())){
			return true;
		}
		else if(position.y%1>1-hitboxWidthRight && !(board.getCases()[nextLine][column+1].getWall()!=null && !board.getCases()[nextLine][column+1].getWall().isBreakable())){
			return true;
		}
		else if(position.y%1>hitboxWidthLeft && position.y%1<1-hitboxWidthRight){
			return true;
		}
		return false;
    }

}
