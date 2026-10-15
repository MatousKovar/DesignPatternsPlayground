package cz.cvut.fit.niadp.mvcgame.strategy;

import java.util.Random;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public class RandomMovingStrategy implements IMovingStrategy {

    private final Random random;

    public RandomMovingStrategy(){
        this.random = new Random();
    }

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
        return new Position(
            random.nextInt(-MvcGameConfig.MOVE_STEP, 2 * MvcGameConfig.MOVE_STEP),
            random.nextInt(-MvcGameConfig.MOVE_STEP, MvcGameConfig.MOVE_STEP)
        );
    }

    @Override
    public IMovingStrategy getNextStrategy(GameModel model) {
        return model.getNextMovingStrategy(this);
    }

    @Override 
    public String getName(){
        return "RANDOM";
    }
   
}
