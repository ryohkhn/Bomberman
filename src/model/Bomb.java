package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Bomb objects that are created by players.
 * firepower Strength of the bomb explosionContact
 * pierce Whether or not the explosions will pierce soft walls
 */
public class Bomb extends GameObject{

    private final Player player;
    private double startTime;
    private final Board board;
    // Stats
    private final int firepower;
    private final boolean pierce;
    private int spriteIndex = -1;
    // Kick
    private boolean kicked;
    private KickDirection kickDirection;

    // location where the explosion will stop.
    private int stopTop;
    private int stopDown;
    private int stopLeft;
    private int stopRight;

    private boolean fuse = false;
    private final ArrayList<Case> explodingCase;
    private boolean hasExploded = false;



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
        // Kick
        this.kicked = false;
        this.kickDirection = KickDirection.Nothing;
        this.startTime = System.currentTimeMillis();
        explodingCase = new ArrayList<>();

        //Set bomb in case
        board.getCases()[x][y].setBomb(this);


    }


    /**
     * Function that destroy walls in a cross-shaped area
     * the explosion has a range corresponding to the distance between the bomb's location and the first block found.
     * If no block is found, the explosion has a range = bomb.firepower.
     */
    public void explode() {
        if(hasExploded) return; // So that there is only one explosion
        hasExploded = true;
        
    	Case [][] c = board.getCases();
        int lineLeft = Math.max(((int) position.y - firepower), 0);
        int lineRight = Math.min(((int) position.y + firepower), 14);
        int columnTop = Math.max(((int) position.x - firepower), 0);
        int columnDown = Math.min(((int) position.x + firepower), 12);
        Case current = c[(int)position.x][(int)position.y];
        int i;
        boolean end = false;
        explodingCase.add(current);

        // right extension
        for(i = (int)position.y + 1 ;i <= lineRight && !end; i++ ){
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
                explodingCase.add(current);
            }
		}
        stopRight = i;
        end = false;

        // left extension
        for(i = (int)position.y - 1 ;i >= lineLeft && !end; i-- ){
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
                explodingCase.add(current);

            }
        }
        stopLeft = i;
        end = false;

        // top extension
        for(i = (int)position.x - 1 ; i >= columnTop && !end; i-- ){
            current = c[i][(int)position.y];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!pierce);
                    explodingCase.add(current);
                }
                else {
                	end = true;
                }
            } else {
                explodingCase.add(current);

            }
        }
        stopTop = i;
        end = false;

        // down extension
        for(i = (int)position.x + 1 ; i <= columnDown && !end; i++ ){
            current = c[i][(int)position.y];
            if (current.getWall() != null) {
                if(current.getWall().isBreakable()) {
                    current.setWall(null);
                    end = (!pierce);
                    explodingCase.add(current);
                }
                else {
                	end = true;
                }
            } else {
                explodingCase.add(current);
            }
        }
        stopDown = i;
    }
    
    /**
     * kill movable on exploded cases.
     * @return points won from kill(s)
     */
    public int kill() {
        int pointsCount = 0;
        for (Case c: explodingCase) {
            pointsCount += c.killMoveables(player);
        }
        return pointsCount;
    }


    // setter et getter :

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

    public boolean getFuse() {
        return fuse;
    }

    public void setFuse(boolean b) {
        this.fuse = b;
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

    private final Point2D.Float velocity;

    KickDirection(Point2D.Float velocity) {
        this.velocity = velocity;
    }

	public Point2D.Float getVelocity() {
        return this.velocity;
    }

}