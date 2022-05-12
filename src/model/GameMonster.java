package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.Clip;

import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GameMonster extends Game{
    private final Gui gui;
    private int nbPlayers;
    private int nbAI;
    private final String map;
    private int monsterMAX;
    private int numberOfMonstersTotal;
    private Clip gameMusic;
    private final Object pauseLock = new Object();
    private volatile boolean paused = false;
    private long pauseTime;
    private long resumeTime;
    private int minutesTimer=3;
    private int secondsTimer=0;

    public GameMonster(String map, int numberOfPlayers, int numberOfAI, Gui gui) {
		this.gui = gui;
		this.map = map;
        players = new ArrayList<>();
        monsters = new ArrayList<>();
        nbPlayers = numberOfPlayers;
        this.nbAI = numberOfAI;
        if (nbPlayers == 0) {
            nbPlayers = 1;
        }
        if (map.equals("maps/default.csv")) monsterMAX = nbPlayers * 2;
        else if (map.equals("maps/map2.csv")) monsterMAX = (nbPlayers + 1) * 2;
        else monsterMAX = (nbPlayers + 2) * 2;
    }

    public Board init() {
		try {
			board = new Board(this.map,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        this.addPlayers();
        this.addMonsters();
        board.setPlayerList(players);
        board.setMonsterList(monsters);
        for (Player play: players) {
            play.setPlayer(true);
        }
        return board;
    }

    public void addMonsters() {
        Monster monster = null;
        int i = monsters.size();
        if (i < monsterMAX) {
            //System.out.println("monstersgame" + monsters.size());
            //System.out.println("numofmonsterstot" + numberOfMonstersTotal);
            int r = random.nextInt(3);
            if (r == 2) monster = new WalkingMonster(0, 0, board);
            else monster = new FlyingMonster(0, 0, board);
            if (placeMonster(monster)) {
                monsters.add(monster);
                numberOfMonstersTotal += 1;
            }
        }
    }

    public boolean placeMonster(Monster monster) {
        int x;
        int y;
        int timing = 0;
        do {
            x = random.nextInt(11) + 1;
            y = random.nextInt(13) + 1;
            timing++; // avoid loopholes
        }while (!checkplace(x,y) && timing <= 25);
        if (timing > 25) return false;
        monster.setMonster(x+0.4F, y+0.4F);
        board.getCases()[x][y].addMovableOnCase(monster);
        return true;
    }

    private boolean checkplace(int x,int y) {
        boolean free = true;
        if (x == 0 || y == 0 || x == 12 || y == 14) {
            return false;
        }
        Case current = board.getCases()[x][y];
        if (current.getWall() != null || !current.getMovablesOnCase().isEmpty()) {
            return false;
        }
        if (x-1 > 0) {
            free = board.getCases()[x-1][y].getMovablesOnCase().isEmpty();
            if (y-1 > 0) {
                free = free && !board.getCases()[x-1][y-1].hasPlayers();
            }
            if (y+1 < 14) {
                free = free && !board.getCases()[x-1][y+1].hasPlayers();
            }
        }
        if (y-1 > 0) {
            free = free && board.getCases()[x][y-1].getMovablesOnCase().isEmpty();
        }
        if (y+1 < 14) {
            free = free && board.getCases()[x][y+1].getMovablesOnCase().isEmpty();
        }
        if (x+1 < 12) {
            free = free && board.getCases()[x+1][y].getMovablesOnCase().isEmpty();
            if (y-1 > 0) {
                free = free && !board.getCases()[x+1][y-1].hasPlayers();
            }
            if (y+1 < 14) {
                free = free && !board.getCases()[x+1][y+1].hasPlayers();
            }
        }
        return free;
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
                player.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K,KeyEvent.VK_SPACE);
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
     * main loop of the game.
     */
    @Override
    public void gameLoop() {
        double loopTimeInterval = 1000 / FPS;
        double lastTime = System.currentTimeMillis();

        try {
            gameMusic = playSound("resources/SFX/BackgroundMusic.wav", true);
        } catch (Exception ignored) {}

        while(true){
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
                    } catch (Exception ignored) {
                    }
                }
                playerUpdate(loopTimeInterval);
                monsterUpdate(loopTimeInterval);
                gui.repaint();
            }
            gui.revalidate();
            //fin des instructions de jeu

            long endLoopTime = System.currentTimeMillis();
            try{
                if((long)loopTimeInterval - (endLoopTime - startLoopTime)>0)
                    Thread.sleep((long)loopTimeInterval - (endLoopTime - startLoopTime));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void playerUpdate(double deltaTime) {
        for(Player p : players){
            p.update(deltaTime);
        }
    }
    private void monsterUpdate(double deltaTime) {
        addMonsters();
        updateSpeed();
        Iterator<Monster> iter = monsters.iterator();
		while(iter.hasNext()) {
			Monster m = iter.next();
            if (m.getDead()) iter.remove();
			else m.update(deltaTime);
		}
    }

    public void updateSpeed() {
        if (numberOfMonstersTotal == 2 * monsterMAX) {
            FlyingMonster.setSpeed(FlyingMonster.getSpeed() + 0.1F);
            WalkingMonster.setSpeed(WalkingMonster.getSpeed() + 0.1F);
        }
    }
    
    private int bombUpdate() {
    	int bombsExploded = 0;
        for(Player p : players){
        	bombsExploded += p.bombUpdate();
        }
        return bombsExploded;
    }

    @Override
    public boolean hasEnded() { // verification de la victoire
        int alivePlayer = this.players.size();
        int aliveMonsters = this.monsters.size();
        for(Player p : this.players){
            if(!p.isAlive()){
                alivePlayer -= 1;
            }
        }
        return alivePlayer == 0 || aliveMonsters == 0 || (((int)(Game.timer/1000)%3600)/60 == minutesTimer && ((int)(Game.timer/1000)%60) == secondsTimer);
        // check monsters
    }


	public void pause() {
        pauseTime = System.currentTimeMillis();
        this.paused = true;
    }

    public void resume() {
        resumeTime = System.currentTimeMillis();
        bombPauseUpdate();
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); 
        }
    }

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

    @Override
    public boolean isGamePvp(){
        return false;
    }

    @Override
    public Board getBoard(){
        return board;
    }

    @Override
    public boolean getPaused() {
        return this.paused;
    }

	@Override
	public void stopMusic() {
		this.gameMusic.stop();
	}
}
