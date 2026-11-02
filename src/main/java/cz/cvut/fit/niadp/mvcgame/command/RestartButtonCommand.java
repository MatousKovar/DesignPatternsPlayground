package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.screen.AppStateManager;

public class RestartButtonCommand implements IButtonCommand {
    private final AppStateManager appStateManager;
    private final MvcGame mvcGame;

    public RestartButtonCommand(AppStateManager manager, MvcGame game){
        appStateManager = manager;
        mvcGame = game;
    }
    @Override
    public void execute() {
        mvcGame.restartGame();
        appStateManager.setGameState();
        return;
    }
    
}
