package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import model.GamePVP;

public class GuiMenu extends JFrame implements ActionListener{
	private final JPanel buttonPanel = new JPanel();
	private JButton newGame;
    private JButton settings;
    private JButton quit;
    
    private int gamemode; // 0 pvp 1 monster
    
	public GuiMenu() {
		
		super("Bomberman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2,1));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		// Buttons
		String newGameLabel = "Start Game";
		String settingsLabel = "Settings";
		String quitLabel = "Quit";
		newGame = new JButton(newGameLabel);
		newGame.setToolTipText("Click this button to start a new game.");
		quit = new JButton(quitLabel);
		quit.setToolTipText("Click this button quit the game.");
		settings = new JButton(settingsLabel);
		
		newGame.addActionListener(newGameAction);
		settings.addActionListener(settingsAction);
		quit.addActionListener(quitAction);
		buttonPanel.setPreferredSize(new Dimension(500,350));
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 120)));
		buttonPanel.add(newGame);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		buttonPanel.add(settings);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		buttonPanel.add(quit);
		

		JButton[] buttons = {newGame, settings, quit};
		
		for(JButton b : buttons) {
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			b.setPreferredSize(new Dimension(150,50));
			b.setUI(new StyledButtonUI());
		}
		
		//////////////////
		//End of buttons//
		//////////////////
		
		
		class ImagePanel extends JPanel {
		    private Image image;
		    public ImagePanel(Image image) {
		        this.image = image;
		    }
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        g.drawImage(image, 0, 0, this);
		    }
		}

		try {
			BufferedImage myImage = ImageIO.read(new File("resources/background.jpg"));
			setContentPane(new ImagePanel(myImage));
		} catch (IOException e) {}
		
		buttonPanel.setOpaque(true);
		buttonPanel.setBackground(new Color(0,0,0,0));
		add(buttonPanel, BorderLayout.CENTER);
		
        setSize(600,553);
		setVisible(true);
	}
	
	 /**
     * This Action disposes the current frame to make room for a new game.
     */
    public Action newGameAction = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
	    //dispose();
	    //Engine.startGame();
	}
    };
    /**
     * This Action is called when the user clicks on the settings button, it creates a new SettingsFrame.
     */
    public Action settingsAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    //new SettingsFrame();
	        JPanel settingPanel = new JPanel();
	        JPanel game = new JPanel();
	        settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
	        JButton returnButton = new JButton("Return");
	        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        JRadioButton gamePvpButton = new JRadioButton();
	        JRadioButton gameMonsterButton = new JRadioButton();
	        gamePvpButton.setText("PVP");
	        gameMonsterButton.setText("Monsters");
	        
	        switch(gamemode) {
	        case 0:
	        	gamePvpButton.setSelected(true);
	        	break;
	        case 1:
	        	gameMonsterButton.setSelected(true);
	        	break;
	        }
	        
	        ButtonGroup editableGroup = new ButtonGroup();
	        editableGroup.add(gamePvpButton);
	        editableGroup.add(gameMonsterButton);
	        
	        gamePvpButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                gamemode = 0;
	            }
	        });
	        
	        gameMonsterButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                gamemode = 1;
	            }
	        });
	        
	        returnButton.setUI(new StyledButtonUI());
	        returnButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                settingPanel.setVisible(false);
	                remove(settingPanel);
	                buttonPanel.setVisible(true);
	                repaint();
	            }
	        });
	        returnButton.setPreferredSize(new Dimension(150,30));
	        
	        game.add(Box.createRigidArea(new Dimension(0, 100)));
	        game.add(gamePvpButton);
	        game.add(gameMonsterButton);
	        game.add(Box.createRigidArea(new Dimension(0, 10)));
	        settingPanel.add(game);
	        settingPanel.add(returnButton);

	        buttonPanel.setVisible(false);
	        
	        JComponent[] comp = {gamePvpButton, gameMonsterButton, game, settingPanel};
	        for(JComponent c : comp) {
	        	c.setOpaque(true);
	        	c.setBackground(new Color(0,0,0,0));
	        }
	        settingPanel.setVisible(true);
	        add(settingPanel);
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
	
	class StyledButtonUI extends BasicButtonUI {

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
	
	public static void setUIFont(javax.swing.plaf.FontUIResource f)
	{   
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while(keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if(value instanceof javax.swing.plaf.FontUIResource) UIManager.put(key, f);
	    }
	}

	
	public static void main(String[] args) {

		try
		{
		    setUIFont(new javax.swing.plaf.FontUIResource("Courier",Font.BOLD,12));
		}
		catch(Exception e){}
		new GuiMenu();
	}
}
