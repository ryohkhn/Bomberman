package model;

import java.awt.geom.Point2D;
/**
 * Bomb objects that are created by players.
 */
public class Bomb extends GameObject{

    // Original player that placed this bomb
    private Player player;
    
    private double startTime;
    
    //Position of the bomb
    private int x;
    private int y;
    private Board board;
    
    // Stats
    private int firepower;
    private boolean pierce;
    
    // Kicking bomb
    private boolean kicked;
    private KickDirection kickDirection;

    /**
     * Constructs a bomb object with values passed in by a player object.
     * @param position Coordinates of this object in the game world
     * @param firepower Strength of the bomb explosionContact
     * @param pierce Whether or not the explosions will pierce soft walls
     * @param timer How long before the bomb detonates
     * @param player Original player that placed this bomb
     */
    public Bomb(int x, int y, int firepower, boolean pierce, Player player, Board board) {
    	
    	//Position
    	this.x = x;
    	this.y = y;
    	this.board = board;
    	
        // Stats
        this.firepower = firepower;
        this.pierce = pierce;
        this.player = player;
        // Kicking bomb
        this.kicked = false;
        this.kickDirection = KickDirection.Nothing;
    }

    public void placeBomb() {
	    //Set bomb in case
		board.getCases()[x][y].setBomb(this);
    }
    
    public void explode() {
    	Case [][] c = board.getCases();
		for( int i = 0; i < 2 + this.firepower; i++ ){         
            Case current = c[x+i][y+i];
            
            if(current!=null && current.getWall() != null){ // Destroy wall near the bomb
                if(current.getWall().isBreakable()){
                	current.setWall(null);
                }
            }
		}
	}

    public void killMovables() {
    	Case [][] c = board.getCases();
    	for( int i = 0; i < 2 + this.firepower; i++ ){ 
			Case current = c[x+i][y+i];
	    	if(current!=null && current.getMovablesOnCase().size() > 0) {
	        	current.killMoveables();
	        }
		}
    }
    
    public void setKicked(boolean kicked, KickDirection kickDirection) {
        this.kicked = kicked;
        this.kickDirection = kickDirection;
    }

    public boolean isKicked() {
        return this.kicked;
    }

    public void stopKick() {
        this.kicked = false;
        this.kickDirection = KickDirection.Nothing;
    }

   /*
    * Deletes the bomb after explosion
	*/
	public void deleteBomb(){
		player.setBombCount(player.getBombCount()-1);
		//startTime = -1;
		board.getCases()[x][y].setBomb(null);
	}

}

/**
 * Provides the speed for bomb moving from kick. Speed should be 6 to ensure the kicking logic is as smooth
 * as possible. Changing the value is dangerous and can introduce bugs to the kicking logic.
 */
enum KickDirection {

    FromTop(new Point2D.Float(0, 6)),
    FromBottom(new Point2D.Float(0, -6)),
    FromLeft(new Point2D.Float(6, 0)),
    FromRight(new Point2D.Float(-6, 0)),
    Nothing(new Point2D.Float(0, 0));

    private Point2D.Float velocity;

    KickDirection(Point2D.Float velocity) {
        this.velocity = velocity;
    }

    public Point2D.Float getVelocity() {
        return this.velocity;
    }

}