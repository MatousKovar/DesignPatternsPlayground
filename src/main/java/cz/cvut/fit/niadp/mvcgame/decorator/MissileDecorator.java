package cz.cvut.fit.niadp.mvcgame.decorator;


import java.util.Set;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.Hitbox;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

public abstract class MissileDecorator extends AbstractMissile {

    protected final AbstractMissile missileObject;
    protected MissileDecorator(AbstractMissile missile) {
        super(missile.getPosition(), missile.getInitAngle(), missile.getInitVelocity(),
         missile.getMovingStrategy(), missile.getMissileSkinType());
        this.missileObject = missile;
    }
    @Override
    public Hitbox getHitbox() {
        return missileObject.getHitbox();
    }
    @Override
    public void move() {
        missileObject.move();
    }
    @Override
    public void moveFromStart(Position displacement) {
        missileObject.moveFromStart(displacement);
    }
    @Override
    public Position getPosition() {
        return missileObject.getPosition();
    }
    @Override
    public MissileSkinType getMissileSkinType() {
        return missileObject.getMissileSkinType();
    }
    @Override
    public IMovingStrategy getMovingStrategy() {
        return missileObject.getMovingStrategy();
    }
    @Override
    public Object createMemento() {
        return missileObject.createMemento();
    }
    @Override
    public long getAge() {
        return missileObject.getAge();
    }
    @Override
    public void restoreMemento(Object memento) {
        missileObject.restoreMemento(memento);
    }
    @Override
    public Set<AbstractEnemy> getAffectedEnemies(AbstractEnemy hitEnemy, Set<AbstractEnemy> enemies) {
        return missileObject.getAffectedEnemies(hitEnemy, enemies);
    }
}
