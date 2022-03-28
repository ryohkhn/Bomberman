package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Bomb objects that are created by players.
 * firepower Strength of the bomb explosionContact
 * pierce Whether or not the explosions will pierce soft walls
 */
public class Bomb extends GameObject{

    // Original player that placed this bomb
    private final Player player;

    private double startTime;

    //Position of the bomb
    private final Board board;
    
    // Stats
    private final int firepower;
    private final boolean pierce;
    
    // Kicking bomb
    private boolean kicked;
    private KickDirection kickDirection;

    private int spriteIndex = -1;
    private boolean hasExploded = false;
    private int stopTop;
    private int stopDown;
    private int stopLeft;
    private int stopRight;


    /**
     * Constructs a bomb object with values passed in by a player object.

     * @param player Original player that placed this bomb
     */
    public Bomb(int x, int y, Player player, Board board) {
        super(x,y);

    	this.board = board;
    	
        // Stats
        this.firepower = player.getFirepower();
        this.pierce = player.getPierce();
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
        if(hasExploded) return; //Pour qu'il n'y ait qu'un seul appel d'explode par bombes.
        hasExploded = true;
    	Case [][] c = board.getCases();
        int lineLeft = Math.max(((int) position.y - firepower), 0);
        int lineRight = Math.min(((int) position.y + firepower), 14);
        int columnTop = Math.max(((int) position.x - firepower), 0);
        int columnDown = Math.min(((int) position.x + firepower), 12);
        boolean end = false;
        Case current = null;
        int i;
        for(i = (int)position.y + 1 ;i < lineRight && !end; i++ ){
            current = c[(int)position.x][i];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!pierce);
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
        assert current != null;
        stopRight = i;
        end = false;
        for(i = (int)position.y - 1 ;i > lineLeft && !end; i-- ){
            current = c[(int)position.x][i];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!pierce);
                }
                else {
                	end = true;
                }
            } else {
                //System.out.println("x=" + (int)position.x + " y=" + i + " for " + current);
                current.killMoveables(board);
            }
        }
        stopLeft = i;
        System.out.println("stop left = " + i + "    and lineLeft is = " + lineLeft);

        end = false;
        for(i = (int)position.x - 1 ; i > columnTop && !end; i-- ){
            current = c[i][(int)position.y];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!pierce);
                }
                else {
                	end = true;
                }
            } else {
                //System.out.println("x=" + i + " y=" + (int)position.y + " for " + current);
                current.killMoveables(board);
            }
        }
        stopTop = i;
        end = false;
        for(i = (int)position.x ; i < columnDown && !end; i++ ){ // commence a x et pas x + 1 pour tuer les joueurs sur l'emplacement de la bombe
            current = c[i][(int)position.y];
            if (current.getWall() != null) {

                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!pierce);
                }
                else {
                	end = true;
                }
            } else {
                //System.out.println("x=" + i + " y=" + (int)position.y + " for " + current);
                current.killMoveables(board);
            }
        }
        stopDown = i;
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
    
    public void setStartTime(double time) {
    	this.startTime = time;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getStopDown() {
        return stopDown;
    }

    public int getStopLeft() {
        return stopLeft;
    }

    public int getStopRight() {
        return stopRight;
    }

    public int getStopTop() {
        return stopTop;
    }

    @Override
    public String toString() {
        return "Bomb{" +
                "player=" + player +
                ", firepower=" + firepower +
                ", pierce=" + pierce +
                ", spriteIndex=" + spriteIndex +
                ", hasExploded=" + hasExploded +
                '}';
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