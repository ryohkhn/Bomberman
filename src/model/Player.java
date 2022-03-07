package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject implements Movable{
    private int id;
    private final float speed = 0.2F;
    public int keyUp, keyDown, keyLeft, keyRight,keyAction;
	public ArrayList<Bomb> bombList = new ArrayList<Bomb>();
	private int nbBomb=1;


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
		if (roundFloat(position.x % 1)>= 0.6F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line+1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+nextLine+" colonne: "+column);
			if(nextLine<Board.cases.length-1) {
				if(Board.cases[nextLine][column].getWall()==null){
					position.x+=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[nextLine][column].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
		}else {
			position.x+=speed;
			position.x=roundFloat(position.x);
		}
	}

	public void detectCollisionUp() {
		if (roundFloat(position.x % 1)<= 0.4F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextLine=line-1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+nextLine+" colonne: "+column);
			if (nextLine>0) {
				if(Board.cases[nextLine][column].getWall()==null){
					position.x-=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[nextLine][column].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
			//System.out.println(position.x);
		}else {
			position.x-=speed;
			position.x=roundFloat(position.x);
		}
		//System.out.println(position.x);
	}
	
	public void detectCollisionLeft() {
		if (roundFloat(position.y % 1)<= 0.4F) {
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column-1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+line+" colonne: "+nextColumn);
			if (nextColumn>0){
				if(Board.cases[line][nextColumn].getWall()==null){
					position.y-=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[line][nextColumn].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
			//System.out.println(position.y);
		}else {
			position.y-=speed;
			position.y=roundFloat(position.y);
		}
		//System.out.println(position.y);
	}
	
	public void detectCollisionRight() {
		if (roundFloat(position.y % 1)>= 0.6F){
			int line= (int)position.x;
			int column= (int)position.y;
			int nextColumn=column+1;
			//System.out.println("Case actuelle ligne: "+line+" colonne: "+column);
			//System.out.println("Case suivante ligne: "+line+" colonne: "+nextColumn);
			if(nextColumn<Board.cases[0].length-1) {
				if(Board.cases[line][nextColumn].getWall()==null){
					position.y+=speed;
					Board.cases[line][column].deleteMovableOnCase(this);
					Board.cases[line][nextColumn].addMovableOnCase(this);
				}
				else{
					System.out.println("Mur !");
				}
			}
			//System.out.println(position.y);
		}else {
			position.y+=speed;
			position.y=roundFloat(position.y);
		}
	}

	private float roundFloat(float f){
		return (float)(Math.round((f)*100.0)/100.0);
	}

	public void setPlayer(BufferedImage a,int ind,float x,float y) {
		this.image = a;
		this.id = ind;
		this.setAttributs(a,x,y);
	}

	public void dropBomb() {
		if(nbBomb > 0){
			bombList.add(new Bomb((int)position.x,(int)position.y)); // on ajoute la bombe aux coordonnées de la case (plus besoin du détail apres la virgule)

		}
	}


}
