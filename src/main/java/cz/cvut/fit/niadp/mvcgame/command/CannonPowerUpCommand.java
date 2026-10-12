package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;

public class CannonPowerUpCommand extends AbstractGameCommand {

    public CannonPowerUpCommand(IGameModel model) {
        this.model = model;
    }

    @Override
    protected void execute() {
        model.cannonPowerUp();
    }
}
