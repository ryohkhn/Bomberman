package view;

import model.Board;
import model.Game;
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
    private final Game game;
    private final ArrayList<Player> players;
    private BufferedImage barImage;
    private BufferedImage pointHolder;
    private BufferedImage largePointHolder;
    private Font font;
    private final LinkedList<BufferedImage> playerImagesList=new LinkedList<>();

    /**
     * Constructor for GuiBar class
     * @param game Game object
     */
    public GuiBar(Game game){
        this.game=game;
        this.players=game.getBoard().getPlayerList();
        try{
            loadPlayerImages();
            loadBarImages();
            font=Font.createFont(Font.TRUETYPE_FONT,new File("resources/retro.ttf")).deriveFont(12F);
        } catch(IOException|FontFormatException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads each player images
     * @throws IOException IO exception when loading images
     */
    private void loadPlayerImages() throws IOException{
        playerImagesList.add(ImageIO.read(new File("resources/player_head_0.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_head_1.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_head_2.png")));
        playerImagesList.add(ImageIO.read(new File("resources/player_head_3.png")));
    }

    /**
     * Load bar images
     * @throws IOException IO exception when loading images
     */
    private void loadBarImages() throws IOException{
        barImage=ImageIO.read(new File("resources/bar.png"));
        pointHolder=ImageIO.read(new File("resources/point_holder.png"));
        largePointHolder=ImageIO.read(new File("resources/large_point_holder.png"));
    }

    /**
     * Java paintComponent that calls every paint function
     * @param g paintComponent Graphics object
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g;
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        drawBarImage(g2);
        paintBar(g2);
        drawValues(g2);
        drawPlayersHeads(g2);
        if(game.getPaused()){
            paintFilter(g2);
        }
        if(game.hasEnded()) paintFilter(g2);
    }

    /**
     * Paint background bar image
     * @param g2 paintComponent Graphics object
     */
    private void drawBarImage(Graphics2D g2){
        g2.drawImage(barImage,0,0,this.getWidth(),this.getHeight(),null);
    }

    /**
     * Paint point holder and timer holder images
     * @param g2 paintComponent Graphics object
     */
    private void paintBar(Graphics2D g2){
        if(game.isGamePvp()){
            int multiplier=0;
            for(Player player : players){
                g2.drawImage(pointHolder,(int)(this.getWidth()/20*((9.4)+multiplier)),this.getHeight()/5,this.getWidth()/20,3*(this.getHeight()/5) ,null);
                multiplier+=3;
            }
        }
        else{
            g2.drawImage(largePointHolder,this.getWidth()/2,this.getHeight()/5,this.getWidth()/5,3*(this.getHeight()/5),null);
        }
    }

    /**
     * Draw values on the bar related to time and player points
     * @param g2 paintComponent object used to paint
     */
    private void drawValues(Graphics2D g2){
        int minutes=((int)(Game.timer/1000)%3600)/60;
        int seconds=((int)(Game.timer/1000)%60);
        int points=0;
        g2.drawString(minutes+":"+(seconds<10?"0"+seconds:seconds),3*(this.getWidth()/20),3*(this.getHeight())/5);
        int multiplier=0;
        if(game.isGamePvp()){
            for(Player player : players){
                g2.drawString(String.valueOf(player.getPoints()),(float)(this.getWidth()/20*((9.7)+multiplier)),(float)(3*(this.getHeight())/5));
                multiplier+=3;
            }
        }
        else{
            for(Player p:players){
                points+=p.getPoints();
            }
            g2.drawString(String.valueOf(points), (int)(11.8*this.getWidth()/20),3*this.getHeight()/5);
        }

    }

    /**
     * Draw players heads images
     * @param g2 paintComponent Graphics object
     */
    private void drawPlayersHeads(Graphics2D g2){
        int multiplier=0;
        if(game.isGamePvp()){
            for(int i=0; i<players.size(); i++){
                g2.drawImage(playerImagesList.get(i),(int)(this.getWidth()/20*((7.8)+multiplier)),this.getHeight()/5,this.getWidth()/Board.cases[0].length,this.getHeight()/2 ,null);
                multiplier+=3;
            }
        }
    }

    /**
     * Paint a transparent filter when the game is paused or ended
     * @param graphics paintComponent Graphics object
     */
    private void paintFilter(Graphics2D graphics){
        int alpha=157;
        Color blackFilter=new Color(0, 0, 0,alpha);
        graphics.setColor(blackFilter);
        graphics.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
