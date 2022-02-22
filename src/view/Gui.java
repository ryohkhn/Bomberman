package view;

import model.Board;

import javax.swing.*;

public class Gui extends JFrame{
    private GuiMenu guiMenu;
    private GuiBar guiBar;
    private GuiBoard guiBoard;
    public static int width;
    public static int height; 

    public Gui(Board board){
        width = 610;
        height = 630;
        this.guiMenu=new GuiMenu();
        this.guiBar=new GuiBar();
        this.guiBoard=new GuiBoard(board);
        setSize(610,550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(guiBoard);
        setVisible(true);
    }
}
