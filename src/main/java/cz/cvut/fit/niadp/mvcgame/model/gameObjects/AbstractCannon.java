package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import java.util.List;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.state.IShootingMode;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public  abstract class AbstractCannon extends GameObject {
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

    public void accept(IVisitor visitor){
        visitor.visit(this);
    }

}
