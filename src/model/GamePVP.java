package model;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

import controller.PlayerInput;
import view.Gui;

public class GamePVP extends Game{
    private ArrayList<Player> playerList;
    private PlayerInput key1,key2,key3,key4;
    private Player player1,player2,player3,player4;
    private BufferedImage image1,image2,image3,image4;
    private Loader loader;
    private Board board;
    private Gui gui;
    public static double timer;
    
    private String map;

    public GamePVP(String map, int numberOfPlayers, int numberOfAI, Gui gui) {
		this.gui = gui;
		this.map = map;
        playerList = new ArrayList<Player>();
    	loader = new Loader();
    }

    public Board init() {
		try {
			board = new Board(this.map,playerList); // fait
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        key1 = new PlayerInput(board.getPlayer(0));
    	gui.addKeyListener(key1);
        key2 = new PlayerInput(board.getPlayer(1));
    	gui.addKeyListener(key2);
        key3 = new PlayerInput(board.getPlayer(2));
    	gui.addKeyListener(key3);
        key4 = new PlayerInput(board.getPlayer(3));
        gui.addKeyListener(key4);
        this.addPlayers();
        return board;
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
            player2.bindKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_ALT_GRAPH);
            player3 = board.getPlayer(2);
            player3.setPlayer(image3,2,11.4F, 1.4F,32,48);
            player3.bindKeys(KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD2);
            player4 = board.getPlayer(3);
            player4.setPlayer(image4,3,11.4F, 13.4F,32,48);
            player4.bindKeys(KeyEvent.VK_U, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K,KeyEvent.VK_SPACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameLoop() {

        double loopTimeInterval = 1000 / FPS;
        double lastTime = System.currentTimeMillis();
        double currentTime;

        while(!this.hasEnded()){
            long startLoopTime = System.currentTimeMillis();

            //instructions timer
            timer += (startLoopTime - lastTime);
            lastTime = startLoopTime;
            // fin timer

            //début des instructions de jeu
            bombUpdate();
            playerUpdate(loopTimeInterval);
            gui.repaint();
            gui.revalidate();
            //fin des instructions de jeu

            long endLoopTime = System.currentTimeMillis();
            try{
                Thread.sleep((long)loopTimeInterval - (endLoopTime - startLoopTime));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void playerUpdate(double deltaTime) {
        for(Player p : playerList){
            p.update(deltaTime);
        }
    }


    public void bombUpdate() {
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
        Gui gui = new Gui();
    }
}
