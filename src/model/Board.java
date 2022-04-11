package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Board{
	// cases représente les lignes
	// cases[0] représente les colonnes
    public static Case[][] cases;
    private ArrayList<ArrayList<String>> mapLayout;
	private BufferedReader bufferedReader;
	private ArrayList<Player> playerList;
	private ArrayList<Monster> monsterList;
	public static int sizeRow;
	public static int sizeCol;
	private Thread thread;
    
	public Board(String filename,ArrayList<Player> p) throws FileNotFoundException {
		this.monsterList = new ArrayList<>();
		this.playerList = p;
		this.loadBoardFile(filename);
		this.loadCases();
		//this.printCases();
	}

	public Case[][] getCases() {
		return cases;
	}
	
    //take filename and load the board
    public void loadBoardFile(String filename) throws FileNotFoundException   {
    	// Loading map file
        try {
            this.bufferedReader = new BufferedReader(new FileReader(filename));
        } catch (IOException | NullPointerException e) {
            // Load default map when map file could not be loaded
            System.err.println(e + ": Cannot load map file, loading default map");
            this.bufferedReader = new BufferedReader(new FileReader("maps/default.csv"));
        }

        // Parsing map data from file
        this.mapLayout = new ArrayList<>();
    	try {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (currentLine.isEmpty()) {
                    continue;
                }
                // Split row into array of strings and add to array list
                mapLayout.add(new ArrayList<>(Arrays.asList(currentLine.split(","))));
            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e + ": Error parsing map data");
            e.printStackTrace();
        }
    }
    
    
    //create array of array of cases from mapLayout
    public void loadCases() {
		sizeCol = this.mapLayout.size();
		sizeRow = this.mapLayout.get(0).size();
		cases = new Case[this.mapLayout.size()][this.mapLayout.get(0).size()];
    	int lineCount = 0;
    	for(ArrayList<String> line : this.mapLayout) {
			int columnCount = 0;
    		for(String c : line) {
    			
				Case currentCase = new Case();
    			
				switch(c) {
	    			case "1":
						this.playerList.add(new Player(0, 0, 0, this));
	    				currentCase.addMovableOnCase(this.playerList.get(0));
	    				break;
	    				
	    			case "2":
						this.playerList.add(new Player(1, 0, 0, this));
						//this.playerList.add(new Bot(1, 0, 0, this));
						currentCase.addMovableOnCase(this.playerList.get(1));
						//this.monsterList.add(new MonstreUn(0, 0, this));
						//currentCase.addMovableOnCase(this.monsterList.get(0));
	    				break;
	    				
	    			case "3":
						this.playerList.add(new Player(2, 0, 0, this));
	    				currentCase.addMovableOnCase(this.playerList.get(1));
	    				break;
	    				
	    			case "4":
						this.playerList.add(new Player(3, 0, 0, this));
	    				currentCase.addMovableOnCase(this.playerList.get(2));
	    				break;
	    				
	    			case "E":
	    				Random rand = new Random();
	    				int r = rand.nextInt(101);
	        			if(r<81) {
	        				currentCase.setWall(new Wall(true));
	        				if(r<61) {
	        					currentCase.setBonus(new Bonus(Bonus.randomBonus()));
	        				}
	        			}
	    				break;
	    			
	    			case "H":
	    				currentCase.setWall(new Wall(false));
	    				break;
	    				
	    			default:
	    				break;
    			}
				this.monsterList.add(new MonstreUn(0, 0, this));
				
    			cases[lineCount][columnCount] = currentCase;
    			
    			columnCount++;
    		}
    		lineCount++;
    	}
    }
    
    public void printCases() {
    	for(Case[] line : cases) {
    		for(Case c : line) {
    			if(c.getBomb() != null) System.out.print("(");
    			if(c.getWall() != null) {
    				String res = c.getWall().isBreakable()?"B":"H";
    				System.out.print(res);
    			}
    			else {
    				if(c.getMovablesOnCase().size()==0) {
    					System.out.print("E");
    				}
    				else {
    					System.out.print(((Player) (c.getMovablesOnCase().get(0))).getId());
    				}
    			}
    			if(c.getBomb() != null) System.out.print(")");
    			else System.out.print(" ");
    		}
    		
    		System.out.println();
    	}
    }

	public ArrayList<Player> getPlayerList(){
		return playerList;
	}

	public int getCasesCol() {
		return cases[0].length;
	}

	public int getCasesRow() {
		return cases.length;
	}

	public Player getPlayer(int i) {
		return playerList.get(i);
	}

	public ArrayList<Monster> getMonsterList() {
		return monsterList;
	}

	public Monster getMonster(int i) {
		return monsterList.get(i);
	}
}
