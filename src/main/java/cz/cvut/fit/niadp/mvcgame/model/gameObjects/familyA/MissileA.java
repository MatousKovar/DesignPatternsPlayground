package cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA;

import java.util.HashSet;
import java.util.Set;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

public class MissileA extends AbstractMissile { 

    public MissileA(Position initPosition, double initAngle, int initVelocity, IMovingStrategy movingStrategy, MissileSkinType type) {
        super(initPosition, initAngle, initVelocity, movingStrategy,type);
    }

    @Override
    public Set<AbstractEnemy> getAffectedEnemies(AbstractEnemy hitEnemy, Set<AbstractEnemy> enemies) {
        HashSet<AbstractEnemy> result = new HashSet<AbstractEnemy>();
        result.add(hitEnemy);
        return result;
    }
}
