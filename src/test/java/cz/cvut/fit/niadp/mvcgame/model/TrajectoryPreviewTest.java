package cz.cvut.fit.niadp.mvcgame.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.MissileA;
import cz.cvut.fit.niadp.mvcgame.proxy.GameModelProxy;
import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;
import cz.cvut.fit.niadp.mvcgame.strategy.RealMovingStrategy;

public class TrajectoryPreviewTest {

    @Test
    public void previewThroughProxyMatchesRealMissileAtSampleTimes() {
        IGameModel model = new GameModelProxy(new GameModel());
        List<Position> points = model.getTrajectoryPreview();
        Assert.assertEquals(20, points.size());

        for (int i = 0; i < points.size(); i++) {
            long age = Math.round((i + 1) * 0.5 * MvcGameConfig.MAGIC_TIME_CONST);
            MissileA missile = new MissileA(
                new Position(model.getCannonPosition().getX(), model.getCannonPosition().getY()),
                MvcGameConfig.INIT_ANGLE, MvcGameConfig.INIT_POWER,
                new RealMovingStrategy(), MissileSkinType.FIRST
            ) {
                @Override
                public long getAge() {
                    return age;
                }
            };
            missile.move();

            Assert.assertEquals(missile.getPosition().getX(), points.get(i).getX());
            Assert.assertEquals(missile.getPosition().getY(), points.get(i).getY());
        }
        Assert.assertTrue(model.getMissiles().isEmpty());
        Assert.assertEquals(MvcGameConfig.CANNON_POS_X, model.getCannonPosition().getX());
        Assert.assertEquals(MvcGameConfig.CANNON_POS_Y, model.getCannonPosition().getY());
    }

    @Test
    public void previewRespondsToAimPowerAndCannonPositionWithoutSharingPositions() {
        GameModel model = new GameModel();
        List<Position> original = model.getTrajectoryPreview();
        model.aimCannonUp();
        List<Position> aimed = model.getTrajectoryPreview();
        Assert.assertTrue(aimed.get(19).getY() < original.get(19).getY());

        model.cannonPowerUp();
        List<Position> powered = model.getTrajectoryPreview();
        Assert.assertTrue(powered.get(19).getX() > aimed.get(19).getX());

        model.moveCannonDown();
        List<Position> moved = model.getTrajectoryPreview();
        Assert.assertEquals(powered.get(0).getY() + MvcGameConfig.MOVE_STEP, moved.get(0).getY());
        moved.get(0).setX(-1000);
        Assert.assertEquals(MvcGameConfig.CANNON_POS_X, model.getCannonPosition().getX());
        Assert.assertEquals(powered.get(0).getX(), model.getTrajectoryPreview().get(0).getX());
    }

    @Test
    public void previewIsEmptyForSimpleAndRandomMovement() {
        GameModel model = new GameModel();
        model.toggleMovingStrategy();
        Assert.assertTrue(model.getTrajectoryPreview().isEmpty());
        model.toggleMovingStrategy();
        Assert.assertTrue(model.getTrajectoryPreview().isEmpty());
        model.toggleMovingStrategy();
        Assert.assertEquals(20, model.getTrajectoryPreview().size());
    }
}
