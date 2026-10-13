package cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.state.DoubleShootingMode;
import cz.cvut.fit.niadp.mvcgame.state.IShootingMode;
import cz.cvut.fit.niadp.mvcgame.state.SingleShootingMode;

public class CannonA extends AbstractCannon {
    
    protected double angle;
    protected int power;
    protected final List<AbstractMissile> shootingBatch;

    private static class Memento {
        private final int positionX;
        private final int positionY;
        private final double angle;
        private final int power;
        private final IShootingMode shootingMode;

        private Memento(int positionX, int positionY, double angle, int power, IShootingMode shootingMode) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.angle = angle;
            this.power = power;
            this.shootingMode = shootingMode;
        }
    }

    public CannonA(Position position, IGameObjectsFactory gameObjectsFactory){
        this.position = position;
        this.gameObjectsFactory = gameObjectsFactory;
        angle = MvcGameConfig.INIT_ANGLE;
        power = MvcGameConfig.INIT_POWER;
        this.shootingMode = new SingleShootingMode();
        shootingBatch = new ArrayList<AbstractMissile>();
    }

    @Override
    public Object createMemento() {
        return new Memento(position.getX(), position.getY(), angle, power, shootingMode);
    }

    @Override
    public void restoreMemento(Object memento) {
        Memento snapshot = (Memento) memento;
        position.setX(snapshot.positionX);
        position.setY(snapshot.positionY);
        angle = snapshot.angle;
        power = snapshot.power;
        shootingMode = snapshot.shootingMode;
    }

    public void moveUp(){
        move(new Vector(0, -1 * MvcGameConfig.MOVE_STEP));
    }

    public void moveDown(){
        
        move(new Vector(0, MvcGameConfig.MOVE_STEP));
    }
    public List<AbstractMissile> shoot(){
        shootingBatch.clear();
        shootingMode.shoot(this);
        return shootingBatch;
    }

    public void primitiveShoot(){
       shootingBatch.add(gameObjectsFactory.createMissile(angle, power));
    }

    @Override
    public void aimUp() {
        angle -= MvcGameConfig.ANGLE_STEP;
    }
    @Override
    public void aimDown() {
        angle += MvcGameConfig.ANGLE_STEP;
    }
    @Override
    public void powerUp() {
        power += MvcGameConfig.POWER_STEP;
    }
    @Override
    public void powerDown() {
        power -= MvcGameConfig.POWER_STEP;
    }

    @Override
    public void toggleShootingMode(){
        if(shootingMode instanceof SingleShootingMode){
            shootingMode = new DoubleShootingMode();
        }else if(shootingMode instanceof DoubleShootingMode){
            shootingMode = new SingleShootingMode();
        }else {

        }
    }

    @Override
    public double getCannonAngle() {
        return this.angle;
    }

    @Override
    public Position getCannonPosition() {
        return this.position;
    }

    @Override
    public int getCannonPower() {
        return this.power;
    }
}
