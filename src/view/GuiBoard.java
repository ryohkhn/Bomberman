package view;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class GuiBoard extends JPanel{
    private Board board;
    private ArrayList<Player> players;
    private BufferedImage block;
    private BufferedImage breakableBlock;
    private BufferedImage unbreakableBlock;
    private HashMap<Bonus.Type, BufferedImage> bonusMap=new LinkedHashMap<>();
    private LinkedList<BufferedImage> explosionMidList=new LinkedList<>();
    private LinkedList<BufferedImage> playerImagesList=new LinkedList<>();

    public GuiBoard(Board board){
        this.board=board;
        this.players=board.getPlayerList();
        GameObject.setSizeY(this.getHeight());
        GameObject.setSizeX(this.getWidth());
        setBackground(Color.WHITE);
        loadImages();
    }

    private void loadImages(){
        try{
            loadExplosionImages();
            loadBonusImages();
            loadPlayerImages();
            block=ImageIO.read(new File("resources/block.png"));
            unbreakableBlock=ImageIO.read(new File("resources/block_unbreakable.png"));
            breakableBlock=ImageIO.read(new File("resources/block_breakable.png"));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void loadExplosionImages() throws IOException{
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_0.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_1.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_2.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_3.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_4.png")));
        explosionMidList.add(ImageIO.read(new File("resources/bomb.png")));
    }
    private void loadBonusImages() throws IOException{
        bonusMap.put(Bonus.Type.Bomb,ImageIO.read(new File("resources/bonus_bomb.png")));
        bonusMap.put(Bonus.Type.Firemax,ImageIO.read(new File("resources/bonus_firemax.png")));
        bonusMap.put(Bonus.Type.Fireup,ImageIO.read(new File("resources/bonus_fireup.png")));
        bonusMap.put(Bonus.Type.Kick,ImageIO.read(new File("resources/bonus_kick.png")));
        bonusMap.put(Bonus.Type.Pierce,ImageIO.read(new File("resources/bonus_pierce.png")));
        bonusMap.put(Bonus.Type.Speed,ImageIO.read(new File("resources/bonus_speed.png")));
        //bonusMap.put(Bonus.Type.Timer,ImageIO.read(new File("resources/bonus_timer.png")));
    }

    private void loadPlayerImages() throws IOException{
        playerImagesList.add(ImageIO.read(new File("resources/player_0.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_1.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_2.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_3.png")));
    }

    private void resetPlayerImages() throws IOException{
        playerImagesList.clear();
        loadPlayerImages();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D) g;
        try{
            paintBoard(g2);
            paintPlayers(g2);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void paintBoard(Graphics2D g2) throws IOException{
        Case[][] cases=board.getCases();
        int x_pos=0;
        int y_pos=0;
        int x_height=this.getHeight()/cases.length;
        int y_width=this.getWidth()/cases[0].length;
        for(Case[] line : cases) {
            for(Case c : line) {
                if(c.getWall() == null) {
                    g2.drawImage(block, x_pos, y_pos, y_width, x_height, null); // on affiche l'herbe
                    if(c.getBonus() != null) {
                        g2.drawImage(bonusMap.get(c.getBonus().getType()), x_pos, y_pos, y_width, x_height, null); // on affiche le bonus
                	}
                	if(c.getBomb()!=null) {
                        g2.drawImage(getBombImageState(cases,c), x_pos, y_pos, y_width, x_height, null); // on affiche l'image de l'état de la bombe
                    }
                }
                else if(c.getWall().isBreakable()){
                    g2.drawImage(breakableBlock,x_pos,y_pos,y_width,x_height,null);
                }
                else{
                    g2.drawImage(unbreakableBlock,x_pos,y_pos,y_width,x_height,null);
                }
                x_pos+=y_width;
            }
            x_pos=0;
            y_pos+=x_height;
        }
    }

    private BufferedImage getBombImageState(Case[][] cases, Case c) {
        switch (c.getBomb().getSpriteIndex()){
            case 0: return explosionMidList.get(0);
            case 1: return explosionMidList.get(1);
            case 2: return explosionMidList.get(2);
            case 3: return explosionMidList.get(3);
            case 4: return explosionMidList.get(4);
            default: return explosionMidList.get(5);
        }
    }

    private void paintPlayers(Graphics2D g2) throws IOException {
        for (Player player : players) {
            float x = player.getPositionX() - 0.4F;
            float y = player.getPositionY() - 0.4F;
            int x_height = this.getHeight() / board.getCases().length;
            int y_width = this.getWidth() / board.getCases()[0].length;
            playerImagesList.set(player.getId(), player.getImage());
            switch (player.getId()) {
                case 0:
                    g2.drawImage(playerImagesList.get(0), (int) (y * y_width), (int) (x * x_height), x_height,y_width, null);
                    break;
                case 1:
                    g2.drawImage(playerImagesList.get(1), (int) (y * y_width), (int) (x * x_height), x_height,y_width, null);
                    break;
                case 2:
                    g2.drawImage(playerImagesList.get(2), (int) (y * y_width), (int) (x * x_height), x_height,y_width, null);
                    break;
                case 3:
                    g2.drawImage(playerImagesList.get(3), (int) (y * y_width), (int) (x * x_height), x_height,y_width, null);
                    break;
                default:
                    break;
            }
        }
    }
}
