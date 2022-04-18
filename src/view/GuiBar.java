package view;

import model.Board;
import model.Game;
import model.GamePVP;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class GuiBar extends JPanel{
    private Game game;
    private ArrayList<Player> players;
    private BufferedImage barImage;
    private Font font;
    private LinkedList<BufferedImage> playerImagesList=new LinkedList<>();

    public GuiBar(Game game){
        this.game=game;
        this.players=game.getBoard().getPlayerList();
        try{
            loadPlayerImages();
            font=Font.createFont(Font.TRUETYPE_FONT,new File("resources/retro.ttf")).deriveFont(12F);
            barImage=ImageIO.read(new File("resources/bar.png"));
        } catch(IOException|FontFormatException e){
            e.printStackTrace();
        }
    }

    private void loadPlayerImages() throws IOException{
        playerImagesList.add(ImageIO.read(new File("resources/player_head_0.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_head_1.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_head_2.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_head_3.png")));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D) g;
        try{
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            paintBar(g2);
            drawValues(g2);
            drawPlayersHeads(g2);
            if(game.getPaused()){
                paintFilter(g2);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void paintBar(Graphics2D g2) throws IOException{
        g2.drawImage(barImage,0,0,this.getWidth(),this.getHeight(),null);
    }

    private void drawValues(Graphics2D g2) throws IOException{
        int minutes=((int)(Game.timer/1000)%3600)/60;
        int seconds=((int)(Game.timer/1000)%60);
        g2.drawString(minutes+":"+(seconds<10?"0"+seconds:seconds),(int)(this.getWidth()/6.5),2*(this.getHeight())/3);
        int multiplier=0;
        for(Player player : players){
            g2.drawString(String.valueOf(player.getPoints()), (float)(multiplier*this.getWidth()/20+(9.5)*(this.getWidth()/20)), (float)(2*(this.getHeight())/3));
            multiplier+=3;
        }
    }

    private void drawPlayersHeads(Graphics2D g2) throws IOException{
        int multiplier=0;
        for(int i=0; i<players.size(); i++){
            g2.drawImage(playerImagesList.get(i),(int)(this.getWidth()/20*((7.8)+multiplier)),this.getHeight()/5,this.getWidth()/Board.cases[0].length,this.getHeight()/2 ,null);
            multiplier+=3;
        }
    }

    private void paintFilter(Graphics2D g){
        int alpha=157;
        Color blackFilter=new Color(0, 0, 0,alpha);
        g.setColor(blackFilter);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
