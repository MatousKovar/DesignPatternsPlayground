package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;

public class ToggleExplosiveShotCommand extends AbstractGameCommand {

    public ToggleExplosiveShotCommand(IGameModel model) {
        this.model = model;
    }

    @Override
    protected void execute() {
        model.toggleExplosiveShot();
    }
}
