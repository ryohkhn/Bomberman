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
    private Board board;
    
    // Stats
    private int firepower;
    private boolean pierce;
    
    // Kicking bomb
    private boolean kicked;
    private KickDirection kickDirection;


    private int spriteTimer;
    private int spriteIndex;


    /**
     * Constructs a bomb object with values passed in by a player object.
     * @param firepower Strength of the bomb explosionContact
     * @param pierce Whether or not the explosions will pierce soft walls
     * @param player Original player that placed this bomb
     */
    public Bomb(int x, int y, int firepower, boolean pierce, Player player, Board board) {
        super(null,x,y);

    	this.board = board;
    	
        // Stats
        this.firepower = firepower;
        this.pierce = pierce;
        this.player = player;
        // Kicking bomb
        this.kicked = false;
        this.kickDirection = KickDirection.Nothing;

        this.startTime = System.currentTimeMillis();

        //Set bomb in case
        board.getCases()[x][y].setBomb(this);


    }
    
    public void explode() {
    	Case [][] c = board.getCases();
        int destroyColumnStart = Math.max((int)position.y - firepower, 1);
        int destroyColumnEnd = Math.min((int) position.y + firepower, c[(int) position.x].length - 2);
        boolean end= false;
        for(int i = (int)position.y; i >= destroyColumnStart && !end; i-- ){
            Case current = c[(int)position.x][(int)position.y+i];
            if(current!=null && current.getWall() != null){ // Destroy wall near the bomb
                end = true;
                if(current.getWall().isBreakable()){
                    current.setWall(null);
                }
            }
        }
        end = false;
        for(int i = (int)position.y; i <= destroyColumnEnd && !end; i++ ){
            Case current = c[(int)position.x][(int)position.y+i];
            if(current!=null && current.getWall() != null){ // Destroy wall near the bomb
                if(current.getWall().isBreakable()){
                    current.setWall(null);
                    end = true;
                }
            }
        }

        int destroyLineStart = Math.max((int)position.x - firepower, 1);
        int destroyLineEnd = Math.min((int) position.x + firepower, c[(int) position.y].length - 2);
        end= false;
        for(int i = (int)position.y; i >= destroyLineStart && !end; i-- ){
            Case current = c[(int)position.x+i][(int)position.y];
            if(current!=null && current.getWall() != null){ // Destroy wall near the bomb
                if(current.getWall().isBreakable()){
                    current.setWall(null);
                    end = true;
                }
            }
        }
        end = false;
        for(int i = (int)position.x; i <= destroyLineEnd && !end; i++ ){
            Case current = c[(int)position.x + i][(int)position.y];
            if(current!=null && current.getWall() != null){ // Destroy wall near the bomb
                if(current.getWall().isBreakable()){
                    current.setWall(null);
                    end = true;
                }
            }
        }
        System.out.println("bombe explode (destroy wall)");
	}

    /**
     * Function that kills players in a cross-shaped area (with each extension of length firepower)
     */
    public void killMovables() {
    	Case [][] c = board.getCases();
        int killColumnStart = Math.max((int)position.y - firepower, 1);
    	int killColumnEnd = Math.min((int) position.y + firepower, c[(int) position.x].length - 2);
        for(int i= killColumnStart ;i <= killColumnEnd; i++ ){
			Case current = c[(int)position.x][(int)position.y+i];
	    	if(current!=null && current.getMovablesOnCase().size() > 0) {
	        	current.killMoveables(board);
	        }
		}
        int killLineStart = Math.max((int)position.x - firepower, 1);
        int killLineEnd = Math.min((int) position.x + firepower, c[(int) position.y].length - 2);
        for(int j=killLineStart; j <= killLineEnd; j++ ){
            Case current = c[(int)position.x+j][(int)position.y];
            if(current!=null && current.getMovablesOnCase().size() > 0) {
                current.killMoveables(board);
            }
        }
        System.out.println("bomb kill movables");
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


    // setter et getter :

    public double getStartTime() {
        return startTime;
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