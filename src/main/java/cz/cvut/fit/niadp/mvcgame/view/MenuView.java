package cz.cvut.fit.niadp.mvcgame.view;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.command.IButtonCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class MenuView implements IView{
    public enum MenuAction {
        NONE, PLAY, QUIT
    }

    private static final double BUTTON_HEIGHT_RATIO = 0.25;
    private static final int BUTTON_MARGIN = 10;
    private final ImageButton playButton;
    private final ImageButton quitButton;
    private final List<ImageButton> buttons;

    private final IGameGraphics gameGraphics;

    public MenuView(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
        ImageButtonFactory buttonFactory = new ImageButtonFactory(gameGraphics);
        int middleX = MvcGameConfig.MAX_X / 2;
        int middleY = MvcGameConfig.MAX_Y / 2;
        int buttonHeight = (int) Math.round(MvcGameConfig.MAX_Y * BUTTON_HEIGHT_RATIO);
        int buttonGroupHeight = buttonHeight * 2 + BUTTON_MARGIN;
        int firstButtonY = middleY - buttonGroupHeight / 2;

        this.playButton = buttonFactory.create(
            MvcGameResources.MENU_IMAGE,
            new Position(middleX, firstButtonY + buttonHeight / 2),
            buttonHeight
        );
        this.quitButton = buttonFactory.create(
            MvcGameResources.QUIT_IMAGE,
            new Position(middleX, firstButtonY + buttonHeight + BUTTON_MARGIN + buttonHeight / 2),
            buttonHeight
        );
        this.buttons = List.of(playButton, quitButton);
    }

    public void render(){
        this.gameGraphics.clear();
        gameGraphics.drawImage(MvcGameResources.BACKGROUND_IMAGE, new Position(0,0));
        for (ImageButton button : buttons) {
            button.render();
        }
    }

    public void processMouseMove(double x, double y){
        for (ImageButton button : buttons) {
            button.processMouseMove(x, y);
        }
    }

    public void processMousePress(double x, double y) {
        for (ImageButton button : buttons) {
            button.processMousePress(x, y);
        }
    }

    public void setCommands(IButtonCommand playCommand, IButtonCommand quitCommand){
        playButton.setCommand(playCommand);
        quitButton.setCommand(quitCommand);
    }

    public void processMouseRelease(double x, double y) {
        for (ImageButton button : buttons){
            button.processMouseRelease(x,y);
        }
    }

    public void resetInteraction() {
        for (ImageButton button : buttons) {
            button.resetInteraction();
        }
    }

}
