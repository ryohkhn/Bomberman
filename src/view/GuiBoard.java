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
        int y_width=this.getWidth()/cases[0].length;
        int x_height=this.getHeight()/cases.length;
        for(Case[] line : cases) {
            for(Case c : line) {
                if(c.getWall() == null) {
                	File image = null;
                	if(c.getBonus() != null) {
                        image=new File(c.getBonus().getSprite());
                	}
                	else {
                        image=new File("resources/block.png");
                	}
                    BufferedImage bufferedImage=ImageIO.read(image);
                    g2.drawImage(ImageIO.read(image),x_pos,y_pos,x_height,y_width,null);
                }
                else if(c.getWall().isBreakable()){
                    File image=new File("resources/block_breakable.png");
                    BufferedImage bufferedImage=ImageIO.read(image);
                    g2.drawImage(ImageIO.read(image),x_pos,y_pos,x_height,y_width,null);
                }
                else{
                    File image=new File("resources/block_unbreakable.png");
                    BufferedImage bufferedImage=ImageIO.read(image);
                    g2.drawImage(ImageIO.read(image),x_pos,y_pos,x_height,y_width,null);
                }
                x_pos+=x_height;
            }
            x_pos=0;
            y_pos+=y_width;
        }
    }

    private void paintPlayers(Graphics2D g2) throws IOException{
        for(Player player:players){
            float x=player.getPositionX()-0.4F;
            float y=player.getPositionY()-0.4F;
            int x_height=this.getHeight()/board.getCases().length;
            int y_width=this.getWidth()/board.getCases()[0].length;
            BufferedImage image = player.getImage();
            if (image == null) {
                switch(player.getId()){
                    case 0:
                        image=loader.loadImage("resources/player_0.png");
                        break;
                    case 1:
                        image=loader.loadImage("resources/player_1.png");
                        break;
                    case 2:
                        image=loader.loadImage("resources/player_2.png");
                        break;
                    case 3:
                        image=loader.loadImage("resources/player_3.png");
                        break;
                    default:
                        break;
                }
            }
            assert(image != null);
            g2.drawImage(image,(int)(y*y_width),(int)(x*x_height),y_width,x_height,null);
        }
    }
}
