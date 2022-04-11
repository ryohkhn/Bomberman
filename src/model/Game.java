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

    public abstract void gameLoop();

    public abstract boolean hasEnded();

	public abstract Board init();

}
