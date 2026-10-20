package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public interface IEnemyMovingStrategy {
    Position calculatePosition(Position initialPosition, double speed, double elapsedSeconds);
}
