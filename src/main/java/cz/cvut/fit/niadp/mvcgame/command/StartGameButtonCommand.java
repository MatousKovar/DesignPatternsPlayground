package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.screen.AppStateManager;
import cz.cvut.fit.niadp.mvcgame.screen.MenuState;

public class StartGameButtonCommand implements IButtonCommand {
    private final AppStateManager appStateManager;
    private final MvcGame mvcGame;
    private final MenuState menuState;

    public StartGameButtonCommand(AppStateManager manager, MvcGame mvcGame, MenuState menuState) {
        this.appStateManager = manager;
        this.mvcGame = mvcGame;
        this.menuState = menuState;
    }

    @Override
    public void execute() {
        mvcGame.generateLevel(menuState.getDifficulty());
        appStateManager.setGameState();
    }
}
