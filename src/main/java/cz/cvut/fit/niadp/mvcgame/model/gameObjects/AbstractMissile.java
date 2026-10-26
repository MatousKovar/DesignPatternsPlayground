package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

import java.util.Set;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

public abstract class AbstractMissile extends LifetimeLimitedGameObject {

    private final Position initialPosition;
    private final double initAngle;
    private final int initVelocity;
    protected IMovingStrategy movingStrategy;
    protected Hitbox hitbox;
    protected MissileSkinType type;

    protected AbstractMissile(Position initPosition, double initAngle, int initVelocity, IMovingStrategy movingStrategy, MissileSkinType type) {
        super(initPosition);
        this.initialPosition = new Position(initPosition.getX(), initPosition.getY());
        this.initAngle = initAngle;
        this.initVelocity = initVelocity;
        this.movingStrategy = movingStrategy;
        this.type = type;
        this.hitbox = new Hitbox(initPosition, type.getWidth(), type.getHeight());
    }

    public void accept(IVisitor visitor){
        visitor.visit(this);
    }

    public double getInitAngle() {
        return initAngle;
    }
    public int getInitVelocity() {
        return initVelocity;
    }

    public IMovingStrategy getMovingStrategy() {
        return movingStrategy;
    }

    public Hitbox getHitbox(){
        return this.hitbox;
    }

    public void move(){
        movingStrategy.updatePosition(this);
    }

    public void moveFromStart(Position displacement) {
        position.setX(initialPosition.getX() + displacement.getX());
        position.setY(initialPosition.getY() + displacement.getY());
    }

    public MissileSkinType getMissileSkinType(){
        return this.type;
    };

    public abstract Set<AbstractEnemy> getAffectedEnemies(AbstractEnemy hitEnemy,Set<AbstractEnemy> enemies);

    public AbstractImpactObject createImpact(IGameObjectsFactory factory, AbstractEnemy hitEnemy){
        return factory.createImpact(hitEnemy.getPosition(), hitEnemy.getHitbox().getWidth(),hitEnemy.getHitbox().getHeight());
    };
}
