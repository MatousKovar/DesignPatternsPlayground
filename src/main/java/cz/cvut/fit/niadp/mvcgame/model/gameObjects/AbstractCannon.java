package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.state.IShootingMode;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.strategy.RealMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.strategy.SimpleMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public  abstract class AbstractCannon extends GameObject {
    private static final int TRAJECTORY_POINT_COUNT = 20;
    private static final double TRAJECTORY_TIME_STEP = 0.5;

    protected IGameObjectsFactory gameObjectsFactory;
    protected IShootingMode shootingMode;

    public abstract void moveUp();
    public abstract void moveDown();

    public abstract void aimUp();
    public abstract void aimDown();
    public abstract void powerUp();
    public abstract void powerDown();
    
    public abstract void toggleShootingMode();
    public abstract List<AbstractMissile> shoot();
    public abstract void primitiveShoot();
    
    public abstract double getCannonAngle();
    public abstract int getCannonPower();
    public abstract Position getCannonPosition();

    public String getShootingModeName(){
        return shootingMode.getName();
    }

    public abstract Object createMemento();
    
    public abstract void restoreMemento(Object memento);

    public abstract void resetSettings();

    public List<Position> getTrajectoryPreview(IMovingStrategy movingStrategy) {
        if (!(movingStrategy instanceof RealMovingStrategy)
                && !(movingStrategy instanceof SimpleMovingStrategy)) {
            return List.of();
        }

        List<Position> points = new ArrayList<>(TRAJECTORY_POINT_COUNT);
        Position origin = getCannonPosition();
        double angle = getCannonAngle();
        int power = getCannonPower();
        Position previousPoint = origin;
        for (int i = 1; i <= TRAJECTORY_POINT_COUNT; i++) {
            double time = i * TRAJECTORY_TIME_STEP;
            Position displacement = movingStrategy.calculateDisplacement(angle, power, time);
            Position base = origin;
            if (movingStrategy instanceof SimpleMovingStrategy) {
                base = previousPoint;
            }
            Position point = new Position(
                base.getX() + displacement.getX(),
                base.getY() + displacement.getY()
            );
            points.add(point);
            previousPoint = point;
        }
        return points;
    }


    public void accept(IVisitor visitor){
        visitor.visit(this);
    }

}
