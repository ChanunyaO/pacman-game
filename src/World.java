import java.util.*;

public class World extends Observable {
    private int size;
    private static int tick;
    private Pacman pacman;
    private List<Ghost> ghosts = new ArrayList<>();
    private List<Dot> dots = new ArrayList<>();
    private boolean notOver;
    private Thread thread;
    private int score;
    private long delayed = 250;
    private List<Wall> walls = new ArrayList<>();
    private int time = -1;
    private boolean notWin;

    public World(int size){
        this.size = size;
        tick = 0;
        score = 0;
        pacman = new Pacman(12,15, Direction.RIGHT);
        setGhosts();
        setWalls();
        setDots();
    }

    public void start(){
        pacman.reset();
        pacman.setPosition(12, 15, Direction.RIGHT);
        tick = 0;
        score = 0;
        notOver = true;
        notWin = true;
        setGhosts();
        setDots();
        setWalls();
        thread = new Thread(){
            @Override
            public void run() {
                while (notOver&&notWin){
                    tick++;
                    pacman.move(nearWall(pacman));
                    for(Ghost ghost: ghosts){
                        ghost.move(nearWall(ghost));
                    }
                    eatGhost();
                    timer();
                    eatDot();
                    setChanged();
                    notifyObservers();
                    waitFor(delayed);
                    if(dots.size()==0){
                        notWin = false;
                    }
                }
            }
        };
        thread.start();
    }

    private void timer(){
        if(time>=20) {
            for (Ghost ghost : ghosts) {
                ghost.setScatter(false);
            }
            time=-1;
        }else if(time>=0) {
            time++;
        }
    }

    private void eatGhost(){
        for(Ghost ghost:ghosts) {
            if (pacman.eat(ghost) && ghost.isScatter()){
                score+=ghost.getScore();
                ghost.setPosition(12,12, Direction.UP);
                ghost.setScatter(false);
            } else if(ghost.canEat(pacman)){
                notOver = false;
            }
        }
    }

    private List nearWall(WObject obj){
        List<String> wallPosition = new ArrayList<>();
        wallPosition.add("");
        for(Wall wall : walls){
            if(obj.getY() == wall.getY() && obj.getX() == wall.getX()-1){
                wallPosition.add("right");
            }
            if(obj.getY() == wall.getY() && obj.getX() == wall.getX()+1){
                wallPosition.add("left");
            }
            if(obj.getY() == wall.getY() - 1 && obj.getX() == wall.getX()){
                wallPosition.add("up");
            }
            if(obj.getY() == wall.getY() + 1 && obj.getX() == wall.getX()){
                wallPosition.add("down");
            }
        }
        return wallPosition;
    }

    private void eatDot() {
        for(int i=0; i<dots.size(); i++){
            if(pacman.eat(dots.get(i))){
                score += dots.get(i).getScore();
                if(dots.get(i).getScore()>1){
                    time=0;
                    for (Ghost ghost : ghosts) {
                        ghost.setScatter(true);
                    }
                }
                dots.remove(i);
                break;
            }
        }
    }

    private void waitFor(long delayed) {
        try {
            Thread.sleep(delayed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setGhosts() {
        ghosts.clear();
        for(int i=0; i<8; i++){
            ghosts.add(new Ghost(12, 12, Direction.UP));
        }
    }

    private void setWalls(){
        walls.clear();
        for(int i=0; i<25; i++){
            if(i==0 || i==24){
                for(int j=0; j<25; j++){
                    if(j!=6 && j!=18) {
                        walls.add(new Wall(j, i));
                    }
                }
            }else if(i==1 || i==23 || i==5 || i==19 || i==9 || i==15){
                walls.add(new Wall(0, i));
                walls.add(new Wall(24, i));
                if(i!=9 && i!=15) {
                    walls.add(new Wall(11, i));
                    walls.add(new Wall(13, i));
                }
            }else if(i==2 || i==22 || i==4 || i==20 || i==6 || i==18 || i==8 || i==16){
                for(int j=0; j<25; j++){
                    if(j<2 || (j>2 && j<6) || (j>6 && j<10) || (j>14 && j<18) || (j>18 && j<22) || j>22) {
                        walls.add(new Wall(j, i));
                    }
                    if(i!=6 && i!=18){
                        walls.add(new Wall(11, i));
                        walls.add(new Wall(13, i));
                    }
                }
            }else if(i==3 || i==21 || i==7 || i==17){
                for(int j=0; j<25; j++){
                    if(j==0 || j==1 || j==3 || j==9 || j==15 || j==21 || j==23 || j==24) {
                        walls.add(new Wall(j, i));
                    }
                    if(i!=3 && i!=21){
                        walls.add(new Wall(11, i));
                        walls.add(new Wall(13, i));
                    }
                }
            }else if(i==10 || i==14){
                for(int j=0; j<25; j++){
                    if(j!=5 && j!=8 && j!=16 && j!=19 && j!=12) {
                        walls.add(new Wall(j, i));
                    }
                }
            }else if(i==11 || i==13){
                for(int j=0; j<25; j++){
                    if(j<5 || j==9 || j==15 || j>19) {
                        walls.add(new Wall(j, i));
                    }
                }
            }else{
                for(int j=0; j<25; j++){
                    if(j==6 || j==7 || j==17 || j==18) {
                        walls.add(new Wall(j, i));
                    }
                }
            }
        }
    }

    private void setDots(){
        dots.clear();
        for(int i=0; i<25; i++) {
            if(i==1 || i==23 || i==5 || i==9 || i==15 || i==19){
                dots.add(new Dot(1, i,2));
                dots.add(new Dot(23, i,2));
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((pacman.getX()!=i || pacman.getY()!=j) && (i!=1 || j==12) && (i!=23 || j==12) && ((9>i||i>15) || (10>j||j>14))) {
                    dots.add(new Dot(i, j, 1));
                }
            }
        }
        for(Wall wall: walls) {
            for (int i = 0; i<dots.size(); i++) {
                if (dots.get(i).getX() == wall.getX() && dots.get(i).getY() == wall.getY()){
                    dots.remove(dots.get(i));
                }
            }
        }
    }

    public int getSize() {
        return size;
    }

    public static int getTick() {
        return tick;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public boolean isGameOver() {
        return !notOver;
    }

    public int getScore(){
        return score;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public boolean isNotWin() {
        return notWin;
    }
}
