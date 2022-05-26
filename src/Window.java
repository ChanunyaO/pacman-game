import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Window extends JFrame implements Observer {

    private int size = 500;
    private World world;
    private Renderer renderer;
    private Gui gui;

    public Window() {
        super();
        addKeyListener(new Controller());
        setLayout(new BorderLayout());
        renderer = new Renderer();
        add(renderer, BorderLayout.CENTER);
        gui = new Gui();
        add(gui, BorderLayout.SOUTH);
        world = new World(25);
        world.addObserver(this);
        setSize(size, size+60);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Pacman");
        setResizable(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        renderer.repaint();
        gui.updateScore(world.getScore());
        if(world.isGameOver()) {
            gui.showGameOverLabel();
            gui.startButton.setEnabled(true);
        }
        if (!world.isNotWin()) {
            gui.showWinningLabel();
            gui.startButton.setEnabled(true);
        }
    }

    class Renderer extends JPanel {

        public Renderer() {
            setDoubleBuffered(true);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintGrids(g);
            paintDot(g);
            paintPacman(g);
            paintGhost(g);
            paintWall(g);
        }

        private void paintGrids(Graphics g) {
            // Background
            g.setColor(new Color(0,0,90));
            g.fillRect(0, 0, size, size);
        }

        private void paintDot(Graphics g){
            int perCell = size/world.getSize();
            g.setColor(new Color(255,255,0));
            for (Dot dot: world.getDots()){
                if(dot.getScore()==1){
                    g.fillOval(dot.getX()*perCell + perCell/2 - 3, dot.getY()*perCell + perCell/2 - 3, 6, 6);
                }else{
                    g.drawImage(new ImageIcon("img/cherry.png").getImage(), dot.getX() * perCell, dot.getY() * perCell, perCell, perCell, null, null);
                }
            }
        }

        private void paintPacman(Graphics g){
            int perCell = size/world.getSize();
            if(world.getTick()%2==0) {
                if (world.getPacman().getDirection() == Direction.RIGHT) {
                    g.drawImage(new ImageIcon("img/Pacman/pacmanRight.png").getImage(), world.getPacman().getX() * perCell, world.getPacman().getY() * perCell, perCell, perCell, null, null);
                } else if (world.getPacman().getDirection() == Direction.DOWN) {
                    g.drawImage(new ImageIcon("img/Pacman/pacmanDown.png").getImage(), world.getPacman().getX() * perCell, world.getPacman().getY() * perCell, perCell, perCell, null, null);
                } else if (world.getPacman().getDirection() == Direction.LEFT) {
                    g.drawImage(new ImageIcon("img/Pacman/pacmanLeft.png").getImage(), world.getPacman().getX() * perCell, world.getPacman().getY() * perCell, perCell, perCell, null, null);
                } else if (world.getPacman().getDirection() == Direction.UP) {
                    g.drawImage(new ImageIcon("img/Pacman/pacmanUp.png").getImage(), world.getPacman().getX() * perCell, world.getPacman().getY() * perCell, perCell, perCell, null, null);
                }
            }else{
                g.drawImage(new ImageIcon("img/Pacman/pacman.png").getImage(), world.getPacman().getX() * perCell, world.getPacman().getY() * perCell, perCell, perCell, null, null);
            }
        }

        private void paintGhost(Graphics g){
            int perCell = size/world.getSize();
            for(Ghost ghost:world.getGhosts()){
                if (ghost.isScatter()) {
                    g.setColor(Color.pink);
                }else{
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(ghost.getX() * perCell, ghost.getY() * perCell, perCell, perCell);
            }
        }

        private void paintWall(Graphics g){
            int perCell = size/world.getSize();
            for(Wall wall : world.getWalls()){
                g.setColor(new Color(0,0, 90));
                g.fillRect(wall.getX()*perCell, wall.getY()*perCell, perCell, perCell);
                g.setColor(Color.cyan);
                if((wall.getX()==0 && (wall.getY()==1 || wall.getY()==5 || wall.getY()==9 || wall.getY()==15 || wall.getY()==19 || wall.getY()==23))
                        || (wall.getX()==1 && ((2<=wall.getY() && wall.getY()<=4) || (6<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=18) || (20<=wall.getY() && wall.getY()<=22)))
                        || (wall.getX()==3 && (wall.getY()==3 || wall.getY()==7 || wall.getY()==17 || wall.getY()==21))
                        || (wall.getX()==4 && ((10<=wall.getY() && wall.getY()<=11) || (13<=wall.getY() && wall.getY()<=14)))
                        || ((wall.getX()==5 || wall.getX()==17) && (wall.getY()==0 || wall.getY()==2 || wall.getY()==4 || wall.getY()==6 || wall.getY()==8 || wall.getY()==16 || wall.getY()==18 || wall.getY()==20 || wall.getY()==22 || wall.getY()==24))
                        || ((wall.getX()==7 || wall.getX()==18) && (wall.getY()==10 || wall.getY()==12 || wall.getY()==14))
                        || (wall.getX()==9 && ((2<=wall.getY() && wall.getY()<=4) || (6<=wall.getY() && wall.getY()<=8) || (11<=wall.getY() && wall.getY()<=13) || (16<=wall.getY() && wall.getY()<=18) || (20<=wall.getY() && wall.getY()<=22)))
                        || (wall.getX()==11 && ((1<=wall.getY() && wall.getY()<=2) || (4<=wall.getY() && wall.getY()<=5) || (7<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=17) || (19<=wall.getY() && wall.getY()<=20) || (22<=wall.getY() && wall.getY()<=23) || wall.getY()==10 || wall.getY()==14))
                        || (wall.getX()==13 && ((1<=wall.getY() && wall.getY()<=2) || (4<=wall.getY() && wall.getY()<=5) || (7<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=17) || (19<=wall.getY() && wall.getY()<=20) || (22<=wall.getY() && wall.getY()<=23)))
                        || (wall.getX()==15 && (wall.getY()==3 || wall.getY()==7 || wall.getY()==17 || wall.getY()==21 || (10<=wall.getY() && wall.getY()<=14)))
                        || (wall.getX()==21 && ((2<=wall.getY() && wall.getY()<=4) || (6<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=18) || (20<=wall.getY() && wall.getY()<=22)))
                ){
                    g.fillRect(wall.getX()*perCell+perCell-3, wall.getY()*perCell, 3, perCell);
                }
                if((wall.getX()==3 && ((2<=wall.getY() && wall.getY()<=4) || (6<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=18) || (20<=wall.getY() && wall.getY()<=22)))
                        || ((wall.getX()==6 || wall.getX()==17) && (wall.getY()==10 || wall.getY()==12 || wall.getY()==14))
                        || ((wall.getX()==7 || wall.getX()==19) && (wall.getY()==0 || wall.getY()==2 || wall.getY()==4 || wall.getY()==6 || wall.getY()==8 || wall.getY()==16 || wall.getY()==18 || wall.getY()==20 || wall.getY()==22 || wall.getY()==24))
                        || (wall.getX()==9 && (wall.getY()==3 || wall.getY()==7 || wall.getY()==17 || wall.getY()==21 || (10<=wall.getY() && wall.getY()<=14)))
                        || (wall.getX()==11 && ((1<=wall.getY() && wall.getY()<=2) || (4<=wall.getY() && wall.getY()<=5) || (7<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=17) || (19<=wall.getY() && wall.getY()<=20) || (22<=wall.getY() && wall.getY()<=23)))
                        || (wall.getX()==13 && ((1<=wall.getY() && wall.getY()<=2) || (4<=wall.getY() && wall.getY()<=5) || (7<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=17) || (19<=wall.getY() && wall.getY()<=20) || (22<=wall.getY() && wall.getY()<=23) || wall.getY()==10 || wall.getY()==14))
                        || (wall.getX()==15 && ((2<=wall.getY() && wall.getY()<=4) || (6<=wall.getY() && wall.getY()<=8) || (11<=wall.getY() && wall.getY()<=13) || (16<=wall.getY() && wall.getY()<=18) || (20<=wall.getY() && wall.getY()<=22)))
                        || (wall.getX()==21 && (wall.getY()==3 || wall.getY()==7 || wall.getY()==17 || wall.getY()==21))
                        || (wall.getX()==20 && ((10<=wall.getY() && wall.getY()<=11) || (13<=wall.getY() && wall.getY()<=14)))
                        || (wall.getX()==23 && ((2<=wall.getY() && wall.getY()<=4) || (6<=wall.getY() && wall.getY()<=8) || (16<=wall.getY() && wall.getY()<=18) || (20<=wall.getY() && wall.getY()<=22)))
                        || (wall.getX()==24 && (wall.getY()==1 || wall.getY()==5 || wall.getY()==9 || wall.getY()==15 || wall.getY()==19 || wall.getY()==23))
                ){
                    g.fillRect(wall.getX()*perCell, wall.getY()*perCell, 3, perCell);
                }
                if((wall.getY()==0 && ((1<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=10) || wall.getX()==12 || (14<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=23)))
                        || ((wall.getY()==2 || wall.getY()==20) && ((4<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=8) || wall.getX()==11 || wall.getX()==13 || (16<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=20)))
                        || ((wall.getY()==4 || wall.getY()==18 || wall.getY()==22) && ((3<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=9) || (15<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=21) || wall.getX()==23 || wall.getX()==1))
                        || ((wall.getY()==5 || wall.getY()==17) && (wall.getX()==11 || wall.getX()==13))
                        || ((wall.getY()==6 || wall.getY()==16) && ((4<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=8) || (16<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=20)))
                        || (wall.getY()==8 && ((3<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=9) || (15<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=21) || wall.getX()==23 || wall.getX()==11 || wall.getX()==1 || wall.getX()==13))
                        || (wall.getY()==9 && ((6<=wall.getX() && wall.getX()<=7) || (10<=wall.getX() && wall.getX()<=11) || (13<=wall.getX() && wall.getX()<=14) || (17<=wall.getX() && wall.getX()<=18)))
                        || ((wall.getY()==10 || wall.getY()==12) && ((6<=wall.getX() && wall.getX()<=7) || (17<=wall.getX() && wall.getX()<=18) || (10<=wall.getX() && wall.getX()<=14)))
                        || (wall.getY()==11 && ((0<=wall.getX() && wall.getX()<=4) || (20<=wall.getX() && wall.getX()<=24) || wall.getX()==9 || wall.getX()==15))
                        || (wall.getY()==14 && ((6<=wall.getX() && wall.getX()<=7) || (17<=wall.getX() && wall.getX()<=18) || (1<=wall.getX() && wall.getX()<=4) || (20<=wall.getX() && wall.getX()<=23) || (9<=wall.getX() && wall.getX()<=15)))
                ){
                    g.fillRect(wall.getX()*perCell, wall.getY()*perCell+perCell-3, perCell, 3);
                }
                if((wall.getY()==24 && ((1<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=10) || wall.getX()==12 || (14<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=23)))
                        || ((wall.getY()==22 || wall.getY()==4) && ((4<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=8) || wall.getX()==11 || wall.getX()==13 || (16<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=20)))
                        || ((wall.getY()==20 || wall.getY()==6 || wall.getY()==2) && ((3<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=9) || (15<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=21) || wall.getX()==23 || wall.getX()==1))
                        || ((wall.getY()==19 || wall.getY()==7) && (wall.getX()==11 || wall.getX()==13))
                        || ((wall.getY()==18 || wall.getY()==8) && ((4<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=8) || (16<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=20)))
                        || (wall.getY()==16 && ((3<=wall.getX() && wall.getX()<=5) || (7<=wall.getX() && wall.getX()<=9) || (15<=wall.getX() && wall.getX()<=17) || (19<=wall.getX() && wall.getX()<=21) || wall.getX()==23 || wall.getX()==11 || wall.getX()==1 || wall.getX()==13))
                        || (wall.getY()==15 && ((6<=wall.getX() && wall.getX()<=7) || (10<=wall.getX() && wall.getX()<=11) || (13<=wall.getX() && wall.getX()<=14) || (17<=wall.getX() && wall.getX()<=18)))
                        || (wall.getY()==14 && ((6<=wall.getX() && wall.getX()<=7) || (17<=wall.getX() && wall.getX()<=18) || (10<=wall.getX() && wall.getX()<=14)))
                        || (wall.getY()==12 && ((6<=wall.getX() && wall.getX()<=7) || (17<=wall.getX() && wall.getX()<=18)))
                        || (wall.getY()==13 && ((0<=wall.getX() && wall.getX()<=4) || (20<=wall.getX() && wall.getX()<=24) || wall.getX()==9 || wall.getX()==15))
                        || (wall.getY()==10 && ((6<=wall.getX() && wall.getX()<=7) || (17<=wall.getX() && wall.getX()<=18) || (1<=wall.getX() && wall.getX()<=4) || (20<=wall.getX() && wall.getX()<=23) || (9<=wall.getX() && wall.getX()<=15)))
                ){
                    g.fillRect(wall.getX()*perCell, wall.getY()*perCell, perCell, 3);
                }
                if((wall.getY()==0 && (wall.getX()==11 || wall.getX()==13 || wall.getX()==24))
                        || ((wall.getY()==2 || wall.getY()==6 || wall.getY()==16 || wall.getY()==20) && (wall.getX()==9 || wall.getX()==21))
                        || ((wall.getY()==4 || wall.getY()==8 || wall.getY()==14 || wall.getY()==18 || wall.getY()==22) && wall.getX()==24)
                        || ((wall.getY()==10) && wall.getX()==15)
                ){
                    g.fillRect(wall.getX()*perCell, wall.getY()*perCell+(perCell-3), 3, 3);
                }
                if((wall.getY()==0 && (wall.getX()==11 || wall.getX()==13 || wall.getX()==0))
                        || ((wall.getY()==2 || wall.getY()==6 || wall.getY()==16 || wall.getY()==20) && (wall.getX()==3 || wall.getX()==15))
                        || ((wall.getY()==4 || wall.getY()==8 || wall.getY()==14 || wall.getY()==18 || wall.getY()==22) && wall.getX()==0)
                        || (wall.getY()==10 && wall.getX()==9)
                ){
                    g.fillRect(wall.getX()*perCell+(perCell-3), wall.getY()*perCell+(perCell-3), 3, 3);
                }
                if(((wall.getY()==2 || wall.getY()==6 || wall.getY()==10 || wall.getY()==16 || wall.getY()==20 || wall.getY()==24) && wall.getX()==0)
                        || ((wall.getX()==3 || wall.getX()==15) && (wall.getY()==4 || wall.getY()==8 || wall.getY()==18 || wall.getY()==22))
                        || (wall.getX()==9 && wall.getY()==14)
                        || ((wall.getX()==11 || wall.getX()==13) && wall.getY()==24)
                ){
                    g.fillRect(wall.getX()*perCell+(perCell-3), wall.getY()*perCell, 3, 3);
                }
                if(((wall.getX()==9 || wall.getX()==21) && (wall.getY()==4 || wall.getY()==8 || wall.getY()==18 || wall.getY()==22))
                        || ((wall.getX()==11 || wall.getX()==13) && wall.getY()==24)
                        || ((wall.getY()==2 || wall.getY()==6 || wall.getY()==10 || wall.getY()==16 || wall.getY()==20 || wall.getY()==24) && wall.getX()==24)
                        || (wall.getX()==15 && wall.getY()==14)
                ){
                    g.fillRect(wall.getX()*perCell, wall.getY()*perCell, 3, 3);
                }
            }
        }
    }

    class Gui extends JPanel {

        private JLabel scoreLabel;
        private JButton  startButton;
        private JLabel gameOverLabel;
        private JLabel winningLabel;

        public Gui() {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            scoreLabel = new JLabel("Score: 0  ");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            add(scoreLabel, c);
            scoreLabel.setVisible(false);
            startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    world.start();
                    scoreLabel.setVisible(true);
                    startButton.setEnabled(false);
                    gui.gameOverLabel.setVisible(false);
                    gui.winningLabel.setVisible(false);
                    Window.this.requestFocus();
                }
            });
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            add(startButton, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            gameOverLabel = new JLabel("GAME OVER");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 2;
            c.gridy = 0;
            gameOverLabel.setForeground(Color.red);
            gameOverLabel.setVisible(false);
            add(gameOverLabel, c);
            winningLabel = new JLabel("GREAT JOB");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 2;
            c.gridy = 0;
            winningLabel.setForeground(Color.GREEN);
            winningLabel.setVisible(false);
            add(winningLabel, c);
        }

        public void updateScore(int score) {
            scoreLabel.setText("Score: " + score + "  ");
        }

        public void showGameOverLabel() {
            gameOverLabel.setVisible(true);
        }

        public void showWinningLabel() {
            winningLabel.setVisible(true);
        }
    }

    class Controller extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                Command c = new CommandTurnNorth(world.getPacman(), world.getTick());
                c.execute();
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                Command c = new CommandTurnSouth(world.getPacman(), world.getTick());
                c.execute();
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                Command c = new CommandTurnWest(world.getPacman(), world.getTick());
                c.execute();
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                Command c = new CommandTurnEast(world.getPacman(), world.getTick());
                c.execute();
            }
        }
    }

    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
    }

}
