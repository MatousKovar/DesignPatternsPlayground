package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public interface IMovingStrategy {
    public void updatePosition(AbstractMissile missile); 

    public Position calculateDisplacement(double angle, int power, double time);

    public IMovingStrategy getNextStrategy(GameModel model);

    public String getName();
}
