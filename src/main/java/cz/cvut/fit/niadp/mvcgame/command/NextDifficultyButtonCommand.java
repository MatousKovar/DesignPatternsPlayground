package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.screen.MenuState;

public class NextDifficultyButtonCommand implements IButtonCommand {
    private final MenuState menuState;

    public NextDifficultyButtonCommand(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public void execute() {
        menuState.nextDifficulty();
    }
}
