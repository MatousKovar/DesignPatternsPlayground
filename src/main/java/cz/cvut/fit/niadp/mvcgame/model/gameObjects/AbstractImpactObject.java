package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public abstract class AbstractImpactObject extends LifetimeLimitedGameObject{
    protected int width;
    protected int height;
    protected AbstractImpactObject(Position initPosition,int height, int width) {
        super(initPosition);
        this.width = width;
        this.height = height;
    }

    public abstract boolean isExpired();
    public abstract long getLifetimeDurationMillis();
    public int getWidth(){return this.width;};
    public int getHeight(){return this.height;};
    
}
