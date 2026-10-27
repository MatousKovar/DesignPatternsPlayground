package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;

public class GameEndState implements IAppState {
    private boolean win;
    private IGameGraphics gameGraphics;
    private final GameEndView view;


    public GameEndState(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
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
        // TODO: Connect the Replay and Exit commands to the view.
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
        // TODO: Forward mouse releases to the view.
    }

    @Override
    public void resetMouseInteraction() {
        // TODO: Reset the view's buttons when leaving this state.
    }

    @Override
    public void render() {
        // TODO: Render the end view using the stored result.
    }
}
