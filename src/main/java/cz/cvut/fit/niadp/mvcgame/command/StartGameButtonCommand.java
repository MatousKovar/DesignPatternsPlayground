package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.screen.AppStateManager;

public class StartGameButtonCommand implements IButtonCommand {
    private final AppStateManager appStateManager;

    public StartGameButtonCommand(AppStateManager manager) {
        this.appStateManager = manager;
    }

    @Override
    public void execute() {
        appStateManager.setGameState();
    }
}
