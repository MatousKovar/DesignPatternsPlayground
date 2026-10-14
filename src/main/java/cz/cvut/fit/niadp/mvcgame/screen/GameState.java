package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.MvcGame;

public class GameState implements IAppState{
    private final MvcGame mvcGame;
    public GameState(MvcGame mvcGame){
        this.mvcGame = mvcGame;
    }

    @Override
    public void processPressedKeys(List<String> keys) {
        mvcGame.processPressedKeys(keys);
    }

    @Override
    public void processMouseClick(double x, double y, AppStateManager navigator) {
        return;
    }

    @Override
    public void render() {
        return;
    }
    
}
