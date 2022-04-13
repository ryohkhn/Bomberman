package model;

public class WalkingMonster extends Monster implements AI{
	final int type = 0;

    public WalkingMonster(float x, float y,Board board) {
        super(x, y);
        this.board = board;
		isAlive = true;
		nextInvoke = 0;
    }

	public void update(double deltaTime) {
		if (isAlive() && isset) {
			killPlayers();
			nextInvoke = (++nextInvoke)%thinkTime;
			if(nextInvoke == 1) {
				if (!move) {
        			chooseDirection();
					move = true;
				}
        	}
			//System.out.println(direction);
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
	}

    @Override
    public void detectCollisionRight(double d) {
		//System.out.println("right");
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
		else if(board.getCases()[line][nextColumn].getWall()==null && board.getCases()[line][nextColumn].getBomb() == null){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y+=speedDelta;
				position.y=roundFloat(position.y);
			} else {
				stop();
			}
		} 
		else{
			position.y+=nextColumn-(nextY+hitboxWidthRight);
			position.y=roundFloat(position.y);
			stop();
		}
		//System.out.println("update");
		//System.out.println(direction);
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
    }

    @Override
    public void detectCollisionUp(double d) {
		//System.out.println("up");
        // TODO Auto-generated method stub
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
		else if(board.getCases()[nextLine][column].getWall()==null && board.getCases()[nextLine][column].getBomb() == null){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x-=speedDelta;
				position.x=roundFloat(position.x);
			} else {
				stop();
			}
		} else {
			stop();
		}
		//System.out.println("update");
		//System.out.println(direction);
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void detectCollisionLeft(double d) {
		//System.out.println("left");
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
		else if(board.getCases()[line][nextColumn].getWall()==null && board.getCases()[line][nextColumn].getBomb()==null){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y-=speedDelta;
				position.y=roundFloat(position.y);
			} else {
				stop();
			}
		} else {
			stop();
		}
		//System.out.println("update");
		//System.out.println(direction);
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
    }

    @Override
    public void detectCollisionDown(double d) {
		//System.out.println("down");
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
		} else if(board.getCases()[nextLine][column].getWall()==null && board.getCases()[nextLine][column].getBomb() == null){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x+=speedDelta;
				position.x=roundFloat(position.x);
			} else {
				stop();
			}
		} else {
			stop();
		}
		//System.out.println("update");
		//System.out.println(direction);
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void killPlayers() {
        // TODO Auto-generated method stub
		int line= (int)position.x;
		int column= (int)position.y;
		board.getCases()[line][column].killPlayers();
        
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
    	move = false;
    }

    @Override
    public void chooseDirection() {
		if (direction == -1) {
			direction = randi.nextInt(3);
			return;
		}
		int d = direction;
        do {
			d = randi.nextInt(5);
			//System.out.println("ab");
		} while ((direction == d && !move) || d == 4);
		if (d == 5) spriteIndex = 0;
		if (d != 5 && d != -1) direction = d;
		//System.out.println("lol" + direction);
    }


    @Override
    public boolean detectDiagonalCollisionRightLeft(int line, int nextColumn) {
        // TODO Auto-generated method stub
		if(position.x%1<hitboxHeightTop &&  board.getCases()[line-1][nextColumn].getWall()==null){
			return true;
		}
		else if(position.x%1>1-hitboxHeightBottom && board.getCases()[line+1][nextColumn].getWall()==null){
			return true;
		}
		else if(position.x%1>hitboxHeightTop && position.x%1<1-hitboxHeightBottom){
			return true;
		}
		return false;
    }

    @Override
    public boolean detectDiagonalCollisionUpDown(int nextLine, int column) {
        // TODO Auto-generated method stub
		if(position.y%1<hitboxWidthLeft && board.getCases()[nextLine][column-1].getWall()==null){
			return true;
		}
		else if(position.y%1>1-hitboxWidthRight && board.getCases()[nextLine][column+1].getWall()==null){
			return true;
		}
		else if(position.y%1>hitboxWidthLeft && position.y%1<1-hitboxWidthRight){
			return true;
		}
		return false;
    }

    @Override
    public float roundFloat(float f) {
        return (float)(Math.round((f)*100.0)/100.0);
    }

}
