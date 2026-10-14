package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.ImageSize;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class MenuView implements IView{
    private static final double BUTTON_HEIGHT_RATIO = 0.25;
    private static final int BUTTON_MARGIN = 10;

    private IGameGraphics gameGraphics; 

    public MenuView(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
    }

    public void render(){
        if(gameGraphics != null) {
            gameGraphics.clear();
            gameGraphics.drawImage(MvcGameResources.BACKGROUND_IMAGE, new Position(0,0));

            int middleX = MvcGameConfig.MAX_X / 2;
            int middleY = MvcGameConfig.MAX_Y / 2;

            int buttonHeight = (int) Math.round(MvcGameConfig.MAX_Y * BUTTON_HEIGHT_RATIO);

            ImageSize menuButtonSize  = gameGraphics.getImageSize(MvcGameResources.MENU_IMAGE,buttonHeight);
            ImageSize quitButtonSize = gameGraphics.getImageSize(MvcGameResources.QUIT_IMAGE,buttonHeight);

            int buttonGroupHeight = buttonHeight * 2 + BUTTON_MARGIN;
            int firstButtonY = middleY - buttonGroupHeight / 2;
            Position targetPlayButton = centerImage(middleX, firstButtonY + buttonHeight / 2, menuButtonSize);
            Position targetQuitButton = centerImage(
                middleX,
                firstButtonY + buttonHeight + BUTTON_MARGIN + buttonHeight / 2,
                quitButtonSize
            );


            gameGraphics.drawImageAtHeight(MvcGameResources.MENU_IMAGE, targetPlayButton, buttonHeight);
            gameGraphics.drawImageAtHeight(MvcGameResources.QUIT_IMAGE, targetQuitButton, buttonHeight);
        }
    }

    private Position centerImage(int trueMiddleX, int trueMiddleY, ImageSize imageSize){
        int finalX = (int) Math.round(trueMiddleX - imageSize.width() / 2);
        int finalY = (int) Math.round(trueMiddleY - imageSize.height() / 2);
        return new Position(finalX, finalY);
    }
}
