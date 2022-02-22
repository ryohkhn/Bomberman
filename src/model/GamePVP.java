package model;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GamePVP extends Game implements Runnable{
    private ArrayList<Player> playerList;
    private PlayerInput key1,key2,key3,key4;
    private Player player1,player2,player3,player4;
    private BufferedImage image1,image2,image3,image4;
    private Loader loader;
    private Board board;
    private Gui gui;

    public GamePVP() {
        playerList = new ArrayList<Player>();
    	loader = new Loader();
    }

    public void init() {
		try {
			board = new Board("maps/default.csv",playerList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.gui=new Gui(board);
        key1 = new PlayerInput(board.getPlayer1());
    	gui.addKeyListener(key1);
        key2 = new PlayerInput(board.getPlayer2());
    	gui.addKeyListener(key2);
        key3 = new PlayerInput(board.getPlayer3());
    	gui.addKeyListener(key3);
        key4 = new PlayerInput(board.getPlayer4());
    	gui.addKeyListener(key4);
        this.addPlayers();
    }

    public void addPlayers() {
        try {
            image1 = loader.loadImage("resources/player_0.png");
            image2 = loader.loadImage("resources/player_1.png");
            image3 = loader.loadImage("resources/player_2.png");
            image4 = loader.loadImage("resources/player_3.png");
            player1 = board.getPlayer1();
            player1.setPlayer(image1,0,1.4F, 1.4F);
            player1.bindKeys(KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_Q, KeyEvent.VK_D,KeyEvent.VK_CONTROL);
            player2 = board.getPlayer2();
            player2.setPlayer(image2,1,13.4F,1.4F);
            player2.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_PAUSE);
            player3 = board.getPlayer3();
            player3.setPlayer(image3,2,13.4F, 11.4F);
            player3.bindKeys(KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD2);
            player4 = board.getPlayer4();
            player4.setPlayer(image4,3,1.4F, 11.4F);
            player4.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K,KeyEvent.VK_SPACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        GamePVP a = new GamePVP();
        a.init();
    }
}
