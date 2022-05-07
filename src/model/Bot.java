package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import view.GuiBoard;

public class Bot implements AI {

	private EnemyDistanceComp edc;
	private int maxDepth = 4;
	private int traverseValue = 999999;
	private int bestPath = 0;

	private int moveToRow = 0;
	private int moveToCol = 0;
	private boolean iReached = true;
	
	private int powerDepth=1;
	private int powerCurrent = 1;
	private int currDir = 0;
	private int path = 0;
	
	private boolean localReached = true;
	private int trapTimeLimit = 200;
	private int timeElapsed = 0;
	
	private int runFromBombTimeLimit = 25*2;
	
	private int bombRow, bombCol;
	
	private int[] proximity = new int[9];
	
	private int runFromBombTime;

	private boolean decidedToBreak = false;
	private boolean kill = false;
	private Player player;
	private Board board;
	private boolean bombAround;
	private int runDir;

	public Bot(Board b,Player p) {
		player = p;
		board = b;
		edc = new EnemyDistanceComp();
		for(int i=0; i<maxDepth-1; i++) {
			powerDepth*=10;
		}
	}

	


	public void update() {
		if(iReached) {
			moveToRow = player.getPositionXasInt();
			moveToCol = player.getPositionYasInt();
			//System.out.println("current position : ( "+moveToRow+" , "+moveToCol+" ) ");
			traverseValue = 9999999;
			bestPath = 0;
			Player enemy = getAccessibleCases();
			//System.out.println("nearest enemy : ( "+enemy.getId()+" ) ");
			findPath(enemy,0,0,0, player.getPositionXasInt(), player.getPositionYasInt());
			path = bestPath; // use bestPath directly if everything fine
			//System.out.println("path to traverse ::  "+path);
     		iReached = false;
     		powerCurrent = powerDepth;
     		timeElapsed = 0;
		}else {
			if(!bombAround)
                traverse();
			else
				runAwayFromBomb();
		}
	}
	private Player getAccessibleCases(){
		// détermine les cases accessibles
		ArrayList<Player> tiles = new ArrayList<>();
		for(int x = 0; x < board.getCases().length; ++x){
			for(int y = 0; y < board.getCases()[0].length; ++y){
				if(board.getCases()[x][y].hasPlayers(player))
					tiles.add(board.getCases()[x][y].getPlayerOnCase(player));
			}
		}
		Collections.sort(tiles, edc); // to the closest enemy
		return tiles.get(0);
	}


	public void stop() {
		player.setReleasedDown();
		player.setReleasedLeft();
		player.setReleasedRight();
		player.setReleasedUp();
		player.setReleasedAction();
	}

	private class EnemyDistanceComp implements Comparator<Player> {
		@Override
		public int compare(Player one, Player two){			
			int dist1 = Math.abs(one.getPositionXasInt() - player.getPositionXasInt()) + Math.abs(one.getPositionYasInt() - player.getPositionYasInt());
			int dist2 = Math.abs(two.getPositionXasInt() - player.getPositionXasInt()) + Math.abs(two.getPositionYasInt() - player.getPositionYasInt());
			return dist1 - dist2;
		}
	}


