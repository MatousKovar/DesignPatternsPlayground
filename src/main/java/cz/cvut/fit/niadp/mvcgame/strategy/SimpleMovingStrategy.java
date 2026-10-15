package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public class SimpleMovingStrategy implements IMovingStrategy{

    @Override
    public void updatePosition(AbstractMissile missile) {
        long time = missile.getAge() / MvcGameConfig.MAGIC_TIME_CONST;
        Position displacement = calculateDisplacement(missile.getInitAngle(), missile.getInitVelocity(), time);
        Position position = missile.getPosition();
        position.setX(position.getX() + displacement.getX());
        position.setY(position.getY() + displacement.getY());
    }

    @Override
    public Position calculateDisplacement(double angle, int power, double time) {
        return new Position(MvcGameConfig.MOVE_STEP, 0);
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
