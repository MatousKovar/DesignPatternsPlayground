package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public class RealMovingStrategy implements IMovingStrategy {

    @Override
    public void updatePosition(AbstractMissile missile) {
        long time = missile.getAge() / MvcGameConfig.MAGIC_TIME_CONST;
        Vector displacement = calculateDisplacement(missile.getInitAngle(), missile.getInitVelocity(), time);
        missile.move(displacement);
    }

    @Override
    public Vector calculateDisplacement(double angle, int power, double time) {
        int dX = (int) (power * time * Math.cos(angle));
        int dY = (int) (power * time * Math.sin(angle)
            + 0.5 * MvcGameConfig.GRAVITY * Math.pow(time, 2));
        return new Vector(dX, dY);
    }
    
    @Override
    public IMovingStrategy getNextStrategy(GameModel model) {
        return model.getNextMovingStrategy(this);
    }

     @Override 
    public String getName(){
        return "REAL";
    }
   
}
