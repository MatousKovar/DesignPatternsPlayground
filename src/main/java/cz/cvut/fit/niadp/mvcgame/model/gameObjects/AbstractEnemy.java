package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import java.util.Objects;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.strategy.IEnemyMovingStrategy;

public abstract class AbstractEnemy extends GameObject {
    protected EnemySkinType type;
    protected Hitbox hitbox;
    private IEnemyMovingStrategy movingStrategy;
    private Position initialPosition;
    private Long spawnedAtNanos;

    public void spawn(long nowNanos) {
        spawnedAtNanos = nowNanos;
        initialPosition = new Position(position.getX(), position.getY());
    }

    public void update(double speed, long nowNanos) {
        if (spawnedAtNanos == null) {
            return;
        }

        double elapsedSeconds = (nowNanos - spawnedAtNanos) / 1_000_000_000.0;
        move(speed, elapsedSeconds);
    }

    public void setMovingStrategy(IEnemyMovingStrategy movingStrategy) {
        this.movingStrategy = Objects.requireNonNull(movingStrategy, "Moving strategy must not be null");
    }

    public void move(double speed, double elapsedSeconds) {
        if (movingStrategy == null) {
            return;
        }

        if (initialPosition == null) {
            initialPosition = new Position(position.getX(), position.getY());
        }

        Position nextPosition = movingStrategy.calculatePosition(initialPosition, speed, elapsedSeconds);
        position.setX(nextPosition.getX());
        position.setY(nextPosition.getY());
    }
    
    public EnemySkinType getEnemySkinType(){
        return type;
    };

    public Hitbox getHitbox(){
        return this.hitbox;
    }

    public abstract int getScoreForKill();
    
}
