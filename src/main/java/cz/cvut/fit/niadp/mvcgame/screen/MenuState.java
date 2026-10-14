package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.view.MenuView;

public class MenuState implements IAppState{

    private MenuView view;

    public MenuState(IGameGraphics graphics){
        this.view = new MenuView(graphics);
    }
    
    @Override
    public void processPressedKeys(List<String> keys) {
        return;
    }

    @Override
    public void render() {
        this.view.render();
    }

    @Override
    public void processMouseClick(double x, double y, AppStateManager navigator) {
        navigator.setGameState();
    }
    
}
