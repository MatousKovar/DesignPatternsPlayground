package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import java.util.Objects;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.strategy.IEnemyMovingStrategy;

public abstract class AbstractEnemy extends GameObject {
    private record Memento(
        Object positionMemento,
        Position initialPosition,
        Long ageNanos,
        IEnemyMovingStrategy movingStrategy
    ) {}

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

        Position nextPosition = positionAtTime(speed, elapsedSeconds);
        position.setX(nextPosition.getX());
        position.setY(nextPosition.getY());
    }

    public Position positionAtTime(double speed, double elapsedSeconds) {
        if (movingStrategy == null) {
            return copyPosition(position);
        }

        Position origin = initialPosition;
        if (origin == null) {
            origin = position;
        }
        return movingStrategy.calculatePosition(copyPosition(origin), speed, elapsedSeconds);
    }

    @Override
    public Object createMemento() {
        Long ageNanos = null;
        if (spawnedAtNanos != null) {
            ageNanos = currentTimeNanos() - spawnedAtNanos;
        }

        return new Memento(
            super.createMemento(),
            copyPosition(initialPosition),
            ageNanos,
            movingStrategy
        );
    }

    @Override
    public void restoreMemento(Object memento) {
        Memento snapshot = (Memento) memento;
        super.restoreMemento(snapshot.positionMemento());
        initialPosition = copyPosition(snapshot.initialPosition());
        movingStrategy = snapshot.movingStrategy();
        spawnedAtNanos = null;
        if (snapshot.ageNanos() != null) {
            spawnedAtNanos = currentTimeNanos() - snapshot.ageNanos();
        }
    }

    protected long currentTimeNanos() {
        return System.nanoTime();
    }

    private Position copyPosition(Position original) {
        if (original == null) {
            return null;
        }
        return new Position(original.getX(), original.getY());
    }
    
    public EnemySkinType getEnemySkinType(){
        return type;
    };

    public Hitbox getHitbox(){
        return this.hitbox;
    }

    public abstract int getScoreForKill();
    
}
