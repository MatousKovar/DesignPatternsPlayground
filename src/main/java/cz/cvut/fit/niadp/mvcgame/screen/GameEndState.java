package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.command.QuitButtonCommand;
import cz.cvut.fit.niadp.mvcgame.command.RestartButtonCommand;
import cz.cvut.fit.niadp.mvcgame.view.GameEndView;

public class GameEndState implements IAppState {
    private boolean win;
    private IGameGraphics gameGraphics;
    private final GameEndView view;


    public GameEndState(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
        this.view = new GameEndView(gameGraphics);
    }
    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isWin() {
        return win;
    }

    @Override
    public void processPressedKeys(List<String> keys, AppStateManager navigator) {
    }

    @Override
    public void processMouseClick(double x, double y, AppStateManager navigator) {
    }

    @Override
    public void setCommands(AppStateManager appStateManager) {
        view.setCommands(new RestartButtonCommand(), new QuitButtonCommand());
    }

    @Override
    public void processMouseMove(double x, double y) {
        // TODO: Forward mouse movement to the view.
    }

    @Override
    public void processMousePress(double x, double y) {
        // TODO: Forward mouse presses to the view.
    }

    @Override
    public void processMouseRelease(double x, double y) {
        
    }

    @Override
    public void resetMouseInteraction() {

    }

    @Override
    public void render() {
        view.render();
    }
}
