package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;

public class SpawnEnemyCommand extends AbstractGameCommand {

    public SpawnEnemyCommand(IGameModel model) {
        this.model = model;
    }

    @Override
    protected void execute() {
        model.spawnNewEnemy();
    }
}
