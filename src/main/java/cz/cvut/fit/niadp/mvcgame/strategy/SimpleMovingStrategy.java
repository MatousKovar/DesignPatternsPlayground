package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public class SimpleMovingStrategy implements IMovingStrategy{

    @Override
    public void updatePosition(AbstractMissile missile) {
        long time = missile.getAge() / MvcGameConfig.MAGIC_TIME_CONST;
        Vector displacement = calculateDisplacement(missile.getInitAngle(), missile.getInitVelocity(), time);
        missile.move(displacement);
    }

    @Override
    public Vector calculateDisplacement(double angle, int power, double time) {
        return new Vector(MvcGameConfig.MOVE_STEP, 0);
    }

    @Override
    public IMovingStrategy getNextStrategy(GameModel model) {
        return model.getNextMovingStrategy(this);
    }

     @Override 
    public String getName(){
        return "SIMPLE";
    }
   
}
