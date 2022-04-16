package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GameMonster extends Game{
    private Gui gui;
    private int nbPlayers;
    private int nbAI;
    private String map;
    private double endTime = -1;
    private final int MONSTERMAX;
    private int numberOfMonstersTotal;
    // jeu en coop
    // TODO ajouter un timer à décompter pour la fin de jeu
    public GameMonster(String map, int numberOfPlayers, Gui gui) {
		this.gui = gui;
		this.map = map;
        players = new ArrayList<>();
        monsters = new ArrayList<>();
        nbPlayers = numberOfPlayers;
        if (nbAI == 0 && nbPlayers == 0) {
            nbPlayers = 1; // à mettre dans game monstrer pour le choix de base
        }
        MONSTERMAX = nbPlayers * 2;
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
        if (i < MONSTERMAX) {
            int r = random.nextInt(3);
            if (r != 2) monster = new WalkingMonster(0, 0, board);
            else monster = new FlyingMonster(0, 0, board);
            monsters.add(monster);
            placeMonster(monster);
            numberOfMonstersTotal += 1;
        }
    }

    public void placeMonster(Monster monster) {
        int x;
        int y;
        do {
            x = random.nextInt(11) + 1;
            y = random.nextInt(13) + 1;
        }while (!checkplace(x,y));
        monster.setMonster(x+0.4F, y+0.4F);
        board.getCases()[x][y].addMovableOnCase(monster);
    }

    private boolean checkplace(int x,int y) {
        boolean check = false;
        if (x == 0 || y == 0 || x == 12 || y == 14) {
            return false;
        }
        Case current = board.getCases()[x][y];
        if (current.getWall() != null || !current.getMovablesOnCase().isEmpty()) {
            return false;
        }
        if (x-1 > 0) {
            check = check || board.getCases()[x-1][y].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
            if (y-1 > 0) {
                check = check || board.getCases()[x-1][y-1].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
            }
            if (y+1 > 0) {
                check = check || board.getCases()[x-1][y+1].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
            }
        }
        if (y-1 > 0) {
            check = check || board.getCases()[x][y-1].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
        }
        if (y+1 > 0) {
            check = check || board.getCases()[x][y+1].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
        }
        if (x+1 < 12) {
            check = check || board.getCases()[x+1][y].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
            if (y-1 > 0) {
                check = check || board.getCases()[x+1][y-1].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
            }
            if (y+1 < 14) {
                check = check || board.getCases()[x+1][y+1].getMovablesOnCase().stream().filter(play -> play instanceof Movable).findFirst().isPresent();
            }
        }
        return !check;
    }


    public void addPlayers() {
        float x = 0;
        float y = 0;
        int i = 0;
        Player player = null;
        while (i < nbPlayers) {
            if (i == 0) {
                x = 1.4F;
                y = 1.4F;
                player = new Player(i, x, y, board);
                player.bindKeys(KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_Q, KeyEvent.VK_D, KeyEvent.VK_CONTROL);
            } else if (i == 1) {
                x = 11.4F;
                y = 13.4F;
                player = new Player(i, x, y, board);
                player.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_ALT_GRAPH);
            } else if (i == 2) {
                x = 1.4F;
                y = 13.4F;
                player = new Player(i, x, y, board);
                player.bindKeys(KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD2);
            } else if (i==3) {
                x = 11.4F;
                y = 1.4F;
                player = new Player(i, x, y, board);
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
            player = new Bot(i, x, y, board);
            board.getCases()[(int)x][(int)y].addMovableOnCase(player);
            players.add(player);
            i++;
        }
    }

    private final Object pauseLock = new Object();
    private volatile boolean paused = false;
    private long pauseTime;
    private long resumeTime;
    
    @Override
    public void gameLoop() {

        double loopTimeInterval = 1000 / FPS;
        double lastTime = System.currentTimeMillis();
        double currentTime;

        try {
			playSound("resources/SFX/BackgroundMusic.wav", true);
		} catch (Exception e1) {}
        boolean endLoop = false;
        while(!endLoop){
        	synchronized (pauseLock) {
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        
            long startLoopTime = System.currentTimeMillis();

            //instructions timer
            timer += (startLoopTime - lastTime);
            lastTime = startLoopTime;
            // fin timer

            //début des instructions de jeu
            
            bombUpdate();
            if(this.hasEnded()){
                if(endTime == -1) endTime = System.currentTimeMillis();
                else if(endTime + 1000 < System.currentTimeMillis()) endLoop = true;
            }
            if(bombUpdate() != 0) {
				try {
					playSound("resources/SFX/BombeExplode.wav", false);
				} catch (Exception e) {}
            }
            playerUpdate(loopTimeInterval);
            monsterUpdate(loopTimeInterval);
            gui.repaint();
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
    
        //gui.endScreen();
    }

    public void playerUpdate(double deltaTime) {
        for(Player p : players){
            p.update(deltaTime);
        }
    }
    private void monsterUpdate(double deltaTime) {
        addMonsters();
        updateSpeed();
        monsters.removeIf(m -> !m.isAlive());
        for(Monster m : monsters){
            m.update(deltaTime);
        }
    }

    public void updateSpeed() {
        if (numberOfMonstersTotal == 2 * MONSTERMAX) {
            FlyingMonster.setSpeed(FlyingMonster.getSpeed() + 0.1F);
            WalkingMonster.setSpeed(WalkingMonster.getSpeed() + 0.1F);
        }
        if (numberOfMonstersTotal == 3 * MONSTERMAX) {
            FlyingMonster.setSpeed(FlyingMonster.getSpeed() + 0.1F);
            WalkingMonster.setSpeed(WalkingMonster.getSpeed() + 0.1F);
        }
        if (numberOfMonstersTotal == 4 * MONSTERMAX) {
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

    private double printTime(double timer2) {
        if(timer >= timer2 + 100){
            return timer;
        }
        return timer2;
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
        return alivePlayer == 0 || aliveMonsters == 0 || (((int)(Game.timer/1000)%3600)/60 == 10 && ((int)(Game.timer/1000)%60) == 0);
        // check monsters
    }

    

    public static void main(String[] args){
        Gui gui = new Gui();
    }

	@Override
	public boolean getPaused() {
		return this.paused;
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
}
