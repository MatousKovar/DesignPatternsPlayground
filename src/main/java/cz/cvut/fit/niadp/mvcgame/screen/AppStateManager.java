package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

public class AppStateManager{
    private final IAppState menuState;
    private final IAppState gameState;
    private final IAppState pauseState;
    private IAppState currentState;

    public AppStateManager(MenuState menuState, GameState gameState, PauseState pauseState){
        this.currentState = menuState;
        this.gameState = gameState;
        this.pauseState = pauseState;
        this.menuState = menuState;
    }

    public void render(){
        this.currentState.render();
    }

    public void processPressedKeys(List<String> keys){
        this.currentState.processPressedKeys(keys);
    }

    public void processMouseClick(double x, double y){
        this.currentState.processMouseClick(x, y,this);
    }

    public void setGameState(){
        this.currentState = gameState;
    }
}