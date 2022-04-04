package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Bot extends Player {

	private ArrayList<String> options;
	private final LinkedList<String> moves;
	private EnemyDistanceComp edc;
	private BonusDistanceComp bdc;
	private final Random random;
	Case [][] cases;
	private boolean init = false;
	private int thinkTime=10;
	private int nextInvoke=0;
	//minimal ai
	public Bot(int id, float x, float y, Board board) {
		super(id, x, y, board);
		super.ai = true;
		edc = new EnemyDistanceComp();
		bdc = new BonusDistanceComp();
		options = new ArrayList<>();
		moves = new LinkedList<>();
		options.add("UP");
		options.add("DOWN");
		options.add("LEFT");
		options.add("RIGHT");
		super.setBombCount(0);
		random = new Random();
		//generateNavMap();
	}

	@Override
	public void update(double deltaTime) {
		if (!init) {
			cases = getBoard().getCases();
			generateNavMap();
			init = true;
		}
		if (isAlive()) {
			nextInvoke = (++nextInvoke)%thinkTime; // moves are unique
		 
        	if(nextInvoke == 1) {
				updateNav();
				String move = moves.poll(); // moves are unique, find solutions for the AI to move in an other case while
											// moving between cases.
				if(move == null || !moveSafe(move) || enemyCheck()){ // reset if bombs or enemies ar nearby
					moves.clear();
					if(canPlaceBombAndEscape(moves)){ 
						//System.out.println(move);
						move = "BOMB";
					} else {
						move = "none";
						Collections.shuffle(options);
						for(int i = 0; i < 4; ++i){
							move = options.get(i);
							//System.out.println(move);
							if(moveSafe(move)){
								break;
							}
						}
					}
				}
			
				if(!move.equals("none")){
					if (move.equals("BOMB")) {
						setAction();
					} else {
						stop();
						chooseDirection(move);
					}
				}
			}
		}
		super.update(deltaTime);
	}

	private void stop() {
		setReleasedDown();
		setReleasedLeft();
		setReleasedRight();
		setReleasedUp();
		setReleasedAction();
	}

	private void chooseDirection(String move) {
		
		if (move.charAt(0) == 'U') { // first letter of move
			setPressUp();
		} else if(move.charAt(0) == 'D') {
			setPressDown();
		} else if(move.charAt(0) == 'L') {
			setPressLeft();
		} else if (move.charAt(0) == 'R') {
			setPressRight();
		}
	}

	private void generateNavMap() {
		int group = 0;
		int nextGroup = 0;
		boolean inc = true;
		for (int x = 0; x < cases.length; ++x) {
			for(int y = 0; y < cases[0].length; ++y){
				Case t = cases[x][y];
				if(t.getWall() != null){
					inc = true;
					continue;
				}
				if(inc){
					group = ++nextGroup;
					inc = false;
				}
				t.setNav_group(group);
				if(x > 0) {
					int leftGroup = cases[x-1][y].getNav_group();
					if(leftGroup == 0) continue;
					int min = Math.min(t.getNav_group(), leftGroup);
					int max = Math.max(t.getNav_group(), leftGroup);
					changeNavGroup(max, min);
					group = min;
				}
			}
			inc = true;
		}

	}

	private void changeNavGroup(int from, int to){
		for(int x = 0; x < cases.length; ++x){
			for(int y = 0; y < cases[0].length; ++y){
				Case t = cases[x][y];
				if(t.getNav_group() == from)
					t.setNav_group(to);
			}
		}
	}


	private void updateNav(){
		for(int x = 0; x < cases.length; ++x){
			for(int y = 0; y < cases[0].length; ++y){
				Case t = cases[x][y];
				if(!t.getNav_update()) continue;
				int u = 999;
				int d = 999;
				int l = 999;
				int r = 999;
				int g;
				if(y > 0 && (g = cases[x][y-1].getNav_group()) != 0) u = g;
				if(y < cases[0].length-1 && (g = cases[x][y+1].getNav_group()) != 0) d = g;
				if(x > 0 && (g = cases[x-1][y].getNav_group()) != 0) l = g;
				if(x < cases.length-1 && (g = cases[x+1][y].getNav_group()) != 0) r = g;
				
				int min = Math.min(Math.min(u, d), Math.min(l, r));
				
				if(u != 999) changeNavGroup(u, min);
				if(d != 999) changeNavGroup(d, min);
				if(l != 999) changeNavGroup(l, min);
				if(r != 999) changeNavGroup(r, min);
				
				t.setNav_group(min);
				t.setNav_update(false);
			}
		}
	}

	private boolean enemyCheck() {
		// determine wheter an enemy is in the range of the possible placements of your bomb
		if (getBombCount() >= getAmmo()) return false;
		ArrayList<IntPair> tiles = getCasesInBombRange((int)position.x, (int)position.y);
		for(IntPair t : tiles){
			for(Player e : getBoard().getPlayerList()){
				int p = e.getId();
				if(p != this.getId() && e.getPositionXasInt() == t.getX() && e.getPositionYasInt() == t.getY())
					return true;
			}
		}
		return false;
	}

	private boolean bonusCheck() {
		// determine wheter an enemy is in the range of the placements of your bonus
		for(int i = 0; i < cases.length; i++){
			for(int j = 0; j < cases[0].length; j++){
				if(cases[i][j].getWall() == null && cases[i][j].getBonus() != null)
					return true;
			}
		}
		return false;
	}


	private void getCasesInBombDir(int x, int y, int xdir, int ydir,ArrayList<IntPair> tiles){
		// détermine les cases où les bombes peuvent tuer
		for(int i = 1; i < getFirepower(); ++i){
			int xx = x + (i * xdir);
			int yy = y + (i * ydir);
			if(xx < 0 || xx >= cases.length || yy < 0 || yy >= cases[0].length) break;
			Case t = cases[xx][yy];
			if(t.getWall() == null || t.getWall().isBreakable()) {
				tiles.add(new IntPair(x, y));
			}
			else if(!t.getWall().isBreakable()) break;
		}
	}
	
	public ArrayList<IntPair> getCasesInBombRange(int x, int y){
		// détermine les cases où les bombes peuvent tuer
		ArrayList<IntPair> tiles = new ArrayList<>();
		tiles.add(new IntPair(x, y));
		getCasesInBombDir(x, y, 1, 0,tiles); // droite
		getCasesInBombDir(x, y, -1, 0,tiles); // gauche
		getCasesInBombDir(x, y, 0, 1,tiles); // bas
		getCasesInBombDir(x, y, 0, -1,tiles); // haut
		return tiles;
	}

	private ArrayList<IntPair> getAccessibleCases(Case from){
		// détermine les cases accessibles
		ArrayList<IntPair> tiles = new ArrayList<>();
		for(int x = 0; x < cases.length; ++x){
			for(int y = 0; y < cases[0].length; ++y){
				if(cases[x][y].getNav_group() == from.getNav_group())
					tiles.add(new IntPair(x, y));
			}
		}
		if(enemyCheck()){
			Collections.shuffle(tiles);
		} else if(bonusCheck()){
			Collections.sort(tiles, bdc); // to the closest bonus
		}else {
			Collections.sort(tiles, edc); // to the closest enemy
		}
		return tiles;
	}


	private IntPair xyFromMove(int ox, int oy, String move){
		int x = ox;
		int y = oy;
		if (move.charAt(0) == 'U') {
			--y;
		} else if(move.charAt(0) == 'D') {
			++y;
		} else if(move.charAt(0) == 'L') {
			--x;
		} else if (move.charAt(0) == 'R') {
			++x;
		}
		x = Math.max(0, Math.min(x, cases.length-1));
		y = Math.max(0, Math.min(y, cases[0].length-1));
		if(cases[x][y].getWall() == null)
			return new IntPair(x, y);
		else
			return new IntPair(ox, oy);
	}

	private void bfsCheck(BFSCase bt, Queue<BFSCase> tqueue, ArrayList<IntPair> tiles, boolean[][] visited, String dir){
		IntPair pos = xyFromMove(bt.c.getX(), bt.c.getY(), dir);
		if(!visited[pos.getX()][pos.getY()]){
			visited[pos.getX()][pos.getY()] = true;
			if(tiles.contains(pos)) tqueue.add(new BFSCase(pos, bt, dir));
		}
	}


	private class EnemyDistanceComp implements Comparator<IntPair> {
		Collection<Player> players;	
		EnemyDistanceComp(){
			players = getBoard().getPlayerList();
		}
		@Override
		public int compare(IntPair one, IntPair two){
			int dist1 = Integer.MAX_VALUE;
			int dist2 = Integer.MAX_VALUE;			
			for(Player p : players){
				if(!p.isAlive() || p.getId() == getId()) continue;
				dist1 = Math.min(Math.abs(one.getX() - p.getPositionXasInt()) + Math.abs(one.getY() - p.getPositionYasInt()), dist1);
				dist2 = Math.min(Math.abs(two.getY() - p.getPositionYasInt()) + Math.abs(two.getY() - p.getPositionYasInt()), dist2);
			}
			return dist1 - dist2;
		}
	}

	private class BonusDistanceComp implements Comparator<IntPair> {
		
		@Override
		public int compare(IntPair one, IntPair two){
			int dist1 = Integer.MAX_VALUE;
			int dist2 = Integer.MAX_VALUE;			
			for(int i = 0; i < cases.length; i++){
				for(int j = 0; j < cases[0].length; j++){
					if(cases[i][j].getWall() == null && cases[i][j].getBonus() != null) {
						dist1 = Math.min(Math.abs(one.getX() - getPositionXasInt()) + Math.abs(one.getY() - getPositionYasInt()), dist1);
						dist2 = Math.min(Math.abs(two.getY() - getPositionYasInt()) + Math.abs(two.getY() - getPositionYasInt()), dist2);
					}
				}
			}
			return dist1 - dist2;
		}
	}

	private boolean getRouteToCase(Case to, LinkedList<String> route){
		if(cases[(int)position.x][(int)position.y].getNav_group() != to.getNav_group()) return false;
		
		boolean [][] visited = new boolean[cases.length][cases[0].length];
		
		ArrayList<IntPair> tiles = getAccessibleCases(cases[getPositionXasInt()][getPositionYasInt()]);
		Queue<BFSCase> tqueue = new LinkedList<>();
		tqueue.add(new BFSCase(new IntPair(getPositionXasInt(),getPositionYasInt()), null, null));
		
		while(true){
			BFSCase bt = tqueue.peek();
			if(bt == null) return false;
			if(cases[bt.c.getX()][bt.c.getY()] == to) break;
			
			if(bt.c.getY() > 0) bfsCheck(bt, tqueue, tiles, visited, "UP");
			if(bt.c.getY() < cases[0].length-1) bfsCheck(bt, tqueue, tiles, visited, "DOWN");
			if(bt.c.getX() > 0) bfsCheck(bt, tqueue, tiles, visited, "LEFT");
			if(bt.c.getX() < cases.length-1) bfsCheck(bt, tqueue, tiles, visited, "RIGHT");

			tqueue.poll();
		}

		for(BFSCase t = tqueue.poll(); t != null; t = t.prev)
			if(t.dir != null) route.addFirst(t.dir);

		return true;
	}

	private boolean canPlaceBombAndEscape(LinkedList<String> moves){
		if(getBombCount() >= getAmmo() || !moveSafe("none")) {
			//System.out.println("oui");
			return false;
		}
		ArrayList<IntPair> bombtiles = getCasesInBombRange(getPositionXasInt(), getPositionYasInt());
		ArrayList<IntPair> movetiles = getAccessibleCases(cases[getPositionXasInt()][getPositionYasInt()]);
		boolean goodPlace = false;
		boolean safe = false;
		for(IntPair t : bombtiles){
			if(!goodPlace && cases[t.getX()][t.getY()].getWall() != null && cases[t.getX()][t.getY()].getWall().isBreakable())
				goodPlace = true;
		}
		// always place a bomb if an enemy is nearby, or on a random chance
		if(enemyCheck() || random.nextInt(5) == 0) goodPlace = true;
		if(!goodPlace) return false;
		for(IntPair t : movetiles){
			if(!safe && !bombtiles.contains(t) && getRouteToCase(cases[t.getX()][t.getY()], moves))
				safe = true;
		}
		System.out.println(safe);
		return safe;
	}

	private boolean moveSafe(String move){
		// check if the case is a safe place
		IntPair pos = xyFromMove(getPositionXasInt(), getPositionYasInt(), move);
		Case t = cases[pos.getX()][pos.getY()];
		return (t.getBomb() == null || !t.getBomb().getwillBeExploding());
	}

	private class BFSCase {
		IntPair c;
		BFSCase prev;
		String dir;
		BFSCase(IntPair t, BFSCase b, String s){
			c = t;
			prev = b;
			dir = s;
		}
	}


	private class IntPair {
		// cordonnées de cases as int,int
		private int l;
		private int r;
		public IntPair(int l, int r){
			this.l = l;
			this.r = r;
		}
		public int getX(){ return l; }
		public int getY(){ return r; }
	}
}
