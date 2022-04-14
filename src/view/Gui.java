package view;

import model.Board;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    private GuiMenu guiMenu;
    private GuiBar guiBar;
    private GuiBoard guiBoard;
    public static int width;
    public static int height; 

    public Gui(Board board){
        width = this.getWidth();
        height = this.getHeight();
        this.guiMenu=new GuiMenu();
        this.guiBar=new GuiBar(board.getPlayerList());
        this.guiBoard=new GuiBoard(board);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setPreferredSize(new Dimension(675,608));
        pack();

        this.setLayout(new BorderLayout());
        guiBar.setPreferredSize(new Dimension(this.getWidth()/15,this.getHeight()/13));

        this.add(guiBar,BorderLayout.NORTH);
        this.add(guiBoard,BorderLayout.CENTER);
        setVisible(true);
    }

    public void repaintGui(){
        guiBar.repaint();
        guiBoard.repaint();
    }

    public void endScreen() { // TODO: 11/04/2022 end screen (le perso du gagnant qui marche a coté d'un "YOU WON!" ce serait pas mal) 
    }
}
