package cz.cvut.fit.niadp.mvcgame.command;

import javafx.application.Platform;

public class QuitButtonCommand implements IButtonCommand {

    @Override
    public void execute() {
        Platform.exit();
    }
}
