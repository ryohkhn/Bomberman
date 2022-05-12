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
	private boolean monstermode;
    
	/**
	 * Constructor for board
	 * @param filename path to map csv file
	 * @param mode gamemode pvp or monsters
	 * @throws FileNotFoundException
	 */
	public Board(String filename,boolean mode) throws FileNotFoundException {
		this.loadBoardFile(filename);
		monstermode = mode;
		this.loadCases();
	}
	
	/**
	 * Take filename and load the board
	 * @param filename file path to a csv file of the board
	 * @throws FileNotFoundException
	 */
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
    
    
    /**
     * Create array of array of cases from mapLayout
     */
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
	    			case "E":
	    				Random rand = new Random();
	    				int r = rand.nextInt(101);
	        			if(!monstermode && r<81) {
	        				currentCase.setWall(new Wall(true));
	        				if(r<31) {
	        					currentCase.setBonus(new Bonus(Bonus.randomBonus()));
	        				}
	        			}
						if(monstermode && r<45) {
	        				currentCase.setWall(new Wall(true));
	        				if(r<10) {
	        					currentCase.setBonus(new Bonus(Bonus.randomBonus()));
	        				}
	        			}
	    				break;
	    			case "H":
	    				currentCase.setWall(new Wall(false));
	    				break;
	    			case "M":
						
						break;
	    			default:
	    				break;
    			}
				
    			cases[lineCount][columnCount] = currentCase;
    			columnCount++;
    		}
    		lineCount++;
    	}
    }
	
	/*
	 * Getters and setters:
	 */
    
	public Case[][] getCases() {
		return cases;
	}

	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}

	public void setMonsterList(ArrayList<Monster> monsterList) {
		this.monsterList = monsterList;
	}

	public boolean getMonsterMode() {
		return monstermode;
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
	
	public ArrayList<Player> getPlayerList(){
		return playerList;
	}

	public int getCasesCol() {
		return cases[0].length;
	}

	public int getCasesRow() {
		return cases.length;
	}
}
