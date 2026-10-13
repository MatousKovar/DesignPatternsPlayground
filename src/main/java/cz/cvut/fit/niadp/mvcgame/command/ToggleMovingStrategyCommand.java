package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;

public class ToggleMovingStrategyCommand extends AbstractGameCommand {

    public ToggleMovingStrategyCommand(IGameModel model) {
        this.model = model;
    }

    @Override
    protected void execute() {
        model.toggleMovingStrategy();
    }
}
