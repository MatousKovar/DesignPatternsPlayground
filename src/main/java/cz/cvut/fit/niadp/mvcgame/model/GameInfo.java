package cz.cvut.fit.niadp.mvcgame.model;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

public class GameInfo
{
    private int score;
    private AbstractCannon cannon;
    private IMovingStrategy strategy;

    public GameInfo(AbstractCannon cannon, IMovingStrategy strategy)
    {
        score = 0;
        this.cannon = cannon;
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

    

}
