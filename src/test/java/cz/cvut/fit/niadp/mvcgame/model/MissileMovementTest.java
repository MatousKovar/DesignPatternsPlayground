package cz.cvut.fit.niadp.mvcgame.model;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.MissileA;
import cz.cvut.fit.niadp.mvcgame.strategy.RealMovingStrategy;

public class MissileMovementTest {

    @Test
    public void repeatedDisplacementKeepsPositionAndHitboxAtSameCoordinates() {
        MissileA missile = new MissileA(
            new Position(50, 100), 0, 10, new RealMovingStrategy(), MissileSkinType.FIRST
        );
        Position originalPosition = missile.getPosition();

        missile.moveFromStart(new Position(20, -30));
        missile.moveFromStart(new Position(20, -30));

        Assert.assertSame(originalPosition, missile.getPosition());
        Assert.assertEquals(70, missile.getPosition().getX());
        Assert.assertEquals(70, missile.getPosition().getY());
        Assert.assertEquals(70, missile.getHitbox().getX());
        Assert.assertEquals(70, missile.getHitbox().getY());

        missile.moveFromStart(new Position(0, 0));

        Assert.assertEquals(50, missile.getPosition().getX());
        Assert.assertEquals(100, missile.getPosition().getY());
    }

    @Test
    public void realMovementUsesFractionalTimeAndDoesNotAccumulateAtSameAge() {
        MissileA missile = new MissileA(
            new Position(50, 100), 0, 20, new RealMovingStrategy(), MissileSkinType.FIRST
        ) {
            @Override
            public long getAge() {
                return MvcGameConfig.MAGIC_TIME_CONST / 2;
            }
        };

        missile.move();
        missile.move();

        Assert.assertEquals(60, missile.getPosition().getX());
        Assert.assertEquals(101, missile.getPosition().getY());
        Assert.assertEquals(60, missile.getHitbox().getX());
        Assert.assertEquals(101, missile.getHitbox().getY());
    }
}
