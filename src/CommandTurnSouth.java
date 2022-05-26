public class CommandTurnSouth extends Command{

    public CommandTurnSouth(Pacman pacman, int tick) {
        super(pacman, tick);
    }

    @Override
    public void execute() {
        getPacman().turnSouth();
    }
}
