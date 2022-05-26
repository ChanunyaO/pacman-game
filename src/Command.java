public abstract class Command {
    private Pacman pacman;
    private int tick;

    public Command(Pacman pacman, int tick) {
        this.pacman = pacman;
        this.tick = tick;
    }

    public abstract void execute();

    public Pacman getPacman() {
        return pacman;
    }

    public int getTick() {
        return tick;
    }

}