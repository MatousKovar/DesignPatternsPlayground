package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.view.PauseView;

public class PauseState implements IAppState{
    private PauseView view;

    public PauseState(IGameGraphics graphics){
        this.view = new PauseView(graphics);
    }
    @Override
    public void processPressedKeys(List<String> keys) {
        return;
    }

    @Override
    public void processMouseClick(double x, double y, AppStateManager navigator) {
        //todo logika
    }

    @Override
    public void render() {
        this.view.render();
    }

}
