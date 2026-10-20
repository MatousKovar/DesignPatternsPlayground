package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public class MoveLeftEnemyStrategy implements IEnemyMovingStrategy {

    @Override
    public Position calculatePosition(Position initialPosition, double speed, double elapsedSeconds) {
        int x = (int) Math.round(initialPosition.getX() - speed * elapsedSeconds);
        return new Position(x, initialPosition.getY());
    }
}
