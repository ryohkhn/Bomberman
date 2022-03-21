package model;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;

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


    private int spriteIndex = -1;


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
    


    /**
     * Function that kills players in a cross-shaped area (with each extension of length firepower)
     * and destroy wall (if allowed)
     */
    public void explode() {
    	Case [][] c = board.getCases();
        int lineLeft = ((int)position.y - this.player.getFirepower())>=0?(int)position.y - this.player.getFirepower():0;
        int lineRight = ((int)position.y + this.player.getFirepower())>14?14:(int)position.y + this.player.getFirepower();
        int columnDown = ((int)position.x + this.player.getFirepower())>12?12:(int)position.x + this.player.getFirepower();
        int columnTop = ((int)position.x - this.player.getFirepower())>=0?(int)position.x - this.player.getFirepower():0;
        boolean end = false;
        for(int i = (int)position.y + 1 ;i <= lineRight && !end; i++ ){
			Case current = c[(int)position.x][i];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!this.player.getPierce());
                }
                else {
                	end = true;
                }
                //System.out.println("right stop at " + (int)position.x + "/" + i + " where there are " + current);
                //System.out.println("start at y=" + (int)position.y + " stop at y=" + lineRight + "\n" );

            } else {
                //System.out.println("x=" + (int)position.x + " y=" + i + " for " + current);
                current.killMoveables(board);
            }
		}
        end = false;
        for(int i = (int)position.y - 1 ;i >= lineLeft && !end; i-- ){
            Case current = c[(int)position.x][i];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!this.player.getPierce());
                }
                else {
                	end = true;
                }
            } else {
                //System.out.println("x=" + (int)position.x + " y=" + i + " for " + current);
                current.killMoveables(board);
            }
        }
        end = false;
        for(int i = (int)position.x - 1 ; i >= columnTop && !end; i-- ){
            Case current = c[i][(int)position.y];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!this.player.getPierce());
                }
                else {
                	end = true;
                }
            } else {
                //System.out.println("x=" + i + " y=" + (int)position.y + " for " + current);
                current.killMoveables(board);
            }
        }
        end = false;
        for(int i = (int)position.x ; i <= columnDown && !end; i++ ){
        	System.out.println(i);
            Case current = c[i][(int)position.y];
            System.out.println(current.getWall() != null);
            if (current.getWall() != null) {

                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!this.player.getPierce());
                }
                else {
                	end = true;
                }
            } else {
                //System.out.println("x=" + i + " y=" + (int)position.y + " for " + current);
                current.killMoveables(board);
            }
            System.out.println(i + " " + columnDown);
        }
        System.out.println("bomb killed movables and destroyed wall");
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

    public KickDirection getKick() {
    	return this.kickDirection;
    }
    
   /*
    * Deletes the bomb after explosion
	*/


    // setter et getter :

    public double getStartTime() {
        return startTime;
    }


	public Player getPlayer() {
		return player;
	}

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }
}

/**
 * Provides the speed for bomb moving from kick. Speed should be 6 to ensure the kicking logic is as smooth
 * as possible. Changing the value is dangerous and can introduce bugs to the kicking logic.
 */
enum KickDirection {

    FromTop(new Point2D.Float(1, 0)),
    FromBottom(new Point2D.Float(-1, 0)),
    FromLeft(new Point2D.Float(0, 1)),
    FromRight(new Point2D.Float(0, -1)),
    Nothing(new Point2D.Float(0, 0));

    private Point2D.Float velocity;

    KickDirection(Point2D.Float velocity) {
        this.velocity = velocity;
    }



	public Point2D.Float getVelocity() {
        return this.velocity;
    }

}