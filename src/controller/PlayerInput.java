package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import model.Player;

public class PlayerInput extends KeyAdapter{
	
	private Player player;
	
	public PlayerInput(Player player) {
		this.player = player;
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		int k = e.getKeyCode();
		if(k == player.getKeyUp()) {
			player.setPressUp();
		}
		else if(k == player.getKeyDown()) {
			player.setPressDown();
		}
		else if(k == player.getKeyRight()) {
			player.setPressRight();
		}
		else if(k == player.getKeyLeft()) {
			player.setPressLeft();
		}
		else if(k == player.getKeyAction()) {
			player.setAction();
		}
	}
	@Override
	public void keyReleased(KeyEvent e){
		int k = e.getKeyCode();
		if(k == player.getKeyUp()) {
			player.setReleasedUp();
		}
		else if(k == player.getKeyDown()) {
			player.setReleasedDown();
		}
		else if(k == player.getKeyRight()) {
			player.setReleasedRight();
		}
		else if(k == player.getKeyLeft()) {
			player.setReleasedLeft();
		}
		else if(k == player.getKeyAction()) {
			player.setReleasedAction();
		}
	}

}
