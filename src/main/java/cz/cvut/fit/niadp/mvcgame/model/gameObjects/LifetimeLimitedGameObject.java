package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public abstract class LifetimeLimitedGameObject extends GameObject {

    private record Memento(Object positionMemento, long ageMillis) {}

    protected LocalDateTime bornAt;
    
    protected LifetimeLimitedGameObject(Position initPosition) {
        position = initPosition;
        bornAt = LocalDateTime.now();
    }
    public long getAge() {
        return ChronoUnit.MILLIS.between(bornAt, now());
    }

    protected LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public Object createMemento() {
        return new Memento(super.createMemento(), getAge());
    }

    @Override
    public void restoreMemento(Object memento) {
        Memento snapshot = (Memento) memento;
        super.restoreMemento(snapshot.positionMemento());
        bornAt = now().minus(snapshot.ageMillis(), ChronoUnit.MILLIS);
    }

}
