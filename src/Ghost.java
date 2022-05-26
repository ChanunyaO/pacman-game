import java.util.List;
import java.util.Random;

public class Ghost extends WObject{
    private Random random = new Random();
    private int randomInt;
    private boolean scatter;
    private int score;
    public Ghost(int x, int y, Direction direction) {
        super(x, y, direction);
        scatter = false;
        score = 10;
    }

    @Override
    public void move(List nearBlock) {
        if (World.getTick() % 2 == 0) {
            randomInt = random.nextInt(5);
            if (randomInt == 1) {
                turnNorth();
            } else if (randomInt == 2) {
                turnSouth();
            } else if (randomInt == 3) {
                turnEast();
            } else if(randomInt == 4){
                turnWest();
            }
        }
        super.move(nearBlock);
    }

    public boolean isScatter(){
        return scatter;
    }

    public int getScore() {
        return score;
    }

    public void setScatter(boolean scatter) {
        this.scatter = scatter;
    }

    public boolean canEat(WObject obj){
        return getX() == obj.getX() && getY() == obj.getY() && !scatter;
    }
}
