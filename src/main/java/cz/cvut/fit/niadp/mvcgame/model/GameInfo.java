package cz.cvut.fit.niadp.mvcgame.model;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitable;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public class GameInfo implements IVisitable
{
    private int score;
    private AbstractCannon cannon;
    private IMovingStrategy strategy;

    public GameInfo(AbstractCannon cannon, IMovingStrategy strategy)
    {
        score = 0;
        this.cannon = cannon;
        this.strategy = strategy;
    }

    public void increaseScore(int value)
    {
        score += value;
    }

    public int getScore(){
        return score;
    }

    public double getCannonAngle(){
        return cannon.getCannonAngle(); 

    }

    public double getCannonPower(){
        return cannon.getCannonPower();
    }
    
    public String getMovingStrategy(){
        return strategy.getName();
    }

    public String getShootingMode(){
        return cannon.getShootingModeName();
    }

    public void setMovingStrategy(IMovingStrategy movingStrategy) {
        this.strategy = movingStrategy;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    

}
