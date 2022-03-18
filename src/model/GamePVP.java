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
    private double timer;

    public GamePVP() {
        playerList = new ArrayList<Player>();
    	loader = new Loader();
    }

    public void init() {
		try {
			board = new Board("maps/default.csv",playerList); // fait
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.gui=new Gui(board);
        key1 = new PlayerInput(board.getPlayer(0));
    	gui.addKeyListener(key1);
        key2 = new PlayerInput(board.getPlayer(1));
    	gui.addKeyListener(key2);
        key3 = new PlayerInput(board.getPlayer(2));
    	gui.addKeyListener(key3);
        key4 = new PlayerInput(board.getPlayer(3));
        gui.addKeyListener(key4);
        this.addPlayers();
    }

    public void addPlayers() {
        try {
            image1 = loader.loadImage("resources/playersheet_0.png");
            image2 = loader.loadImage("resources/playersheet_1.png");
            image3 = loader.loadImage("resources/playersheet_2.png");
            image4 = loader.loadImage("resources/playersheet_3.png");
            player1 = board.getPlayer(0);
            player1.setPlayer(image1, 0, 1.4F, 1.4F,32,48);
            player1.bindKeys(KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_Q, KeyEvent.VK_D, KeyEvent.VK_CONTROL);
            player2 = board.getPlayer(1);
            player2.setPlayer(image2,1,1.4F,13.4F,32,48);
            //player2.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_PAUSE);
            player3 = board.getPlayer(2);
            player3.setPlayer(image3,2,11.4F, 13.4F,32,48);
            player3.bindKeys(KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD2);
            player4 = board.getPlayer(3);
            player4.setPlayer(image4,3,11.4F, 1.4F,32,48);
            player4.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K,KeyEvent.VK_SPACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameLoop() {
        double loopTimeInterval = 1000 / FPS;
        double delta = 0;
        double lastTime = System.currentTimeMillis();
        double currentTime;
        double timerComparatif=0;

        while(!this.hasEnded()){
            currentTime = System.currentTimeMillis();
            delta += (currentTime - lastTime) / loopTimeInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            timerComparatif = printTime(timerComparatif);

            if (delta >= 1) {
                delta--;

                bombExplode();
                bombUpdate();
                playerUpdate(loopTimeInterval);
                gui.repaint();

            }
        }
    }

    private void playerUpdate(double deltaTime) {
        for(Player p : playerList){
            p.update(deltaTime);
        }
    }

    private void bombExplode() {
        for(Player p : playerList){
        }
    }

    private void bombUpdate() {
        for(Player p : playerList){
            p.bombUpdate();
        }
    }

    private double printTime(double timer2) {
        if(timer >= timer2 + 100){
            //System.out.println("---------------------------------- Timer : " + (int)timer/1000 + " s " + (int)timer%1000/100 + " ms " + " ------------------------------------");
            return timer;
        }
        return timer2;
    }

    @Override
    public boolean hasEnded() { // verification de la victoire
        return false;
    }



    public static void main(String[] args){
        GamePVP game=new GamePVP();
        game.run();
    }

    @Override
    public void run() {
        init();
        gameLoop();
    }
}
