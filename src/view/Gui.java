package view;

import model.*;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gui extends JFrame implements KeyListener{
    private GuiMenu guiMenu;
    private GuiBar guiBar;
    private GuiBoard guiBoard;
    public static int width;
    public static int height; 
    private Board board; // TODO: 05/05/2022 suppr ?
    private Game game;
    public static Thread gameThread;
    public static boolean isPaused=false; // TODO: 05/05/2022 suppr ?
    private Clip menuMusic;

    public Gui(){
        width = this.getWidth();
        height = this.getHeight();
        this.guiMenu=new GuiMenu(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);

        getContentPane().setPreferredSize(new Dimension(675,608));
        pack();

        this.setLayout(new BorderLayout());
        this.add(guiMenu,BorderLayout.CENTER);
        try {
			menuMusic = Game.playSound("resources/SFX/MenuMusic.wav", true);
		} catch (Exception e){
            e.printStackTrace();
        }
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void repaintGui(){
        guiBar.repaint();
        guiBoard.repaint();
    }

    public void restartGame(){
        this.remove(guiBoard);
        this.remove(guiBar);
        this.remove(guiMenu);
        this.game.stopMusic();
        gameThread = null;
        this.guiMenu = null;
        this.guiBar = null;
        this.guiBoard = null;
        this.game.setGameRestart(true);
        this.game = null;
        this.board = null;
        for(KeyListener key : getKeyListeners()){
            removeKeyListener(key);
        }

        this.guiMenu=new GuiMenu(this);
        addKeyListener(this);
        this.setLayout(new BorderLayout());
        this.add(guiMenu,BorderLayout.CENTER);
        try {
            menuMusic = Game.playSound("resources/SFX/MenuMusic.wav", true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Create Model objects and Gui objects to start the game after menu configuration is done
     */
	public void startGame(){
		this.remove(guiMenu);
		if (menuMusic != null) menuMusic.stop();

		switch(guiMenu.getGamemode()) {
		case 0:
            game = new GameMonster(guiMenu.getMap(),guiMenu.getNumberOfPlayers(),this);
			break;
		case 1:
            game = new GamePVP(guiMenu.getMap(),guiMenu.getNumberOfPlayers(),guiMenu.getNumberOfAI(),this);
			break;
		}

		Game.timer = 0;
		board = game.init();
        this.guiBar=new GuiBar(game);
        this.guiBoard=new GuiBoard(game, this);

        guiBar.setPreferredSize(new Dimension(this.getWidth()/15,this.getHeight()/13));
        this.add(guiBar,BorderLayout.NORTH);
		this.add(guiBoard,BorderLayout.CENTER);
        requestFocusInWindow();

        gameThread=new Thread(() -> game.gameLoop());
        System.out.println("start gameThread\n");

        gameThread.start();
	}


    /**
     * Key event to pause the game and resume
     * @param keyEvent key pressed
     */
    @Override
    public void keyPressed(KeyEvent keyEvent){
        int k = keyEvent.getKeyCode();
        if(!game.hasEnded() && k==KeyEvent.VK_ESCAPE){
            if(!game.getPaused()){
                guiBoard.showPauseButtons();
                game.pause();
            } else{
                guiBoard.removePauseButtons();
                game.resume();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent){}

    @Override
    public void keyTyped(KeyEvent keyEvent){}


    public static void main(String[] args){
        Gui gui = new Gui();
    }
}
