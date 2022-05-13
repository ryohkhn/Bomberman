package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import view.GuiBoard;

/**
 * (Unfinished)
 * Bot that is playing as a player controlled by a computer.
 * Move and take actions according to the issues faced in the environment
 */
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
	
	private int bombRow; 
	private int bombCol;
	
	private int[] proximity = new int[9];
	
	private int runFromBombTime;

	private boolean decidedToBreak = false;
	private boolean kill = false;
	private Player player;
	private Board board;
	private boolean bombAround;

	/**
	 * Constructs an AI object with values passed in by a player object 
	 * @param b Board of the game
	 * @param p Original player who is a bot
	 */
	public Bot(Board b,Player p) {
		player = p;
		board = b;
		edc = new EnemyDistanceComp();
		// create int of path
		for(int i=0; i<maxDepth-1; i++) {
			powerDepth*=10;
		}
	}

	/**
	 * Function that updates the actions of AI
	 */
	public void update() {
		if(iReached) { // case at the end of path reached
			moveToRow = player.getPositionXasInt();
			moveToCol = player.getPositionYasInt();
			traverseValue = 9999999;
			bestPath = 0;
			Player enemy = getAccessibleCases();
			findPath(enemy,0,0,0, player.getPositionXasInt(), player.getPositionYasInt());
			path = bestPath; // use bestPath directly if everything fine
     		iReached = false;
     		powerCurrent = powerDepth;
     		timeElapsed = 0;
		}else {
			if(!bombAround) // check if bombs are around
                traverse();
		}
	}
	/**
	 * Function that create an arraylist of cases where enemies are present
	 * ,sort the arraylist by checking the closest enemy
	 * @return an object player, the first one from the arraylist.
	 */
	private Player getAccessibleCases(){
		ArrayList<Player> tiles = new ArrayList<>();
		for(int x = 0; x < board.getCases().length; ++x){
			for(int y = 0; y < board.getCases()[0].length; ++y){
				if(board.getCases()[x][y].hasPlayers(player))
					tiles.add(board.getCases()[x][y].getPlayerOnCase(player));
			}
		}
		Collections.sort(tiles, edc); // to the closest enemy
		if(tiles.isEmpty()) return null;
		return tiles.get(0);
	}

	/**
	 * Function that releases all actions of the player
	 */
	public void stop() {
		player.setReleasedDown();
		player.setReleasedLeft();
		player.setReleasedRight();
		player.setReleasedUp();
		player.setReleasedAction();
	}

	/**
	 * Function that checks and compares the shortest path between an enemy and the current player 
	 */
	private class EnemyDistanceComp implements Comparator<Player> {
		@Override
		public int compare(Player one, Player two){			
			int dist1 = Math.abs(one.getPositionXasInt() - player.getPositionXasInt()) + Math.abs(one.getPositionYasInt() - player.getPositionYasInt());
			int dist2 = Math.abs(two.getPositionXasInt() - player.getPositionXasInt()) + Math.abs(two.getPositionYasInt() - player.getPositionYasInt());
			return dist1 - dist2;
		}
	}

	/**
	 * Determine actions of the player and make it effective if path exists.
	 */
	private void traverse() {
		if(path==0) { // no path then reastart
			timeElapsed = 0;
	        stop();
			powerCurrent = powerDepth;
			iReached = true;
			return;
		}else if(localReached){ // use path
			localReached = false;
			currDir = path/powerCurrent;
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
		}else if(!localReached ) {
			if( Math.abs(player.getPositionY() - moveToRow*GameObject.sizeY) <= player.getSpeed()  && Math.abs(player.getPositionX() - moveToCol*GameObject.sizeX) <= player.getSpeed()) { // path reached
				player.setPosition(moveToCol*GameObject.sizeX/board.getCases().length + (GameObject.sizeX - GuiBoard.objectSizex)/2, moveToRow*GameObject.sizeY/board.getCases()[0].length  + (GameObject.sizeY - Player.sizeY)/2);
				localReached = true;
				stop();
       		}else {
				timeElapsed++; //  check if the bot isn't in a trap
				if(timeElapsed > trapTimeLimit) {
                   timeElapsed = 0;
                   localReached = true;
                   stop();
                   path = 0;
                   iReached = true;
				}else {
					// priority of actions depending on the environment
					if(detectBomb(bombRow,bombCol)) { // check bombs
						//runThink = true;
					    bombAround = true;
					    path = 0;
					    iReached = true;
					    localReached = true;
					    stop();
					   
					    timeElapsed = 0;
					    currDir = 0;
					  //  return;
					}else if(kill) { // setbomb
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
					else if(decidedToBreak) { // decide to break wall
						stop();
						player.setAction();
						 
						decidedToBreak = false;
					    path = 0;
					    iReached = true;
					    localReached = true;
					    timeElapsed = 0;
					    currDir = 0;
					}
					else { // move
		               chooseDirection();
					}
				}
			}
		}
	}
	
	/**
	 * Function that sets movements of the current bot according to the variable currDIr
	 */
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

    /**
	 * Function which the path to take in order to escape the bomb
	 */
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

	/**
	 * Function which determines the path recursively.
	 * Cases are checked for each directions.
	 * If a path exists, return the path
	 * @param enemy the target as enemy
	 * @param depth the depth of the path which isn't deeper than 4
	 * @param direction the direction 
	 * @param path int where the path is stocked
	 * @param row the row of the current case from where paths are checked
	 * @param col the col of the current case from where paths are checked
	 */
	private void findPath(Player enemy, int depth, int direction, int path, int row, int col) {

		
		if(depth > maxDepth) {
			int distanceCol = enemy.getPositionXasInt() - col;
			int distanceRow = enemy.getPositionYasInt() - row;
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
	
	/**
	 * Function that checks bombs around the player
	 * @param row current position in row of player
	 * @param col current postiion in col of player
	 * @return boolean which checks it.
	 */
	private boolean detectBomb(int row, int col) {
		// proximity is a grill of 3x3 representing a grill where (row,col)
		// is the position of central case of the grill
		proximity[0] = getElementsInPlayerDir(row, col, -1, -1);
		proximity[1] = getElementsInPlayerDir(row, col, -1, 0);
		proximity[2] = getElementsInPlayerDir(row, col, -1, 1);
		proximity[3] = getElementsInPlayerDir(row, col, 0, -1);
		proximity[4] = getElementsInPlayerDir(row, col, 0, 0);
		proximity[5] = getElementsInPlayerDir(row, col, 0, 1);
		proximity[6] = getElementsInPlayerDir(row, col, 1, -1);
		proximity[7] = getElementsInPlayerDir(row, col, 1, 0);
		proximity[8] = getElementsInPlayerDir(row, col, 1, 1);
		for(int i=0; i<9; i++) {
			if(proximity[i] == 5) { // if 5 there is a bomb
				return true;
			}
		}
		return false;
	}
	/**
	 * Get the Elements present a new case
	 * @param x current case of x
	 * @param y current case of y
	 * @param xdir direction in row
	 * @param ydir direction in col
	 * @return the value according to the present element
	 */
	private int getElementsInPlayerDir(int x, int y, int xdir, int ydir){
		int xx = x + xdir;
		int yy = y + ydir;
		if(xx < 0 || xx >= board.getCases().length || yy < 0 || yy >= board.getCases()[0].length) { // invalid coordinates
			return 0;
		}
		Case t = board.getCases()[xx][yy];
		if (t.getBomb() != null) return 5; // bomb placed
		if (t.getWall() != null && t.getWall().isBreakable()) {
			return 3; // brekable wall
		} else if(t.getWall() != null){
			return 4; // solid wall
		}
		return 0;
	}
}
