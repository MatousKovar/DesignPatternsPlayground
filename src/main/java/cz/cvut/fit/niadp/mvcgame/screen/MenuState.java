package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

import javafx.application.Platform;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.command.IButtonCommand;
import cz.cvut.fit.niadp.mvcgame.command.QuitButtonCommand;
import cz.cvut.fit.niadp.mvcgame.command.StartGameButtonCommand;
import cz.cvut.fit.niadp.mvcgame.view.MenuView;

public class MenuState implements IAppState{

    private MenuView view;

    public MenuState(IGameGraphics graphics){
        this.view = new MenuView(graphics);
    }
    @Override
    public void setCommands(AppStateManager appStateManager){
        view.setCommands(new StartGameButtonCommand(appStateManager), new QuitButtonCommand());
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
    }

    @Override
    public void processMouseMove(double x, double y){
        this.view.processMouseMove(x, y);
    }

    @Override
    public void processMousePress(double x, double y) {
        view.processMousePress(x, y);
    }

    // Jo tady by bylo lepsi mit nejakou formu GameStateCommand - a ty commandy by executoval AppStateManager ale pro moje dve tlacitka jsem usoudi
    @Override
    public void processMouseRelease(double x, double y) {
        this.view.processMouseRelease(x, y);
    }

    @Override
    public void resetMouseInteraction() {
        view.resetInteraction();
    }
    
}
