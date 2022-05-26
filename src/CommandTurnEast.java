public class CommandTurnEast extends Command{

    public CommandTurnEast(Pacman pacman, int tick) {
        super(pacman, tick);
    }

    @Override
    public void execute() {
        getPacman().turnEast();
    }
}
