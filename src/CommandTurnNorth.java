public class CommandTurnNorth extends Command{

    public CommandTurnNorth(Pacman pacman, int tick) {
        super(pacman, tick);
    }

    @Override
    public void execute() {
        getPacman().turnNorth();
    }
}
