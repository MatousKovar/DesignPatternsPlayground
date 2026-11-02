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

    private static final double BUTTON_HEIGHT_RATIO = 0.18;
    private static final int BUTTON_MARGIN = 70;
    private static final int RESULT_FONT_SIZE = 64;
    private final ImageButton replayButton;
    private final ImageButton quitButton;
    private final IGameGraphics gameGraphics;
    private boolean win;

    public GameEndView(IGameGraphics graphics){
        this.gameGraphics = graphics;
        ImageButtonFactory buttonFactory = new ImageButtonFactory(gameGraphics);
        int middleX = MvcGameConfig.MAX_X / 2;
        int middleY = (int) Math.round(MvcGameConfig.MAX_Y * 0.55);
        int buttonHeight = (int) Math.round(MvcGameConfig.MAX_Y * BUTTON_HEIGHT_RATIO);
        int buttonOffset = (buttonHeight + BUTTON_MARGIN) / 2;

        this.replayButton = buttonFactory.create(
            MvcGameResources.PLAY_IMAGE,
            new Position(middleX - buttonOffset, middleY),
            buttonHeight
        );
        this.quitButton = buttonFactory.create(
            MvcGameResources.QUIT_IMAGE,
            new Position(middleX + buttonOffset, middleY),
            buttonHeight
        );
        registerButton(replayButton);
        registerButton(quitButton);
    }

    @Override
    public void render() {
        gameGraphics.clear();
        gameGraphics.drawImage(MvcGameResources.BACKGROUND_IMAGE, new Position(0, 0));

        String resultText = "You lost";
        if (win) {
            resultText = "You won";
        }

        Position textPosition = new Position(MvcGameConfig.MAX_X / 2, MvcGameConfig.MAX_Y / 3);
        gameGraphics.drawCenteredText(resultText, textPosition, RESULT_FONT_SIZE);
        renderButtons();
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setCommands(IButtonCommand restart, IButtonCommand quit){
        this.replayButton.setCommand(restart);
        this.quitButton.setCommand(quit);
    }

}
