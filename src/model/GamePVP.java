package model;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GamePVP extends Game{
    ArrayList<Player> playerList;
    PlayerInput key1,key2,key3,key4;
    Player player1,player2,player3,player4;
    BufferedImage image1,image2,image3,image4;
    Loader loader;

    public GamePVP() {
        playerList = new ArrayList<Player>();
    	loader = new Loader();
        this.addPlayers();
    }

    public void init() {
        Board board = null;
		try {
			board = new Board("maps/default.csv",playerList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Gui gui=new Gui(board);
        key1 = new PlayerInput(board.getPlayer1());
    	gui.addKeyListener(key1);
        key2 = new PlayerInput(board.getPlayer2());
    	gui.addKeyListener(key2);
        key3 = new PlayerInput(board.getPlayer3());
    	gui.addKeyListener(key3);
        key4 = new PlayerInput(board.getPlayer4());
    	gui.addKeyListener(key4);
    }

    public void addPlayers() {
        try {
            image1 = loader.loadImage("resources/player_0.png");
            image2 = loader.loadImage("resources/player_1.png");
            image3 = loader.loadImage("resources/player_2.png");
            image4 = loader.loadImage("resources/player_3.png");
            player1 = new Player(image1, 0, 0, 0);
            player1.bindKeys(KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_Q, KeyEvent.VK_D);
            player2 = new Player(image2, 1, 5, 5);
            player2.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
            player3 = new Player(image3, 2, 10, 10);
            player3.bindKeys(KeyEvent.VK_8, KeyEvent.VK_5, KeyEvent.VK_4, KeyEvent.VK_6);
            player4 = new Player(image4, 3, 15, 15);
            player4.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K);
            playerList.add(player1);
            playerList.add(player2);
            playerList.add(player3);
            playerList.add(player4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GamePVP a = new GamePVP();
        a.init();
    }
}
