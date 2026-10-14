package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.screen.AppStateManager;

public class PauseButtonCommand implements IButtonCommand {
    private final AppStateManager appStateManager;

    public PauseButtonCommand(AppStateManager manager) {
        this.appStateManager = manager;
    }

    @Override
    public void execute() {
        appStateManager.setPauseState();
    }
}
