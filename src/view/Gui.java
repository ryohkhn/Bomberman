package view;

import model.Board;
import model.Player;

import javax.swing.*;
import java.util.ArrayList;

public class Gui extends JFrame implements Runnable{
    private GuiMenu guiMenu;
    private GuiBar guiBar;
    private GuiBoard guiBoard;

    public Gui(Board board){
        this.guiMenu=new GuiMenu();
        this.guiBar=new GuiBar();
        this.guiBoard=new GuiBoard(board);
        this.add(guiBoard);
        setSize(610,630);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void run(){
        guiBoard.repaint();
    }
}
