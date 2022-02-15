package model;

import view.Gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Board{
    private Case[][] cases;
    private ArrayList<ArrayList<String>> mapLayout;
	private BufferedReader bufferedReader;
	private ArrayList<Player> playerList;
    
	public Board(String filename, ArrayList<Player> playerList) throws FileNotFoundException {
		this.playerList = playerList;
		this.loadBoardFile(filename);
		this.loadCases();
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
		this.cases = new Case[this.mapLayout.size()][this.mapLayout.get(0).size()];
    	int lineCount = 0;
    	for(ArrayList<String> line : this.mapLayout) {
			int columnCount = 0;
    		for(String c : line) {
    			
				Case currentCase = new Case();
    			
				switch(c) {
	    			case "1":
	    				currentCase.addMovableOnCase(this.playerList.get(0));
	    				break;
	    				
	    			case "2":
	    				currentCase.addMovableOnCase(this.playerList.get(1));
	    				break;
	    				
	    			case "3":
	    				currentCase.addMovableOnCase(this.playerList.get(2));
	    				break;
	    				
	    			case "4":
	    				currentCase.addMovableOnCase(this.playerList.get(3));
	    				break;
	    				
	    			case "E":
	    				Random rand = new Random();
	    				int r = rand.nextInt(101);
	        			if(r<81) {
	        				currentCase.setWall(new Wall(true));
	        			}
	    				break;
	    			
	    			case "H":
	    				currentCase.setWall(new Wall(false));
	    				break;
	    				
	    			default:
	    				break;
    			}
    			this.cases[lineCount][columnCount] = currentCase;
    			
    			columnCount++;
    		}
    		lineCount++;
    	}
    }
    
    public void printCases() {
    	for(Case[] line : this.cases) {
    		for(Case c : line) {
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
    			System.out.print(" ");
    		}
    		System.out.println();
    	}
    }

	public Case[][] getCases(){
		return cases;
	}

	public ArrayList<Player> getPlayerList(){
		return playerList;
	}

	public static void main(String[] args){
    	ArrayList<Player> players = new ArrayList<Player>();
    	
		players.add(new Player(0));
		players.add(new Player(1));
		players.add(new Player(2));
		players.add(new Player(3));
    	Board board = null;
		try {
			board = new Board("maps/default.csv",players);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	//System.out.println(board.mapLayout.toString() + "\n");
    	board.printCases();
		Gui gui=new Gui(board);
    }
}