	private void traverse() {
		if(path==0) {
			timeElapsed = 0;
	        stop();
			powerCurrent = powerDepth;
			iReached = true;
			//System.out.println(" path " + bestPath + " is fully traversed ");
			return;
		}else if(localReached){ // not reached final but reached next local
			localReached = false;
			currDir = path/powerCurrent;
			System.out.println(":::: Direction chosen ::::: "+currDir);
			path = path%powerCurrent;
			powerCurrent = powerCurrent/10;
			if(currDir == 1) {// up
				moveToRow -= 1 ;
			}else if(currDir== 2) {// right
				moveToCol += 1;	
			}else if(currDir== 3) {// down
				moveToRow += 1;
			}else if(currDir == 4) {// left
				moveToCol -= 1;	
			}
			//System.out.println("I have to reach : ( "+moveToRow+" , "+moveToCol+" ) ");
		}else if(!localReached ) {
			if( Math.abs(player.getPositionY() - moveToRow*GameObject.sizeY) <= player.getSpeed()  && Math.abs(player.getPositionX() - moveToCol*GameObject.sizeX) <= player.getSpeed()) {
			//	System.out.println("I reached at  : ( "+moveToRow+" , "+moveToCol+" ) ");
				player.setPosition(moveToCol*GameObject.sizeX/board.getCases().length + (GameObject.sizeX - GuiBoard.objectSizex)/2, moveToRow*GameObject.sizeY/board.getCases()[0].length  + (GameObject.sizeY - Player.sizeY)/2);
				//Engine.updateStage = true;
				localReached = true;
				stop();
       		}else {
				timeElapsed++;
				if(timeElapsed > trapTimeLimit) {
                   timeElapsed = 0;
                   localReached = true;
                   stop();
                  // me.setPosition(me.getCenterCol()*Window.width/StageMatrix.size, me.getCenterRow()*Window.height/StageMatrix.size);
                   path = 0;
                   iReached = true;
				}else {
				//	System.out.println("Going towards " + currDir);
					if(detectBomb(bombRow,bombCol)) {
						//runThink = true;
					    bombAround = true;
					    path = 0;
					    iReached = true;
					    localReached = true;
					    stop();
					   
					    timeElapsed = 0;
					    currDir = 0;
					  //  return;
					}else if(kill) {
						if(runFromBombTime <= 0) {
							stop();
							player.setAction();
						}
						runFromBombTime++;
						moveAway();
						if(runFromBombTime >= runFromBombTimeLimit) {
							kill = false;
							path = 0;
							iReached = true;
							localReached = true;
							timeElapsed = 0;
							currDir = 0;
							runFromBombTime = 0;
							player.setPosition((player.getPositionY()*GameObject.sizeX/board.getCases().length + (GameObject.sizeX - GuiBoard.objectSizex)/2), player.getPositionX()*GameObject.sizeY/board.getCases()[0].length  + (GameObject.sizeY - Player.sizeY)/2);
							stop();
						}
					}
					else if(decidedToBreak) {
						stop();
						player.setAction();
						 
						decidedToBreak = false;
					    path = 0;
					    iReached = true;
					    localReached = true;
					    timeElapsed = 0;
					    currDir = 0;
					}
					else {
		               chooseDirection();
					}
				}
			}
		}
	}
	
	public void chooseDirection() {
		if(currDir == 0) { // do nothing
			player.setReleasedDown();
			player.setReleasedLeft();
			player.setReleasedRight();
			player.setReleasedUp();		
		}
		if(currDir == 1) {// go up
			player.setPressUp();
		}
		if(currDir == 2) { // go right
			player.setPressRight();
		}
		if(currDir == 3) {// go down
			player.setPressDown();
		}
		if(currDir == 4) { // go left
			player.setPressLeft();
		}
		
	}
    
	private void moveAway() {
		int row = player.getPositionXasInt();
		int col = player.getPositionYasInt();
		
		
		if(row-1 > 0 && col-1 > 0 && board.getCases()[row-1][col-1].getWall() == null && board.getCases()[row-1][col-1].getBomb() == null ) {
			player.setPressUp();
			player.setPressLeft();
		}
		else if(row-1 > 0 && col+1 < board.getCases()[0].length && board.getCases()[row-1][col+1].getWall() == null && board.getCases()[row-1][col+1].getBomb() == null ) {
			player.setPressUp();
			player.setPressRight();
		}
		else if(row+1 < board.getCases().length  && col-1 > 0 && board.getCases()[row+1][col-1].getWall() == null && board.getCases()[row+1][col-1].getBomb() == null ) {
			player.setPressDown();
			player.setPressLeft();
		}
		else if(row+1 < board.getCases().length && col+1 < board.getCases()[0].length && board.getCases()[row+1][col+1].getWall() == null && board.getCases()[row+1][col+1].getBomb() == null ) {
			player.setPressDown();
			player.setPressRight();
		}
		
	}

	
	
