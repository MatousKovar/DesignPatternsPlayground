package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.ImageSize;
import cz.cvut.fit.niadp.mvcgame.builder.Difficulty;
import cz.cvut.fit.niadp.mvcgame.command.IButtonCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class MenuView extends AbstractButtonView{
    public enum MenuAction {
        NONE, PLAY, QUIT
    }

    private static final double BUTTON_HEIGHT_RATIO = 0.25;
    private static final int BUTTON_MARGIN = 10;
    private static final int DIFFICULTY_BUTTON_HEIGHT = 160;
    private static final int DIFFICULTY_BUTTON_MARGIN = 24;
    private final ImageButton playButton;
    private final ImageButton quitButton;
    private final ImageButton difficultyButton;

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
        registerButton(playButton);
        registerButton(quitButton);
        ImageSize difficultySize = gameGraphics.getImageSize(
            MvcGameResources.DIFFICULTY_MEDIUM_IMAGE, DIFFICULTY_BUTTON_HEIGHT
        );
        difficultyButton = buttonFactory.create(
            MvcGameResources.DIFFICULTY_MEDIUM_IMAGE,
            new Position(
                (int) Math.round(DIFFICULTY_BUTTON_MARGIN + difficultySize.width() / 2),
                MvcGameConfig.MAX_Y - DIFFICULTY_BUTTON_MARGIN - DIFFICULTY_BUTTON_HEIGHT / 2
            ),
            DIFFICULTY_BUTTON_HEIGHT
        );
        registerButton(difficultyButton);
    }

    public void render(){
        this.gameGraphics.clear();
        gameGraphics.drawImage(MvcGameResources.BACKGROUND_IMAGE, new Position(0,0));
        renderButtons();
    }

    public void setCommands(IButtonCommand playCommand, IButtonCommand quitCommand,
            IButtonCommand nextDifficultyCommand){
        playButton.setCommand(playCommand);
        quitButton.setCommand(quitCommand);
        difficultyButton.setCommand(nextDifficultyCommand);
    }

    public void setDifficulty(Difficulty difficulty) {
        String imagePath = switch (difficulty) {
            case EASY -> MvcGameResources.DIFFICULTY_EASY_IMAGE;
            case MEDIUM -> MvcGameResources.DIFFICULTY_MEDIUM_IMAGE;
            case HARD -> MvcGameResources.DIFFICULTY_HARD_IMAGE;
        };
        difficultyButton.setImagePath(imagePath);
    }

}
