package cz.cvut.fit.niadp.mvcgame.decorator;

import java.util.Set;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

public abstract class MissileDecorator extends AbstractMissile {

    protected AbstractMissile missileObject;
    protected MissileDecorator(Position initPosition, double initAngle, int initVelocity,
            IMovingStrategy movingStrategy, MissileSkinType type) {
        super(initPosition, initAngle, initVelocity, movingStrategy, type);
    }
}
