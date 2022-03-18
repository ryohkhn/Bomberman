package model;

import java.awt.image.BufferedImage;

public class Bot extends Player {

	//minimal ai
	public Bot(BufferedImage image, int id, float x, float y, Board board) {
		super(image, id, x, y, board);
		super.ai = true;
	}
	private int thinkTime=75;
	
	private int nextInvoke=0;
	
	private boolean letsPlantBomb = false;
	
	@Override
	public void update(double deltaTime) {
		if (isAlive()) {
			nextInvoke = (++nextInvoke)%thinkTime;
		 
        	if(nextInvoke == 1) {
				stop();
        		chooseDirection();
        	}
		}
		super.update(deltaTime);
	}
	
	private void stop() {
		setReleasedDown();
		setReleasedLeft();
		setReleasedRight();
		setReleasedUp();
		double shouldIPlant = Math.random();
		if(shouldIPlant > 0.9) {
			letsPlantBomb = true;
			setAction();
		}else {
			letsPlantBomb = false;
		}
	}
	
	private void chooseDirection() {
		int direction = (int)(Math.random()*4) ;
        switch(direction) {
        	case 0: // up
        		setPressUp();
        		if(letsPlantBomb) {
        			setPressRight();
        		}
        		break;
        	case 1: // down
        		setPressDown();
        		if(letsPlantBomb) {
        			setPressLeft();
        		}
        		break;
        	case 2: // left
        		setPressLeft();
        		if(letsPlantBomb) {
        			setPressUp();
        		}
        	break;
        	case 3: // right
        		setPressRight();
        		if(letsPlantBomb) {
        			setPressDown();
        		}
        	break;
			default:
			break;
        }
	}
	 
	public void resetInvoke() {
		nextInvoke = 0;
	}
}
