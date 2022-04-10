package view;

import model.Board;
import model.Game;
import model.GamePVP;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    private GuiMenu guiMenu;
    private GuiBar guiBar;
    private GuiBoard guiBoard;
    private Board board;
    public static int width;
    public static int height; 
    private Game game;

    public Gui(){
        width = 600;
        height = 553;
        this.guiMenu=new GuiMenu(this);

        setSize(600,553);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        this.add(guiMenu,BorderLayout.CENTER);
        setVisible(true);
    }

    public void repaintGui(){
        guiBar.repaint();
        guiBoard.repaint();
    }

	public void startGame(){
        switch (guiMenu.getGamemode()) {
        	case 0:
        		game = new GamePVP(guiMenu.getMap(),guiMenu.getNumberOfPlayers(),guiMenu.getNumberOfAI(),this);
        		break;
        	case 1:
        		break;

        }
   		this.game = new GamePVP(guiMenu.getMap(),guiMenu.getNumberOfPlayers(),guiMenu.getNumberOfAI(),this);
        this.board = game.init();
        this.guiBar=new GuiBar(board.getPlayerList());
        this.guiBoard=new GuiBoard(this.board);
        guiBar.setPreferredSize(new Dimension(this.getHeight()/15,this.getWidth()/15));
        this.remove(guiMenu);
		this.add(guiBar,BorderLayout.NORTH);
		this.add(guiBoard,BorderLayout.CENTER);
        repaintGui();
        revalidate();
	}

	public static void main(String[] args){
		Gui gui = new Gui();
	}
}
