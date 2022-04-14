package model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GamePVP extends Game{
    private ArrayList<Player> playerList;
    private PlayerInput key1,key2,key3,key4;
    private Player player1,player2,player3,player4;
    private Board board;
    private Gui gui;
    public static double timer;
    private ArrayList<Monster> monsterList;
    // should use game class as starter for choosing modes
    private String map;
    private double endTime = -1;

    public GamePVP(String map, int numberOfPlayers, int numberOfAI, Gui gui) {
		this.gui = gui;
		this.map = map;
        playerList = new ArrayList<Player>();
        monsterList = new ArrayList<Monster>();
    }

    public Board init() {
		try {
			board = new Board(this.map,playerList,monsterList); // fait
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        key1 = new PlayerInput(board.getPlayer(0));
    	gui.addKeyListener(key1);
        key2 = new PlayerInput(board.getPlayer(1));
    	gui.addKeyListener(key2);
        //key3 = new PlayerInput(board.getPlayer(2));
    	//gui.addKeyListener(key3);
        monsterList.add(new WalkingMonster(0, 0, board));
        monsterList.add(new FlyingMonster(0, 0, board));
        //monsterList.add(new MonstreDeux(0, 0, board));
        //key4 = new PlayerInput(board.getPlayer(3));
        //gui.addKeyListener(key4);
        this.addPlayers();
        return board;
    }

    public void addPlayers() {
        try {
            player1 = board.getPlayer(0);
            player1.setPlayer(0, 1.4F, 1.4F);
            player1.bindKeys(KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_Q, KeyEvent.VK_D, KeyEvent.VK_CONTROL);
            player2 = board.getPlayer(1);
            player2.setPlayer(1,1.4F,13.4F);
            player2.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_ALT_GRAPH);
            /*
            player3 = board.getPlayer(2);
            player3.setPlayer(2,11.4F, 1.4F);
            player3.bindKeys(KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD2);
            */
            //player4 = board.getPlayer(3);
            //player4.setPlayer(3,11.4F, 13.4F);
            //player4.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K,KeyEvent.VK_SPACE);
            board.getMonster(0).setMonster(11.4F, 13.4F);
            board.getMonster(1).setMonster(11.4F, 1.4F);
            // should be in gamemonsters
        } catch (Exception e) {
            e.printStackTrace();
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
        for(Player p : playerList){
            p.update(deltaTime);
        }
    }

    private void monsterUpdate(double deltaTime) {
        for(Monster m : monsterList){
            m.update(deltaTime);
        }
    }
    
    private int bombUpdate() {
    	int bombsExploded = 0;
        for(Player p : playerList){
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
        int alivePlayer = this.playerList.size();
        for(Player p : this.playerList){
            if(!p.isAlive()){
                alivePlayer -= 1;
            }
        }
        return alivePlayer <= 1;
    }

    void playSound(String soundFile, boolean loop) throws Exception {
	    File f = new File(soundFile);
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
	    Clip clip = AudioSystem.getClip();
	    clip.addLineListener(event -> {
	        if(LineEvent.Type.STOP.equals(event.getType())) {
	            clip.close();
	        }
	    });
	    clip.open(audioIn);
	    clip.start();
	    if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
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
        for(Player p : playerList){
        	for(Bomb b : p.getBombList()){
        		b.setStartTime(b.getStartTime() + resumeTime - pauseTime);
        	}
        }
        resumeTime = 0;
        pauseTime = 0;
	}
}
