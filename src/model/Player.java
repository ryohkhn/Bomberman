package model;

import java.awt.image.BufferedImage;

public class Player extends GameObject implements Movable{
    private int id;
    private final float speed = 0.2F;
    public int keyUp, keyDown, keyLeft, keyRight,keyAction;

    public Player(BufferedImage image,int id,float x,float y) {
        super(image,x,y);
    	this.id = id;
    }
    
	public int getId() {
		return id;
	}

    public void bindKeys(int up, int down, int left, int right,int action) {
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		keyAction = action;
	}

	public float getSpeed() {
		return speed;
	}

    public void detectCollisionDown() {
		if (position.y % 1 >= 0.75F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column+1;
			System.out.println(Board.cases[0].length);
			if (nextColumn<Board.cases.length-1) {
				System.out.println("Line "+line);
				System.out.println("Column "+nextColumn);
				if(Board.cases[line][nextColumn].getWall()==null){
					position.y+=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[line][nextColumn].addMovableOnCase(this);
				}
			}
			System.out.println(position.y);
		}else {
			position.y+=speed;
		}
		System.out.println(position.y);
	}
	
	public void detectCollisionUp() {
		if (position.y % 1 <= 0.2F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column-1;
			if (nextColumn>0){
				if(Board.cases[line][nextColumn].getWall()==null){
					position.y-=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[line][nextColumn].addMovableOnCase(this);
				}
			}
			System.out.println(position.y);
		}else {
			position.y-=speed;
		}
		System.out.println(position.y);
	}
	
	public void detectCollisionLeft() {
		if (position.x % 1 <= 0.2F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int previousLine=line-1;
			if (previousLine>0) {
				if(Board.cases[previousLine][column].getWall()==null){
					position.x-=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[previousLine][column].addMovableOnCase(this);
				}
			}
			System.out.println(position.x);
		}else {
			position.x-=speed;
		}
		System.out.println(position.x);
	}
	
	public void detectCollisionRight() {
		if (position.x % 1 >= 0.8F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line+1;
			if (nextLine<Board.cases.length) {
				if(Board.cases[nextLine][column].getWall()==null){
					position.x+=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[nextLine][column].addMovableOnCase(this);
				}
			}
			System.out.println(position.x);
		}else {
			position.x+=speed;
		}
		System.out.println(position.x);
	}

	public void setPlayer(BufferedImage a,int ind,float x,float y) {
		this.image = a;
		this.id = ind;
		this.setAttributs(a,x,y);
	}
}
