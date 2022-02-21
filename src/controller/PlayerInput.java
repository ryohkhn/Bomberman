package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Player;

public class PlayerInput extends KeyAdapter{
	
	private Player player;
	
	public PlayerInput(Player player) {
		this.player = player;
	}
	
	public void keyPressed(KeyEvent e){
  	  player.keyPressed(e);
    }
	
	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}	
	
}
