package view;

import model.Board;
import model.Case;
import model.GameObject;
import model.Loader;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GuiBoard extends JPanel{
    private Board board;
    private ArrayList<Player> players;
    Loader loader;

    public GuiBoard(Board board){
        this.board=board;
        this.players=board.getPlayerList();
        GameObject.setSizeY(this.getHeight());
        GameObject.setSizeX(this.getWidth());
        setBackground(Color.WHITE);
        loader = new Loader();
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
                    File image=new File("resources/block.png");
                    g2.drawImage(ImageIO.read(image), x_pos, y_pos, y_width, x_height, null); // on affiche l'herbe
                    if(c.getBonus() != null) {
                        image=new File(c.getBonus().getSprite());
                	}
                	if(c.getBomb()!=null) {
                        image = getBombImageState(cases,c);
                    }
                    g2.drawImage(ImageIO.read(image), x_pos, y_pos, y_width, x_height, null); // on affiche l'élément par dessus
                }
                else if(c.getWall().isBreakable()){
                    File image=new File("resources/block_breakable.png");
                    g2.drawImage(ImageIO.read(image),x_pos,y_pos,y_width,x_height,null);
                }
                else{
                    File image=new File("resources/block_unbreakable.png");
                    g2.drawImage(ImageIO.read(image),x_pos,y_pos,y_width,x_height,null);
                }
                x_pos+=y_width;
            }
            x_pos=0;
            y_pos+=x_height;
        }
    }

    private File getBombImageState(Case[][] cases, Case c) {
        switch (c.getBomb().getSpriteIndex()){
            case 0: return new File("resources/explosion/explosion_mid_0.png");
            case 1: return new File("resources/explosion/explosion_mid_1.png");
            case 2: return new File("resources/explosion/explosion_mid_2.png");
            case 3: return new File("resources/explosion/explosion_mid_3.png");
            case 4: return new File("resources/explosion/explosion_mid_4.png");
            default: return new File("resources/bonus_pierce.png");
        }
    }

    private void paintPlayers(Graphics2D g2) throws IOException {
        for (Player player : players) {
            BufferedImage image = player.getImage();
            float x = player.getPositionX() - 0.4F;
            float y = player.getPositionY() - 0.4F;
            int x_height = this.getHeight() / board.getCases().length;
            int y_width = this.getWidth() / board.getCases()[0].length;
            if (image == null && player.isAlive()) {
                switch (player.getId()) {
                    case 0:
                        image = loader.loadImage("resources/player_0.png");
                        break;
                    case 1:
                        image = loader.loadImage("resources/player_1.png");
                        break;
                    case 2:
                        image = loader.loadImage("resources/player_2.png");
                        break;
                    case 3:
                        image = loader.loadImage("resources/player_3.png");
                        break;
                    default:
                        break;
                }
            }
            if (image != null){
                g2.drawImage(image, (int) (y * y_width), (int) (x * x_height), x_height,y_width, null);
            }
        }   
    }
}
