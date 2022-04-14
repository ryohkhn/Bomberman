package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

public abstract class Game{
    public double timer = 0;
    public int FPS = 60;
    private ArrayList<Player> players;
    private File boardTemplate;
    private Board board;
    
    private final Object pauseLock = new Object();
    private volatile boolean paused;
    
    public abstract void gameLoop();

    public abstract boolean hasEnded();

	public abstract Board init();

	public abstract boolean getPaused();
	public abstract void pause();
	public abstract void resume();

}
