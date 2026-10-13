package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public abstract class AbstractImpactObject extends LifetimeLimitedGameObject{
    protected AbstractImpactObject(Position initPosition) {
        super(initPosition);
    }

    public abstract boolean isExpired();
    public abstract long getLifetimeDurationMillis();
    
}
