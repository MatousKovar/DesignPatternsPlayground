package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public interface IMovingStrategy {
    public void updatePosition(AbstractMissile missile); 

    public Vector calculateDisplacement(double angle, int power, double time);

    public IMovingStrategy getNextStrategy(GameModel model);

    public String getName();
}
