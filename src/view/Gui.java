package view;

import model.Board;
import model.GamePVP;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Gui extends JFrame{
    private GuiMenu guiMenu;
    private GuiBar guiBar;
    private GuiBoard guiBoard;
    public static int width;
    public static int height; 

    public Gui(Board board){
        width = 600;
        height = 553;
        this.guiMenu=new GuiMenu(this);
        this.guiBar=new GuiBar(board.getPlayerList());
        this.guiBoard=new GuiBoard(board);

        setSize(600,553);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        guiBar.setPreferredSize(new Dimension(this.getHeight()/15,this.getWidth()/15));

        this.add(guiMenu,BorderLayout.CENTER);
        
        //this.add(guiBar,BorderLayout.NORTH);
        
        //this.add(guiBoard,BorderLayout.CENTER);
        //setUndecorated(true);
        setVisible(true);
    }

    public void repaintGui(){
        guiBar.repaint();
        guiBoard.repaint();
    }

	public void startGame() {
		this.remove(guiMenu);
		this.add(guiBar,BorderLayout.NORTH);
		this.add(guiBoard,BorderLayout.CENTER);
	}
}