	private void findPath(Player enemy, int depth, int direction, int path, int row, int col) {

		
		if(depth > maxDepth) {
			int distanceCol = enemy.getPositionXasInt() - col;
			int distanceRow = enemy.getPositionYasInt() - row;
			System.out.println(distanceCol);
			System.out.println(distanceRow);
			int distance = distanceCol*distanceCol + distanceRow*distanceRow;
			
			if(distance < traverseValue) {
				traverseValue = distance;
				bestPath = path;
			}
			
			return;
		}
		
		if(direction == 0 || depth == 0) {     									// initial four iterations
			findPath(enemy,depth+1, 1, path, row, col);
			findPath(enemy,depth+1, 2, path, row, col);
			findPath(enemy,depth+1, 3, path, row, col);
			findPath(enemy,depth+1, 4, path, row, col);
			return;
		}
		
	
		if(direction == 1) {// up
			row -= 1 ;
		}else if(direction== 2) {// right
			col += 1;	
		}else if(direction== 3) {// down
			row += 1;
		}else if(direction== 4) {// left
			col -= 1;	
		}

		if(board.getCases()[row][col].hasPlayers(player)) {		// Player
			if(depth <= 1) {
				kill = true;
			}
	
		}
		else if(board.getCases()[row][col].getWall() != null && !board.getCases()[row][col].getWall().isBreakable()) {    // Solid Bricks
			return;
		}
		else if(board.getCases()[row][col].getWall() != null) {    // Breakable Bricks
			decidedToBreak = true;
			return;
		}
		else if(board.getCases()[row][col].getBomb() != null) {
			bombRow = row; bombCol = col;
			System.out.println("Bomb!!");
			return;
		}
		
		path = path*10 + direction;
		
		if(direction!=3)
			findPath(enemy,depth+1, 1, path, row, col);
		if(direction!=4)
			findPath(enemy,depth+1, 2, path, row, col);
		if(direction!=1)
			findPath(enemy,depth+1, 3, path, row, col);
		if(direction!=2)
			findPath(enemy,depth+1, 4, path, row, col);
		
	}
	
	
	private boolean detectBomb(int row, int col) {
		// détermine les cases où les bombes peuvent tuer
		proximity[0] = getBombsInPlayerDir(row, col, -1, -1);
		proximity[1] = getBombsInPlayerDir(row, col, -1, 0);
		proximity[2] = getBombsInPlayerDir(row, col, -1, 1);
		proximity[3] = getBombsInPlayerDir(row, col, 0, -1);
		proximity[4] = getBombsInPlayerDir(row, col, 0, 0);
		proximity[5] = getBombsInPlayerDir(row, col, 0, 1);
		proximity[6] = getBombsInPlayerDir(row, col, 1, -1);
		proximity[7] = getBombsInPlayerDir(row, col, 1, 0);
		proximity[8] = getBombsInPlayerDir(row, col, 1, 1);
		for(int i=0; i<9; i++) {
			if(proximity[i] == 5) {
				return true;
			}
		}
		return false;
	}

	private int getBombsInPlayerDir(int x, int y, int xdir, int ydir){
		// détermine les cases où les bombes peuvent tuer
		int xx = x + xdir;
		int yy = y + ydir;
		if(xx < 0 || xx >= board.getCases().length || yy < 0 || yy >= board.getCases()[0].length) {
			return 0;
		}
		Case t = board.getCases()[xx][yy];
		if (t.getBomb() != null) return 5;
		if (t.getWall() != null && t.getWall().isBreakable()) {
			return 3;
		} else if(t.getWall() != null){
			return 4;
		}
		return 0;
		
	}
	
	private void runAwayFromBomb() {
		 // start thinking
	//		System.out.println("Deciding where to run ......");
		if(proximity[0] == 5 || proximity[2] == 5 ||proximity[6] == 5 || proximity[8] == 5 ) {
			// stop
			runDir = 0;
		}
		else if(proximity[3] == 5 || proximity[5] == 5) {
			if(proximity[1] == 4 || proximity[1] == 3) {
				//move down
				runDir = 3;
			}
			else {
				//move up
				runDir = 1;
			}				
		}
		else if(proximity[1] == 5 || proximity[7] == 5) {
			if(proximity[3] == 4 || proximity[3] == 3) {
				//move right
				runDir = 2;
			}
			else {
				//move left
				runDir = 4;
			}				
		}
		else if(proximity[4] == 5) {
			if(proximity[0] == 4 || proximity[0] == 3) {
				// move diagonal right up ie go to vicinity[2]
			    runDir = 6;
			}
			if(proximity[2] == 4 || proximity[2] == 3) {
				// move diagonal right down ie go to vicinity[8]
			    runDir = 7;
			}
			if(proximity[8] == 4 || proximity[8] == 3) {
				// move diagonal left down ie go to vicinity[6]
			   runDir = 8;
			}
			if(proximity[6] == 4 || proximity[6] == 3) {
				// move diagonal left up ie go to vicinity[0]
			   runDir = 5;
			}
		}
		runTo(runDir);
	}
	
	
	public void runTo(int i) {
		if (i == 0) {
			stop();
		} else if (i == 1) {
			player.setPressUp();
		} else if (i == 2) {
			player.setPressRight();
		} else if (i == 3) {
			player.setPressDown();
		} else if (i == 4) {
			player.setPressLeft();
		} else if (i == 5) {
			player.setPressUp();
			player.setPressLeft();
		} else if (i == 6) {
			player.setPressUp();
			player.setPressRight();
		} else if (i == 7) {
			player.setPressDown();
			player.setPressRight();
		} else {
			player.setPressDown();
			player.setPressLeft();
		}
	}
	
}
