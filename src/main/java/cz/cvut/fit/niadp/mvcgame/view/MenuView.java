package cz.cvut.fit.niadp.mvcgame.view;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.ImageSize;
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
        int middleX = MvcGameConfig.MAX_X / 2;
        int middleY = MvcGameConfig.MAX_Y / 2;
        int buttonHeight = (int) Math.round(MvcGameConfig.MAX_Y * BUTTON_HEIGHT_RATIO);
        int buttonGroupHeight = buttonHeight * 2 + BUTTON_MARGIN;
        int firstButtonY = middleY - buttonGroupHeight / 2;

        this.playButton = createButton(
            MvcGameResources.MENU_IMAGE,
            new Position(middleX, firstButtonY + buttonHeight / 2)
        );
        this.quitButton = createButton(
            MvcGameResources.QUIT_IMAGE,
            new Position(middleX, firstButtonY + buttonHeight + BUTTON_MARGIN + buttonHeight / 2)
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

    public void onMouseMoved(double x, double y){
        for (ImageButton button : buttons) {
            button.onMouseMoved(x, y);
        }
    }

    public void onMousePressed(double x, double y) {
        for (ImageButton button : buttons) {
            button.onMousePressed(x, y);
        }
    }

    /**
     * Note: asi idealne pro budouci rozsirovani tady mit nejakej CommandDesign pattern kde ten button bude mit command a ten metodu execute() 
     */
    public MenuAction onMouseReleased(double x, double y) {
        boolean playClicked = playButton.onMouseReleased(x, y);
        boolean quitClicked = quitButton.onMouseReleased(x, y);
        if (playClicked) {
            resetInteraction();
            return MenuAction.PLAY;
        }
        if (quitClicked) {
            resetInteraction();
            return MenuAction.QUIT;
        }
        return MenuAction.NONE;
    }

    public void resetInteraction() {
        for (ImageButton button : buttons) {
            button.resetInteraction();
        }
    }

    private Position centerImage(int trueMiddleX, int trueMiddleY, ImageSize imageSize){
        int finalX = (int) Math.round(trueMiddleX - imageSize.width() / 2);
        int finalY = (int) Math.round(trueMiddleY - imageSize.height() / 2);
        return new Position(finalX, finalY);
    }

    private ImageButton createButton(String imagePath, Position centerPosition){
        int buttonHeight = (int) Math.round(MvcGameConfig.MAX_Y * BUTTON_HEIGHT_RATIO);
        ImageSize buttonSize = gameGraphics.getImageSize(imagePath, buttonHeight);
        Position position = centerImage(centerPosition.getX(), centerPosition.getY(), buttonSize);
        return new ImageButton(gameGraphics, imagePath, position, buttonHeight);
    }
}
