package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;


public class AppStateManager{
    private final IAppState menuState;
    private final IAppState gameState;
    private final IAppState pauseState;
    private final GameEndState endGameState;
    private IAppState currentState;

    public AppStateManager(MenuState menuState, GameState gameState, PauseState pauseState, GameEndState endGameState){
        this.currentState = menuState;
        this.gameState = gameState;
        this.pauseState = pauseState;
        this.menuState = menuState;
        this.endGameState = endGameState;
        initCommands();
    }

    public void render(){
        this.currentState.render();
    }

    public void initCommands(){
        menuState.setCommands(this);
    }

    public void processPressedKeys(List<String> keys){
        this.currentState.processPressedKeys(keys, this);
        this.render();
    }

    public void processMouseClick(double x, double y){
        this.currentState.processMouseClick(x, y,this);
        this.render();
    }

    public void processMouseMove(double x, double y) {
        currentState.processMouseMove(x, y);
    }

    public void processMousePress(double x, double y) {
        currentState.processMousePress(x, y);
        render();
    }

    public void processMouseRelease(double x, double y) {
        currentState.processMouseRelease(x, y);
        render();
    }

    public void resetMouseInteraction() {
        currentState.resetMouseInteraction();
        render();
    }

    public void setGameState(){
        currentState.resetMouseInteraction();
        this.currentState = gameState;
        this.render();
    }

    public void setMenuState(){
        currentState.resetMouseInteraction();
        this.currentState = menuState;
        this.render();
    }

    public void setGameEndState(boolean win){
        currentState.resetMouseInteraction();
        currentState = endGameState;
        endGameState.setWin(win);
        this.render();
    }

    public void setPauseState(){
        currentState.resetMouseInteraction();
        this.currentState = pauseState;
        this.render();
    }
}
