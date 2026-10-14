package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class MenuView implements IView{
    private static final double BUTTON_HEIGHT_RATIO = 0.25;

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
            
            
            int margin = 10;

            gameGraphics.drawImageAtHeight(MvcGameResources.MENU_IMAGE, new Position(middleX, middleY), buttonHeight);
            gameGraphics.drawImageAtHeight(MvcGameResources.QUIT_IMAGE, new Position(middleX, middleY+margin+buttonHeight), buttonHeight);
        }
    };
}
