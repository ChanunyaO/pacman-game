public class CommandTurnWest extends Command{

    public CommandTurnWest(Pacman pacman, int tick) {
        super(pacman, tick);
    }

    @Override
    public void execute() {
        getPacman().turnWest();
    }
}
