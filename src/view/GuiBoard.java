package view;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuiBoard extends JPanel{
    private final Game game;
    private final Board board;
    private final Gui gui;
    private final ArrayList<Player> players;
    private final ArrayList<Monster> monsters;
    private BufferedImage block;
    private BufferedImage breakableBlock;
    private BufferedImage unbreakableBlock;
    public static final int objectSizex = 21;
    public static final int objectSizey = 22;
    private JButton resumeButton;
    private JButton restartButton;
    private JButton quitButton;
    private final JPanel buttonPanel = new JPanel();
    protected boolean close = false;
    protected boolean endButtonsPrinted = false;
    private final HashMap<Bonus.Type, BufferedImage> bonusMap=new LinkedHashMap<>();

    private final LinkedList<BufferedImage> explosionMidList=new LinkedList<>();
    private final LinkedList<BufferedImage> explosionWidthList=new LinkedList<>();
    private final LinkedList<BufferedImage> explosionHeightList=new LinkedList<>();
    private final LinkedList<BufferedImage> explosionLeftList=new LinkedList<>();
    private final LinkedList<BufferedImage> explosionRightList=new LinkedList<>();
    private final LinkedList<BufferedImage> explosionTopList=new LinkedList<>();
    private final LinkedList<BufferedImage> explosionDownList=new LinkedList<>();

    private BufferedImage bombImage;
    private final LinkedList<BufferedImage[][]> playerImagesList=new LinkedList<>();

    private final LinkedList<BufferedImage> kondoriaLeftList =new LinkedList<>();
    private final LinkedList<BufferedImage> kondoriaRightList =new LinkedList<>();
    private final LinkedList<BufferedImage> kondoriaDeadList =new LinkedList<>();
    
    private final LinkedList<BufferedImage> minvoLeftList =new LinkedList<>();
    private final LinkedList<BufferedImage> minvoRightList =new LinkedList<>();
    private final LinkedList<BufferedImage> minvoDeadList =new LinkedList<>();

    /**
     * Constructor for Gui class
     * @param game Game object
     * @param gui Gui object
     */
    public GuiBoard(Game game, Gui gui){
        this.game=game;
        this.gui = gui;
        this.board=game.getBoard();
        this.players=board.getPlayerList();
        this.monsters=board.getMonsterList();

        // set size of the JPanel
        GameObject.setSizeY(this.getHeight());
        GameObject.setSizeX(this.getWidth());

        createPauseButtons();

        loadImages();
    }

    /**
     * Function that calls all loadImages method
     */
    private void loadImages(){
        try{
            loadExplosionImages();
            loadBonusImages();
            loadPlayerImages();
            loadMonsterImages();
            block=ImageIO.read(new File("resources/block.png"));
            unbreakableBlock=ImageIO.read(new File("resources/block_unbreakable.png"));
            breakableBlock=ImageIO.read(new File("resources/block_breakable.png"));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Load all explosions images
     * @throws IOException IO exception when loading images
     */
    private void loadExplosionImages() throws IOException{
        bombImage = ImageIO.read(new File("resources/bomb.png"));

        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_0.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_1.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_2.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_3.png")));
        explosionMidList.add(ImageIO.read(new File("resources/explosion/explosion_mid_4.png")));

        explosionHeightList.add(ImageIO.read(new File("resources/explosion/explosion_hauteur_1.png")));
        explosionHeightList.add(ImageIO.read(new File("resources/explosion/explosion_hauteur_2.png")));
        explosionHeightList.add(ImageIO.read(new File("resources/explosion/explosion_hauteur_3.png")));
        explosionHeightList.add(ImageIO.read(new File("resources/explosion/explosion_hauteur_4.png")));

        explosionWidthList.add(ImageIO.read(new File("resources/explosion/explosion_largeur_1.png")));
        explosionWidthList.add(ImageIO.read(new File("resources/explosion/explosion_largeur_2.png")));
        explosionWidthList.add(ImageIO.read(new File("resources/explosion/explosion_largeur_3.png")));
        explosionWidthList.add(ImageIO.read(new File("resources/explosion/explosion_largeur_4.png")));

        explosionLeftList.add(ImageIO.read(new File("resources/explosion/explosion_gauche_1.png")));
        explosionLeftList.add(ImageIO.read(new File("resources/explosion/explosion_gauche_2.png")));
        explosionLeftList.add(ImageIO.read(new File("resources/explosion/explosion_gauche_3.png")));
        explosionLeftList.add(ImageIO.read(new File("resources/explosion/explosion_gauche_4.png")));

        explosionRightList.add(ImageIO.read(new File("resources/explosion/explosion_droite_1.png")));
        explosionRightList.add(ImageIO.read(new File("resources/explosion/explosion_droite_2.png")));
        explosionRightList.add(ImageIO.read(new File("resources/explosion/explosion_droite_3.png")));
        explosionRightList.add(ImageIO.read(new File("resources/explosion/explosion_droite_4.png")));

        explosionTopList.add(ImageIO.read(new File("resources/explosion/explosion_top_1.png")));
        explosionTopList.add(ImageIO.read(new File("resources/explosion/explosion_top_2.png")));
        explosionTopList.add(ImageIO.read(new File("resources/explosion/explosion_top_3.png")));
        explosionTopList.add(ImageIO.read(new File("resources/explosion/explosion_top_4.png")));

        explosionDownList.add(ImageIO.read(new File("resources/explosion/explosion_bas_1.png")));
        explosionDownList.add(ImageIO.read(new File("resources/explosion/explosion_bas_2.png")));
        explosionDownList.add(ImageIO.read(new File("resources/explosion/explosion_bas_3.png")));
        explosionDownList.add(ImageIO.read(new File("resources/explosion/explosion_bas_4.png")));

    }

    /**
     * Load all bonus images
     * @throws IOException IO exception when loading images
     */
    private void loadBonusImages() throws IOException{
        bonusMap.put(Bonus.Type.Bomb,ImageIO.read(new File("resources/bonus_bomb.png")));
        bonusMap.put(Bonus.Type.Firemax,ImageIO.read(new File("resources/bonus_firemax.png")));
        bonusMap.put(Bonus.Type.Fireup,ImageIO.read(new File("resources/bonus_fireup.png")));
        bonusMap.put(Bonus.Type.Kick,ImageIO.read(new File("resources/bonus_kick.png")));
        bonusMap.put(Bonus.Type.Pierce,ImageIO.read(new File("resources/bonus_pierce.png")));
        bonusMap.put(Bonus.Type.Speed,ImageIO.read(new File("resources/bonus_speed.png")));
    }

    /**
     * Load all players images
     * @throws IOException IO exception when loading images
     */
    private void loadPlayerImages() throws IOException{
        for (int i = 0; i <4; i++) {
            BufferedImage image = ImageIO.read(new File("resources/playersheet_"+ i+".png"));
            int rows = image.getHeight() / objectSizey;
            int cols = image.getWidth() / objectSizex;

            BufferedImage [][] walkFrames = new BufferedImage[rows][cols];
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    walkFrames[row][col] = image.getSubimage(col * objectSizex, row * objectSizey, objectSizex, objectSizey);
                }
            }
            playerImagesList.add(walkFrames);
        }
    }

    /**
     * Load all monsters images
     * @throws IOException IO exception when loading images
     */
    private void loadMonsterImages() throws IOException{
        kondoriaLeftList.add(ImageIO.read(new File("resources/monsters/kondoria_left1.png")));
        kondoriaLeftList.add(ImageIO.read(new File("resources/monsters/kondoria_left2.png")));
        kondoriaLeftList.add(ImageIO.read(new File("resources/monsters/kondoria_left3.png")));

        kondoriaRightList.add(ImageIO.read(new File("resources/monsters/kondoria_right1.png")));
        kondoriaRightList.add(ImageIO.read(new File("resources/monsters/kondoria_right2.png")));
        kondoriaRightList.add(ImageIO.read(new File("resources/monsters/kondoria_right3.png")));

        kondoriaDeadList.add(ImageIO.read(new File("resources/monsters/kondoria_dead.png")));
        kondoriaDeadList.add(ImageIO.read(new File("resources/monsters/mob_dead1.png")));
        kondoriaDeadList.add(ImageIO.read(new File("resources/monsters/mob_dead2.png")));
        kondoriaDeadList.add(ImageIO.read(new File("resources/monsters/mob_dead3.png")));

        minvoLeftList.add(ImageIO.read(new File("resources/monsters/minvo_left1.png")));
        minvoLeftList.add(ImageIO.read(new File("resources/monsters/minvo_left2.png")));
        minvoLeftList.add(ImageIO.read(new File("resources/monsters/minvo_left3.png")));

        minvoRightList.add(ImageIO.read(new File("resources/monsters/minvo_right1.png")));
        minvoRightList.add(ImageIO.read(new File("resources/monsters/minvo_right2.png")));
        minvoRightList.add(ImageIO.read(new File("resources/monsters/minvo_right3.png")));

        minvoDeadList.add(ImageIO.read(new File("resources/monsters/minvo_dead.png")));
        minvoDeadList.add(ImageIO.read(new File("resources/monsters/mob_dead1.png")));
        minvoDeadList.add(ImageIO.read(new File("resources/monsters/mob_dead2.png")));
        minvoDeadList.add(ImageIO.read(new File("resources/monsters/mob_dead3.png")));
    }

    /**
     * Java paintComponent that calls every paint function
     * @param g paintComponent Graphics object
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D) g;
        paintBoard(g2);
        paintPlayers(g2);
        if (board.getMonsterMode()){
            try {
                paintMonsters(g2);
            }catch (Exception ignored) {}
        }
        if(game.getPaused()){
            paintFilter(g2);
        }
        if(game.hasEnded()){
            paintFilter(g2);
            if(!endButtonsPrinted) {
                endButtonsPrinted = true;
                createEndButtons();
            }
        }

    }

    /**
     * Paint the board: walls,bonus,grass images
     * @param g2 paintComponent Graphics object
     */
    private void paintBoard(Graphics2D g2){
        Case[][] cases=board.getCases();
        int height=this.getHeight()/cases.length;
        int width=this.getWidth()/cases[0].length;
        ArrayList<Point> bombs= new ArrayList<>();
        for(int x = 0; x < cases.length; x++){
            for(int y = 0; y < cases[x].length; y++){
                if(cases[x][y].getWall() == null) {
                    g2.drawImage(block, y * width, x * height, width, height, null); // grass paint
                    if(cases[x][y].getBonus() != null) {
                        g2.drawImage(bonusMap.get(cases[x][y].getBonus().getType()), y * width, x * height, width, height, null); // bonus paint
                    }
                    if(cases[x][y].getBomb()!=null) bombs.add(new Point(x, y)); // adding bomb(s) into list
                }
                else if(cases[x][y].getWall().isBreakable()){
                    g2.drawImage(breakableBlock,y * width,x * height,width,height,null); // breakable blocks paint
                }
                else{
                    g2.drawImage(unbreakableBlock,y * width,x * height,width,height,null);// unbreakable blocks paint
                }

            }
        }
        for(Point p : bombs){ // bombs paint
            paintBomb(g2, p.x, p.y, width, height);
        }
    }

    /**
     * Paint bomb and explosion images based on its state.
     * @param g2 graphics component item
     * @param x position x
     * @param y position y
     * @param width of the case
     * @param height of the case
     */
    private void paintBomb(Graphics2D g2, int x, int y, int width, int height){
        Bomb bomb = board.getCases()[x][y].getBomb();
        int spriteIndex = bomb.getSpriteIndex();
        if(spriteIndex == -1){
            // the bomb is displayed
            g2.drawImage(bombImage, width * y, height * x, width, height, null); // on affiche l'image de l'état de la bombe
            return;
        }
        if(spriteIndex == 0){ // the center explosion of the bomb is displayed
            g2.drawImage(explosionMidList.get(0), width * y, height * x, width, height, null);
            return;
        }
        // the center explosion of the bomb is displayed
        g2.drawImage(getBombImageState("mid",spriteIndex), width * y, height * x, width, height, null);

        //left
        int i = y - 1;
        while(i > bomb.getStopLeft() + 1 && board.getCases()[x][i].getWall() == null) {
            // the image of the state of the arms of the explosion is displayed
            g2.drawImage(getBombImageState("width", spriteIndex), width * i, height * x, width, height, null);
            i -= 1;
        }
        if(board.getCases()[x][i].getWall() == null || board.getCases()[x][i].getWall().isBreakable() ) {
            // the image of the state of the tip of the explosion arm is displayed
            g2.drawImage(getBombImageState("left", spriteIndex), width * i, height * x, width, height, null);
        }

        //top
         i = x - 1;
        while(i > bomb.getStopTop() + 1 && board.getCases()[i][y].getWall() == null) {
            // the image of the state of the arms of the explosion is displayed
            g2.drawImage(getBombImageState("height", spriteIndex), width * y, height * i, width, height, null);
            i -= 1;
        }
        if(board.getCases()[i][y].getWall() == null || board.getCases()[i][y].getWall().isBreakable()) {
            // the image of the state of the tip of the explosion arm is displayed
            g2.drawImage(getBombImageState("top", spriteIndex), width * y, height * i, width, height, null);
        }

        //right
        i = y + 1;
        while(i < bomb.getStopRight() - 1 && board.getCases()[x][i].getWall() == null) {
            // the image of the state of the arms of the explosion is displayed
            g2.drawImage(getBombImageState("width", spriteIndex), width * i, height * (x), width, height, null);
            i += 1;
        }
        if(board.getCases()[x][i].getWall() == null || board.getCases()[x][i].getWall().isBreakable()) {
            // the image of the state of the tip of the explosion arm is displayed
            g2.drawImage(getBombImageState("right", spriteIndex), width * i, height * (x), width, height, null);
        }

        //down
        i = x + 1;
        while(i < bomb.getStopDown() - 1 && board.getCases()[i][y].getWall() == null) {
            // the image of the state of the arms of the explosion is displayed
            g2.drawImage(getBombImageState("height", spriteIndex), width * y, height * i, width, height, null);
            i += 1;
        }
        if(board.getCases()[i][y].getWall() == null || board.getCases()[i][y].getWall().isBreakable()) {
            // the image of the state of the tip of the explosion arm is displayed

            g2.drawImage(getBombImageState("down", spriteIndex), width * y, height * i, width, height, null);
        }
    }

    /**
     * Get the image of the bomb depending on the direction and the state
     * @param explosion Direction of the explosion
     * @param spriteIndex State of bomb exposion
     * @return Image of the bomb
     */
    private BufferedImage getBombImageState(String explosion, int spriteIndex){
        switch (explosion) {
            case "mid" : switch (spriteIndex) {
                case 1 : return explosionMidList.get(1);
                case 2 : return explosionMidList.get(2);
                case 3 : return explosionMidList.get(3);
                case 4 : return explosionMidList.get(4);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            case "height" : switch (spriteIndex) {
                case 1 : return explosionHeightList.get(0);
                case 2 : return explosionHeightList.get(1);
                case 3 : return explosionHeightList.get(2);
                case 4 : return explosionHeightList.get(3);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            case "width" : switch (spriteIndex) {
                case 1 : return explosionWidthList.get(0);
                case 2 : return explosionWidthList.get(1);
                case 3 : return explosionWidthList.get(2);
                case 4 : return explosionWidthList.get(3);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            case "down" : switch (spriteIndex) {
                case 1 : return explosionDownList.get(0);
                case 2 : return explosionDownList.get(1);
                case 3 : return explosionDownList.get(2);
                case 4 : return explosionDownList.get(3);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            case "top" : switch (spriteIndex) {
                case 1 : return explosionTopList.get(0);
                case 2 : return explosionTopList.get(1);
                case 3 : return explosionTopList.get(2);
                case 4 : return explosionTopList.get(3);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            case "right" : switch (spriteIndex) {
                case 1 : return explosionRightList.get(0);
                case 2 : return explosionRightList.get(1);
                case 3 : return explosionRightList.get(2);
                case 4 : return explosionRightList.get(3);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            case "left" : switch (spriteIndex) {
                case 1 : return explosionLeftList.get(0);
                case 2 : return explosionLeftList.get(1);
                case 3 : return explosionLeftList.get(2);
                case 4 : return explosionLeftList.get(3);
                default : throw new IllegalStateException("Unexpected value: " + spriteIndex);
            }
            default : throw new IllegalStateException("Unexpected value: " + explosion);
        }
    }

    /**
     * Paint the players images
     * @param g2 paintComponent Graphics object
     */
    private void paintPlayers(Graphics2D g2){
        for (Player player : players) {
            float x = player.getPositionX() - 0.4F;
            float y = player.getPositionY() - 0.4F;
            int x_height = this.getHeight() / board.getCases().length;
            int y_width = this.getWidth() / board.getCases()[0].length;
            int direction = player.getDirection();
            int spriteIndex = player.getSpriteIndex();
            if (!player.isSet()) continue;
            if (player.getId() == 0){
                g2.drawImage(playerImagesList.get(0)[direction][spriteIndex], (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
            } else if (player.getId() == 1){
                g2.drawImage(playerImagesList.get(1)[direction][spriteIndex], (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
            } else if (player.getId() == 2){
                g2.drawImage(playerImagesList.get(2)[direction][spriteIndex], (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
            } else if (player.getId() == 3){
                g2.drawImage(playerImagesList.get(3)[direction][spriteIndex], (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
            }
        }
    }

    /**
     * Paint monsters images.
     * @param g2 paintComponent Graphics object
     * @throws IOException
     */
    private void paintMonsters(Graphics2D g2) throws IOException {
        for (Monster monster: monsters) {
            float x = monster.getPositionX() - 0.4F;
            float y = monster.getPositionY() - 0.4F;
            int x_height = this.getHeight() / board.getCases().length;
            int y_width = this.getWidth() / board.getCases()[0].length;
            int direction = monster.getDirection();
            int spriteIndex = monster.getSpriteIndex();
            if (!monster.isSet()) continue;
            if (monster.getType() == 0) {
                if (!monster.isAlive()) {
                    g2.drawImage(minvoDeadList.get(spriteIndex), (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
                } else {
                    if (direction == 2 || direction == 0) {
                        g2.drawImage(minvoLeftList.get(spriteIndex), (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
                    }
                    if (direction == 3 || direction == 1) {
                        g2.drawImage(minvoRightList.get(spriteIndex), (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
                    }
                }
            } else {
                if (!monster.isAlive()) {
                    g2.drawImage(kondoriaDeadList.get(spriteIndex), (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
                } else {
                    if (direction == 2 || direction == 0) {
                        g2.drawImage(kondoriaLeftList.get(spriteIndex), (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
                    }
                    if (direction == 3 || direction == 1) {
                        g2.drawImage(kondoriaRightList.get(spriteIndex), (int) (y * y_width), (int) (x * x_height), y_width, x_height, null);
                    }
                }
            }
        }
    }

    /**
     * Paint a transparent filter when the game is paused or ended
     * @param g paintComponent Graphics object
     */
    private void paintFilter(Graphics2D g){
        int alpha=157;
        Color blackFilter=new Color(0, 0, 0,alpha);
        g.setColor(blackFilter);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }

    /**
     * Create and add the pause buttons to the JPanel
     */
    private void createPauseButtons(){
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(0,0,0,0));
        resumeButton=new JButton("Resume");
        quitButton=new JButton("Quit");
        resumeButton.addActionListener(event -> {
            removePauseButtons();
            game.resume();
            requestFocusAncestor();
        });
        quitButton.addActionListener(event -> System.exit(0));
        JButton[] buttons = {resumeButton, quitButton};
        for(JButton b : buttons) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setPreferredSize(new Dimension(200,60));
            b.setUI(new GuiMenu.StyledButtonUI());
        }
    }

    /**
     * Create and add the end buttons to the JPanel
     */
    protected void createEndButtons(){
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(0,0,0,0));
        restartButton=new JButton("Restart");
        quitButton=new JButton("Quit");
        restartButton.addActionListener(event -> {
            close = true;
            gui.restartGame();
        });
        quitButton.addActionListener(event -> System.exit(0));
        JButton[] buttons = {restartButton, quitButton};
        for(JButton b : buttons) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setPreferredSize(new Dimension(200,60));
            b.setUI(new GuiMenu.StyledButtonUI());
        }
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, this.getHeight()/3)));
        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, this.getHeight()/10)));
        buttonPanel.add(quitButton);
        this.add(buttonPanel,CENTER_ALIGNMENT);
        repaint();
        revalidate();
    }

    /**
     * Show the pause buttons on the JPanel
     */
    public void showPauseButtons(){
        buttonPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, this.getHeight()/3)));
        buttonPanel.add(resumeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, this.getHeight()/10)));
        buttonPanel.add(quitButton);
        this.add(buttonPanel,CENTER_ALIGNMENT);
        repaint();
        revalidate();
    }

    /**
     * Hide the pause buttons of the JPanel
     */
    public void removePauseButtons(){
        buttonPanel.removeAll();
        this.remove(buttonPanel);
        repaint();
        revalidate();
    }

    /**
     * Request the focus on the parent JFrame for keyboard inputs
     */
    public void requestFocusAncestor(){
        SwingUtilities.getWindowAncestor(this).requestFocusInWindow();
    }

}
