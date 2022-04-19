package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;


public class GuiMenu extends JPanel implements ActionListener{
	private final JPanel buttonPanel = new JPanel();
	private JButton newGame;
    private JButton settings;
    private JButton quit;
    private Gui frame;
    
    private int gamemode = 1; // 0 monster 1 pvp TODO switch les deux game modes une fois monstres réalisés pour solo versus monstres
    private int map = 0; //0 non selected 1-3 selected
    private int numberOfPlayers = 4; // 1-4
    private int numberOfAI = 0;
    
	public GuiMenu(Gui frame) {
		this.frame = frame;
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		// Buttons
		newGame = new JButton("Start Game");
		//newGame.setToolTipText("Click this button to start a new game.");
		settings = new JButton("Settings");
		//settings.setToolTipText("Click this button change the settings of the game.");
		quit = new JButton("Quit");
		//quit.setToolTipText("Click this button quit the game.");
		
		newGame.addActionListener(newGameAction);
		settings.addActionListener(settingsAction);
		quit.addActionListener(quitAction);
		buttonPanel.setPreferredSize(new Dimension(500,350));
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 120)));
		buttonPanel.add(newGame);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		buttonPanel.add(settings);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		buttonPanel.add(quit);
		
		JButton[] buttons = {newGame, settings, quit};
		for(JButton b : buttons) {
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			b.setPreferredSize(new Dimension(200,60));
			b.setUI(new StyledButtonUI());
		}
		
		//////////////////
		//End of buttons//
		//////////////////
		
		try
		{
		    setUIFont(new javax.swing.plaf.FontUIResource("Courier",Font.BOLD,12));
		}
		catch(Exception e){}
		
		buttonPanel.setOpaque(true);
		buttonPanel.setBackground(new Color(0,0,0,0));
		add(buttonPanel);
		setVisible(true);
	}
	
	 /**
     * This Action disposes the current frame to make room for a new game.
     */
    public Action newGameAction = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
		frame.startGame();
	}
    };
    /**
     * This Action is called when the user clicks on the settings button, it creates a new SettingsFrame.
     */
    public Action settingsAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    
	        JPanel settingPanel = new JPanel();
	        settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
	        
	        //
	        // Gamemode
	        //
	                
	        JRadioButton gamePvpButton = new JRadioButton();
	        JRadioButton gameMonsterButton = new JRadioButton();
	        gamePvpButton.setText("PVP");
	        gameMonsterButton.setText("Monsters");
	        
	        switch(getGamemode()) {
		        case 0:
					gameMonsterButton.setSelected(true);
		        	break;
		        case 1:
		        	gamePvpButton.setSelected(true);
		        	break;
	        }
	        
	        ButtonGroup editableGroup = new ButtonGroup();
	        editableGroup.add(gamePvpButton);
	        editableGroup.add(gameMonsterButton);
	        
	        gamePvpButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                gamemode = 1;
	            }
	        });
	        
	        gameMonsterButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                gamemode = 0;
	            }
	        });
	        
	        JPanel gamemodes = new JPanel();
	        gamemodes.add(new JLabel("Select gamemode:"));
	        gamemodes.add(gamePvpButton);
	        gamemodes.add(gameMonsterButton);
	        gamemodes.setOpaque(true);
	        gamemodes.setBackground(new Color(0,0,0,0));
	        
	        //
	        // Number of players and AI
	        //
	        
	        JPanel nbPlayers = new JPanel();
	        Integer[] optionsToChoose = {1,2,3,4};
	        JComboBox<Integer> jComboBox = new JComboBox<>(optionsToChoose);
	        jComboBox.setSelectedItem(getNumberOfPlayers());
	       
	        
	        nbPlayers.add(new JLabel("Number of players: "));
	        nbPlayers.add(jComboBox);
	        nbPlayers.setOpaque(true);
	        nbPlayers.setBackground(new Color(0,0,0,0));

	        JPanel nbAI = new JPanel();
	        Integer[] optionsToChooseAI = {0,1,2,3};
	        JComboBox<Integer> jComboBoxAI = new JComboBox<>(optionsToChooseAI);
	        jComboBoxAI.setSelectedItem(getNumberOfAI());

	        nbAI.add(new JLabel("Number of AI: "));
	        nbAI.add(jComboBoxAI);
	        nbAI.setOpaque(true);
	        nbAI.setBackground(new Color(0,0,0,0));
	        
	        jComboBoxAI.addActionListener (new ActionListener () {
	            public void actionPerformed(ActionEvent e) {
	                numberOfAI = (jComboBoxAI.getItemCount() == 0) ? 0 : (int) jComboBoxAI.getSelectedItem();
	                repaint();
	            }
	        });
	        
	        jComboBox.addActionListener (new ActionListener () {
	            public void actionPerformed(ActionEvent e) {
	                numberOfPlayers = (int) jComboBox.getSelectedItem();
	                jComboBoxAI.removeAllItems();		                
	                for(int i = 0; i < 5 - getNumberOfPlayers(); i++) {
		                jComboBoxAI.addItem(i);
	                }
	            }
	        });
	        transparentBox(jComboBox);
	        transparentBox(jComboBoxAI);
	        
	        //
	        // Maps
	        //
	        
	        GridLayout mapLayout = new GridLayout(1,3);
	        mapLayout.setHgap(25);
	        JPanel maps = new JPanel(mapLayout);
	        boolean mapError = false;
			try {
				ImageIcon map1 = new ImageIcon(ImageIO.read(new File("resources/map.png")));
				ImageIcon map2 = new ImageIcon(ImageIO.read(new File("resources/map.png")));
				ImageIcon map3 = new ImageIcon(ImageIO.read(new File("resources/map.png")));

		        ImageIcon map1selected = new ImageIcon(ImageIO.read(new File("resources/map_selected.jpg")));
		        ImageIcon map2selected = new ImageIcon(ImageIO.read(new File("resources/map_selected.jpg")));
		        ImageIcon map3selected = new ImageIcon(ImageIO.read(new File("resources/map_selected.jpg")));
		        
		        JLabel map1label = new JLabel(map1);
		        JLabel map2label = new JLabel(map2);
		        JLabel map3label = new JLabel(map3);
		        
		        switch(map) {
			        case 0:
			        	break;
			        case 1:
				        map1label.setIcon(map1selected);
			        	break;
			        case 2:
				        map2label.setIcon(map2selected);
			        	break;
			        case 3:
				        map3label.setIcon(map3selected);
			        	break;
			        default:
			        	break;
		        }
		        
		        map1label.addMouseListener(new MouseListener() 
		        {
		            @Override
		            public void mouseClicked(MouseEvent e) 
		            {
				        map1label.setIcon(map1selected);
				        map2label.setIcon(map2);
				        map3label.setIcon(map3);
				        map = 1;
		            }
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) {}
					public void mousePressed(MouseEvent arg0) {}
					public void mouseReleased(MouseEvent arg0) {}
		        });
		        
		        map2label.addMouseListener(new MouseListener() 
		        {
		            @Override
		            public void mouseClicked(MouseEvent e) 
		            {
				        map1label.setIcon(map1);
				        map2label.setIcon(map2selected);
				        map3label.setIcon(map3);
				        map = 2;
		            }
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) {}
					public void mousePressed(MouseEvent arg0) {}
					public void mouseReleased(MouseEvent arg0) {}
		        });
		        
		        map3label.addMouseListener(new MouseListener() 
		        {
		            @Override
		            public void mouseClicked(MouseEvent e) 
		            {
				        map1label.setIcon(map1);
				        map2label.setIcon(map2);
				        map3label.setIcon(map3selected);
				        map = 3;
		            }
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) {}
					public void mousePressed(MouseEvent arg0) {}
					public void mouseReleased(MouseEvent arg0) {}
		        });
		        maps.add(map1label);
		        maps.add(map2label);
		        maps.add(map3label);
		        maps.setOpaque(true);
				maps.setBackground(new Color(0,0,0,0));
			} catch (IOException e1) {
				System.out.println("Maps not found");
				mapError = true;
			}

			//
			// Return
			//
			
	        JButton returnButton = new JButton("Return");
	        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	        returnButton.setUI(new StyledButtonUI());
	        returnButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                settingPanel.setVisible(false);
	                remove(settingPanel);
	                add(buttonPanel);
	    	        buttonPanel.setVisible(true);
	                repaint();
	            }
	        });
	        returnButton.setPreferredSize(new Dimension(150,30));  

	        JPanel game = new JPanel();
	        game.setLayout(new BoxLayout(game, BoxLayout.Y_AXIS));
	        
	        game.add(Box.createRigidArea(new Dimension(0, 20)));
	        game.add(gamemodes);
	        game.add(nbPlayers);
	        game.add(nbAI);
	        if(!mapError) {
		        game.add(Box.createRigidArea(new Dimension(0, 10)));
		        JLabel text = new JLabel("Select map:");
		        text.setAlignmentX(Component.CENTER_ALIGNMENT);
		        game.add(text);
		        game.add(Box.createRigidArea(new Dimension(0, 10)));
		        game.add(maps);
	        }

	        game.add(Box.createRigidArea(new Dimension(0, 20)));
	        settingPanel.add(game);
	        settingPanel.add(returnButton);
	        
	        JComponent[] comp = {gamePvpButton, gameMonsterButton, game, settingPanel};
	        for(JComponent c : comp) {
	        	c.setOpaque(true);
	        	c.setBackground(new Color(0,0,0,0));
	        }
	        
	        buttonPanel.setVisible(false);
	        remove(buttonPanel);
	        settingPanel.setVisible(true);
	        add(settingPanel);
	        repaint();
		}
    };
    /**
     * This Action terminates the current process.
     */
    public Action quitAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
    };
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	static class StyledButtonUI extends BasicButtonUI {

	    @Override
	    public void installUI (JComponent c) {
	        super.installUI(c);
	        AbstractButton button = (AbstractButton) c;
	        button.setOpaque(false);
	        button.setBorder(new EmptyBorder(5, 15, 5, 15));
	    }

	    @Override
	    public void paint (Graphics g, JComponent c) {
	        AbstractButton b = (AbstractButton) c;
	        paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
	        super.paint(g, c);
	    }

	    private void paintBackground (Graphics g, JComponent c, int yOffset) {
	        Dimension size = c.getSize();
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setColor(c.getBackground().darker());
	        g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
	        g.setColor(c.getBackground());
	        g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
	    }
	}
	
	public static void setUIFont(FontUIResource f)
	{   
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while(keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if(value instanceof FontUIResource) UIManager.put(key, f);
	    }
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage myImage;
		try {
			myImage = ImageIO.read(new File("resources/background.jpg"));
	        g.drawImage(myImage, 0, 0, null);
		} catch (IOException e) {}
	}

	public int getGamemode() {
		return gamemode;
	}

	public String getMap() {
		switch(this.map) {
		case 1:
			return "maps/default.csv";
		case 2:
			return "maps/empty.csv";
		case 3:
			return "maps/map2.csv";
		default:
			return "maps/default.csv";
		}
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public int getNumberOfAI() {
		return numberOfAI;
	}
	
	private void transparentBox(Container container) {
	      Component[] components = container.getComponents();
	      for (Component component : components) {
	         if (component instanceof AbstractButton) {
	            component.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						frame.repaint();
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						frame.repaint();
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						frame.repaint();
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						frame.repaint();
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						frame.repaint();
					}
	            });
	         }
	      }
	   }

}