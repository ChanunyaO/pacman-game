import java.util.List;

public abstract class WObject {
    private int x;
    private int y;

    private int dx = 0;
    private int dy = 0;
    private Direction direction;

    public WObject(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void turnNorth() {
        direction = Direction.UP;
        dx = direction.getX();
        dy = direction.getY();
    }

    public void turnSouth() {
        direction = Direction.DOWN;
        dx = direction.getX();
        dy = direction.getY();
    }

    public void turnWest() {
        direction = Direction.LEFT;
        dx = direction.getX();
        dy = direction.getY();
    }

    public void turnEast() {
        direction = Direction.RIGHT;
        dx = direction.getX();
        dy = direction.getY();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void reset() {
        dx = dy = 0;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setPosition(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void move(List nearBlock){
        if ((nearBlock.contains("down") && dy == -1) || (nearBlock.contains("up") && dy == 1)) {
            dy = 0;
        }
        if ((nearBlock.contains("right") && dx == 1) || (nearBlock.contains("left") && dx == -1)) {
            dx = 0;
        }
        if(x<0){
            x = 25;
        }else if(x>=25){
            x = -1;
        }else if(y<0){
            y = 25;
        }else if(y>=25){
            y = -1;
        }
        x += dx;
        y += dy;
    }

    public boolean eat(WObject obj){
        return x == obj.getX() && y == obj.getY();
    }

    public boolean eat(Dot dot){
        return x == dot.getX() && y == dot.getY();
    }
}
