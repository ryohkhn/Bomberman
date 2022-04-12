package view;

import model.Board;
import model.Game;
import model.GamePVP;

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
    private Board board;
    private Game game;
    public static Thread gameThread;
    public static boolean isPaused=false;

    public Gui(){
        width = 600;
        height = 553;
        this.guiMenu=new GuiMenu(this);

        setSize(600,553);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);

        this.setLayout(new BorderLayout());        
        this.add(guiMenu,BorderLayout.CENTER);
        setVisible(true);
    }

    public void repaintGui(){
        guiBar.repaint();
        guiBoard.repaint();
    }

	public void startGame(){
		this.remove(guiMenu);
		switch(guiMenu.getGamemode()) {
		case 0:
			game = new GamePVP(guiMenu.getMap(),guiMenu.getNumberOfPlayers(),guiMenu.getNumberOfAI(),this);
			break;
		case 1:
			break;
		}

		board = game.init();
        this.guiBar=new GuiBar(board.getPlayerList());
        this.guiBoard=new GuiBoard(board);
        guiBar.setPreferredSize(new Dimension(this.getHeight()/15,this.getWidth()/15));        
        this.add(guiBar,BorderLayout.NORTH);
		this.add(guiBoard,BorderLayout.CENTER);
        requestFocusInWindow();

        gameThread=new Thread(new Runnable(){
            @Override
            public void run(){
                game.gameLoop();
            }
        });

        gameThread.start();
	}
	public static void main(String[] args) {
		Gui gui = new Gui();
	}

    @Override
    public void keyTyped(KeyEvent keyEvent){

    }

    @Override
    public void keyPressed(KeyEvent keyEvent){
        int k = keyEvent.getKeyCode();
        if(k==KeyEvent.VK_ESCAPE){
            if(Gui.isPaused){
                Gui.isPaused=false;
                Gui.gameThread.resume();
                System.out.println("resume");
            } else{
                Gui.isPaused=true;
                Gui.gameThread.stop();
                System.out.println("stop");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent){

    }
}
