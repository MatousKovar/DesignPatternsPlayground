package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.command.QuitButtonCommand;
import cz.cvut.fit.niadp.mvcgame.command.StartGameButtonCommand;
import cz.cvut.fit.niadp.mvcgame.view.MenuView;

public class MenuState implements IAppState{

    private final MenuView view;
    private final MvcGame mvcGame;

    public MenuState(IGameGraphics graphics, MvcGame mvcGame){
        this.view = new MenuView(graphics);
        this.mvcGame = mvcGame;
    }
    @Override
    public void setCommands(AppStateManager appStateManager){
        view.setCommands(new StartGameButtonCommand(appStateManager, mvcGame), new QuitButtonCommand());
    }
    
    @Override
    public void processPressedKeys(List<String> keys, AppStateManager navigator) {
        return;
    }

    @Override
    public void render() {
        this.view.render();
    }

    @Override
    public void processMouseClick(double x, double y, AppStateManager navigator) {
    }

    @Override
    public void processMouseMove(double x, double y){
        this.view.processMouseMove(x, y);
    }

    @Override
    public void processMousePress(double x, double y) {
        view.processMousePress(x, y);
    }

    @Override
    public void processMouseRelease(double x, double y) {
        this.view.processMouseRelease(x, y);
    }

    @Override
    public void resetMouseInteraction() {
        view.resetInteraction();
    }
    
}
