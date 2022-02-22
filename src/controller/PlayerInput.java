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
		int k = e.getKeyCode();
		if(k == player.keyUp) {
			player.detectCollisionUp();
		}
		else if(k == player.keyDown) {
			player.detectCollisionDown();
		}
		else if(k == player.keyRight) {
			player.detectCollisionRight();
		}
		else if(k == player.keyLeft) {
			player.detectCollisionLeft();
		}
		else if(k == player.keyAction) {
			//bomber
		}
	}
}
