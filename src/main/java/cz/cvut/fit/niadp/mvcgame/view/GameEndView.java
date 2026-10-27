package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.command.IButtonCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class GameEndView extends AbstractButtonView{

    public enum MenuAction {
        NONE, REPLAY, QUIT
    }

    private static final double BUTTON_HEIGHT_RATIO = 0.25;
    private static final int BUTTON_MARGIN = 10;
    private final ImageButton replayButton;
    private final ImageButton quitButton;
    private final IGameGraphics gameGraphics;

    public GameEndView(IGameGraphics graphics){
        this.gameGraphics = graphics;
        ImageButtonFactory buttonFactory = new ImageButtonFactory(gameGraphics);
        int middleX = MvcGameConfig.MAX_X / 2;
        int middleY = MvcGameConfig.MAX_Y / 2;
        int buttonHeight = (int) Math.round(MvcGameConfig.MAX_Y * BUTTON_HEIGHT_RATIO);
        int buttonGroupHeight = buttonHeight * 2 + BUTTON_MARGIN;
        int firstButtonY = middleY - buttonGroupHeight / 2;

        this.replayButton = buttonFactory.create(
            MvcGameResources.MENU_IMAGE,
            new Position(middleX, firstButtonY + buttonHeight / 2),
            buttonHeight
        );
        this.quitButton = buttonFactory.create(
            MvcGameResources.QUIT_IMAGE,
            new Position(middleX, firstButtonY + buttonHeight + BUTTON_MARGIN + buttonHeight / 2),
            buttonHeight
        );
        registerButton(replayButton);
        registerButton(quitButton);
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }

    public void setCommands(IButtonCommand restart, IButtonCommand quit){
        this.replayButton.setCommand(restart);
        this.quitButton.setCommand(quit);
    }

}
