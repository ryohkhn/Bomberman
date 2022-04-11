package model;

public class MonstreDeux extends GameObject implements Monster,AI {
    private boolean isAlive;
    private int nextInvoke;
    private int thinkTime;
    private Board board;
    private int direction;
    private float speed = 1F;

    public MonstreDeux(float x, float y) {
        super(x, y);
    }

    
	public void update(double deltaTime) {
		if (isAlive()) {
			nextInvoke = (++nextInvoke)%thinkTime;
			killPlayers();
        	if(nextInvoke == 1) {
				stop();
        		chooseDirection(deltaTime);
        	}
		}
	}

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void detectCollisionRight(double d) {
        double speedDelta=speed/d;
		direction = 3;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
		int line= (int)position.x;
		int column= (int)position.y;
		int nextColumn=column+1;
		double nextY=position.y+speedDelta;
		if((nextY+hitboxWidthRight)<nextColumn){
			position.y+=speedDelta;
			position.y=roundFloat(position.y);
		}
		else if(!(Board.cases[line][nextColumn].getWall() !=null && !Board.cases[line][nextColumn].getWall().isBreakable())){
			if(detectDiagonalCollisionRightLeft(line,nextColumn)){
				position.y+=speedDelta;
				position.y=roundFloat(position.y);
			}
		}
		else{
			position.y+=nextColumn-(nextY+hitboxWidthRight);
			position.y=roundFloat(position.y);
			direction = -1;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void detectCollisionUp(double d) {
        // TODO Auto-generated method stub
        double speedDelta=speed/d;
		direction = 0;
		board.getCases()[(int)position.x][(int)position.y].deleteMovableOnCase(this);
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
		} else {
			direction = -1;
		}		
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void detectCollisionLeft(double d) {
        // TODO Auto-generated method stub
        double speedDelta=speed/d;
		//System.out.println("Kick :" + this.kick);
		//System.out.println("Pierce :" + this.pierce);
		direction=2;
		board.getCases()[(int) position.x][(int) position.y].deleteMovableOnCase(this);
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
		} else {
			direction = -1;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void detectCollisionDown(double d) {
        // TODO Auto-generated method stub
        double speedDelta=speed/d;
		direction = 1;
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
		else if(Board.cases[nextLine][column].getWall()==null && Board.cases[nextLine][column].getBomb() == null){
			if(detectDiagonalCollisionUpDown(nextLine,column)){
				position.x+=speedDelta;
				position.x=roundFloat(position.x);
			}
		} else {
			direction = -1;
		}
		board.getCases()[(int)position.x][(int)position.y].addMovableOnCase(this);
        
    }

    @Override
    public void killPlayers() {
        // TODO Auto-generated method stub
		int line= (int)position.x;
		int column= (int)position.y;
		board.getCases()[line][column].killMoveables();
        
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        direction = -1;
    }

    @Override
    public void chooseDirection(double d) {
        // TODO Auto-generated method stub
        if (direction == -1) direction = randi.nextInt(4);
        if(direction == 0) {
            detectCollisionDown(d);
        }else if (direction == 1) {
            detectCollisionUp(d);
        } else if (direction == 2) {
            detectCollisionLeft(d);
        } else if (direction == 3) {
            detectCollisionRight(d);
        }
        
    }


    @Override
    public boolean detectDiagonalCollisionRightLeft(int line, int nextColumn) {
        // TODO Auto-generated method stub
		if(position.x%1<hitboxHeightTop && (board.getCases()[line-1][nextColumn].getWall()==null && board.getCases()[line-1][nextColumn].getWall().isBreakable())){
			return true;
		}
		else if(position.x%1>1-hitboxHeightBottom && (board.getCases()[line-1][nextColumn].getWall()==null && board.getCases()[line-1][nextColumn].getWall().isBreakable())){
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
