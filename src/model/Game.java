package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public abstract class Game{
    public static double timer = 0;
    public final int FPS = 60;
    protected ArrayList<Player> players;
	protected ArrayList<Monster> monsters;
    protected File boardTemplate;
    protected Board board;
	protected Random random = new Random();
	protected boolean gameEnd = false;
    
    public abstract void gameLoop();

    public abstract boolean hasEnded();

	public abstract Board init();

	public abstract boolean getPaused();
	public abstract void pause();
	public abstract void resume();
	public abstract boolean isGamePvp();
	public abstract int getNbPlayers();
	public abstract int getNbAI();

	/**
	 * Plays audio file
	 * @param soundFile
	 * @param loop boolean to loop the audio or not
	 * @return clip object of audio
	 * @throws Exception
	 */
	public static Clip playSound(String soundFile, boolean loop) throws Exception {
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
	    return clip;
	}

	public Board getBoard(){
		return board;
	}

	public boolean getGameEnd(){
		return gameEnd;
	}
	
    public abstract void stopMusic();
}
