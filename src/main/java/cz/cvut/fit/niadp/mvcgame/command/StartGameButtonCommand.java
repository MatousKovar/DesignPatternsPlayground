package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.screen.AppStateManager;

public class StartGameButtonCommand implements IButtonCommand {
    private final AppStateManager appStateManager;
    private final MvcGame mvcGame;

    public StartGameButtonCommand(AppStateManager manager, MvcGame mvcGame) {
        this.appStateManager = manager;
        this.mvcGame = mvcGame;
    }

    @Override
    public void execute() {
        mvcGame.generateLevel();
        appStateManager.setGameState();
    }
}
