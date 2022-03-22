package view;

import model.GamePVP;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GuiBar extends JPanel{
    private ArrayList<Player> players;

    public GuiBar(ArrayList<Player> playerList){
        this.players=playerList;
        this.setLayout(new FlowLayout());
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D) g;
        try{
            Font font=Font.createFont(Font.TRUETYPE_FONT,new File("resources/retro.ttf")).deriveFont(12F);
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            paintBar(g2);
            drawValues(g2);
        } catch(IOException|FontFormatException e){
            e.printStackTrace();
        }
    }

    private void paintBar(Graphics2D g2) throws IOException{
        File barImage=new File("resources/bar.png");
        g2.drawImage(ImageIO.read(barImage),0,0,this.getWidth(),this.getHeight(),null);
    }

    private void drawValues(Graphics2D g2) throws IOException{
        int minutes=((int)(GamePVP.timer/1000)%3600)/60;
        int seconds=((int)(GamePVP.timer/1000)%60);
        g2.drawString(minutes+":"+(seconds<10?"0"+seconds:seconds),(int)(this.getWidth()/6.5),2*(this.getHeight())/3);
        for(int i=0; i<players.size(); i++){
            //48
            int multiplier=i%2==0?48+((i+1)*12):(48+((i+1)*13));
            g2.drawString(String.valueOf(players.get(i).getPoints()),multiplier*(this.getWidth()/100),2*(this.getHeight())/3);
        }
    }
}
