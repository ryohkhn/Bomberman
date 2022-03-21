package view;

import model.GamePVP;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuiBar extends JPanel{
    private JLabel timer;
    private JLabel[] playerPoints;
    private ArrayList<Player> players;

    public GuiBar(ArrayList<Player> playerList){
        this.players=playerList;
        this.setLayout(new FlowLayout());
        timer=new JLabel();
        playerPoints=new JLabel[players.size()];
        this.add(timer);
        for(int i=0; i<playerPoints.length; i++){
            playerPoints[i]=new JLabel("oui");
            this.add(playerPoints[i]);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        labelUpdate();
    }

    private void labelUpdate(){
        int seconds=(int)(GamePVP.timer/1000);
        timer.setText("Timer "+((seconds%3600)/60)+":"+(seconds%60));
        for(int i=0; i<players.size(); i++){
            playerPoints[i].setText("Player "+players.get(i).getId()+" : 0");
        }
    }
}
