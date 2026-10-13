package cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractImpactObject;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public class ImpactA extends AbstractImpactObject{
    private static final long LIFETIME_DURATION_MILLIS = 1_500;

    public ImpactA(Position initPosition) {
        super(initPosition);
    }

    @Override
    public boolean isExpired() {
        return getAge() >= getLifetimeDurationMillis();
    }

    @Override
    public long getLifetimeDurationMillis() {
        return LIFETIME_DURATION_MILLIS;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
    
}
