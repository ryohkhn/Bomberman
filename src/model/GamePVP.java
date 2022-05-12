package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GamePVP extends Game{
    private Gui gui;
    private int nbPlayers;
    private int nbAI;
    private String map;

    private final Object pauseLock = new Object();
    private volatile boolean paused = false;
    private long pauseTime;
    private long resumeTime;
    private Clip gameMusic;

    /**
     * Constructor for GamePVP
     * @param map
     * @param numberOfPlayers
     * @param numberOfAI
     * @param gui
     */
    public GamePVP(String map, int numberOfPlayers, int numberOfAI, Gui gui) {
		this.gui = gui;
		this.map = map;
        players = new ArrayList<>();
        nbPlayers = numberOfPlayers;
        nbAI = numberOfAI;
        System.out.println(nbAI);
        if (nbAI == 0 && nbPlayers < 2) {
            nbPlayers = 1;
            nbAI = 1;
        }
        if (nbAI != 0 && nbPlayers == 0) {
            nbPlayers = 1;
        }
    }

    /**
     * Create a new board.
     * @return new board
     */
    public Board init() {
		try {
			board = new Board(this.map,false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        this.addPlayers();
        board.setPlayerList(players);
        for (Player play: players) {
            play.setPlayer(false);
        }
        return board;
    }

    /**
     * Adds players to the model and assigns keys to them.
     * Adds AI.
     */
    public void addPlayers() {
        float x = 0;
        float y = 0;
        int i = 0;
        Player player = null;
        // add numberof players playing and their positions of board
        while (i < nbPlayers) {
            if (i == 0) {
                x = 1.4F;
                y = 1.4F;
                player = new Player(i, x, y, board,false);
                player.bindKeys(KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_Q, KeyEvent.VK_D, KeyEvent.VK_CONTROL);
            } else if (i == 1) {
                x = 11.4F;
                y = 13.4F;
                player = new Player(i, x, y, board,false);
                player.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_ALT_GRAPH);
            } else if (i == 2) {
                x = 1.4F;
                y = 13.4F;
                player = new Player(i, x, y, board,false);
                player.bindKeys(KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD2);
            } else if (i==3) {
                x = 11.4F;
                y = 1.4F;
                player = new Player(i, x, y, board,false);
                player.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K,KeyEvent.VK_N);
            }
            board.getCases()[(int)x][(int)y].addMovableOnCase(player);
            PlayerInput key = new PlayerInput(player);
            gui.addKeyListener(key);
            players.add(player);
            i++;
         }
        while (i < nbPlayers + nbAI) {
            if (i == 1) {
                x = 11.4F;
                y = 13.4F;
            } else if (i == 2) {
                x = 1.4F;
                y = 13.4F;
            } else if (i==3) {
                x = 11.4F;
                y = 1.4F;
            }
            player = new Player(i, x, y, board,true);
            board.getCases()[(int)x][(int)y].addMovableOnCase(player);
            players.add(player);
            i++;
        }
    }

    /**
     * The main loop of the game.
     */
    @Override
    public void gameLoop() {

        double loopTimeInterval = 1000 / FPS;
        double lastTime = System.currentTimeMillis();

        try {
			gameMusic = playSound("resources/SFX/BackgroundMusic.wav", true);
		} catch (Exception ignored) {}

        while(true) {
            long startLoopTime = System.currentTimeMillis();
            if (!gameRestart) {
                gameEndScreen = hasEnded();
                if (!this.gameEndScreen) {
                    synchronized (pauseLock) {
                        if (paused) {
                            try {
                                gui.repaint();
                                synchronized (pauseLock) {
                                    pauseLock.wait();
                                }
                            } catch (InterruptedException ex) {
                                break;
                            }
                        }
                    }
                }
                //instructions timer
                timer += (startLoopTime - lastTime);
                lastTime = startLoopTime;
                // fin timer

                //début des instructions de jeu
                if (bombUpdate() != 0) {
                    try {
                        playSound("resources/SFX/BombeExplode.wav", false);
                    } catch (Exception ignored) {}
                }
                playerUpdate(loopTimeInterval);
                gui.repaint();
            }
            gui.revalidate();
            //fin des instructions de jeu
            long endLoopTime = System.currentTimeMillis();
            try {
                if ((long) loopTimeInterval - (endLoopTime - startLoopTime) > 0)
                    Thread.sleep((long) loopTimeInterval - (endLoopTime - startLoopTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updating player(s) position.
     * @param deltaTime loop time interval.
     */
    public void playerUpdate(double deltaTime) {
        for(Player p : players){
            p.update(deltaTime);
        }
    }

    /**
     * Updating bomb(s).
     * @return the number of exploded bombs.
     */
    private int bombUpdate() {
    	int bombsExploded = 0;
        for(Player p : players){
        	bombsExploded += p.bombUpdate();
        }
        return bombsExploded;
    }

    /**
     * Verify if victory state has been reached.
     * (if there is only one player alive).
     */
    @Override
    public boolean hasEnded() { // verification de la victoire
        int alivePlayer = this.players.size();
        for(Player p : this.players){
            if(!p.isAlive()){
                alivePlayer -= 1;
            }
        }
        return alivePlayer <= 1;
    }

    /**
     * Set pause.
     */
	public void pause() {
        pauseTime = System.currentTimeMillis();
        this.paused = true;
    }

    /**
     * Resume game.
     */
    public void resume() {
        resumeTime = System.currentTimeMillis();
        bombPauseUpdate();
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); 
        }
    }

    /**
     * Update bomb(s) timer with the pause duration.
     */
	private void bombPauseUpdate() {
		timer -= (resumeTime - pauseTime);
        for(Player p : players){
        	for(Bomb b : p.getBombList()){
        		b.setStartTime(b.getStartTime() + resumeTime - pauseTime);
        	}
        }
        resumeTime = 0;
        pauseTime = 0;
	}

	/*
	 * Getters and setters
	 */
	
    @Override
    public int getNbPlayers(){
        return nbPlayers;
    }

    @Override
    public int getNbAI() {
        return nbAI;
    }

    public boolean getPaused(){
        return this.paused;
    }
    
    public void stopMusic() {
    	this.gameMusic.stop();
    }
    
    @Override
    public boolean isGamePvp(){
        return true;
    }
}
