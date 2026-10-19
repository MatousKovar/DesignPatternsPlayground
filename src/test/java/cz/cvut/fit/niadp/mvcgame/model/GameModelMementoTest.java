package cz.cvut.fit.niadp.mvcgame.model;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.command.CannonShootCommand;
import cz.cvut.fit.niadp.mvcgame.command.MoveCannonDownCommand;
import cz.cvut.fit.niadp.mvcgame.command.MoveCannonUpCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractImpactObject;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.ImpactA;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.MissileA;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.strategy.RealMovingStrategy;
import mockit.Mocked;
import mockit.Verifications;

public class GameModelMementoTest {

    @Test
    public void snapshotRestoresMissilePositionHitboxAndAgeRepeatedly() {
        GameModel model = new GameModel();
        AgedMissile missile = new AgedMissile();
        model.getMissiles().add(missile);
        missile.setAgeMillis(250);
        missile.moveFromStart(new Position(20, -30));
        Object snapshot = model.createMemento();

        for (int i = 0; i < 2; i++) {
            missile.moveFromStart(new Position(200, 100));
            missile.advanceTimeMillis(10_000);
            model.getMissiles().clear();
            model.setMemento(snapshot);

            Assert.assertTrue(model.getMissiles().contains(missile));
            Assert.assertEquals(70, missile.getPosition().getX());
            Assert.assertEquals(70, missile.getPosition().getY());
            Assert.assertEquals(70, missile.getHitbox().getX());
            Assert.assertEquals(70, missile.getHitbox().getY());
            Assert.assertEquals(250, missile.getAge());
        }
    }

    @Test
    public void snapshotsKeepIndependentMissilePositions() {
        GameModel model = new GameModel();
        model.cannonShoot();
        AbstractMissile missile = model.getMissiles().iterator().next();
        missile.moveFromStart(new Position(20, 0));
        Object first = model.createMemento();
        missile.moveFromStart(new Position(40, 0));
        Object second = model.createMemento();

        model.setMemento(first);
        Assert.assertEquals(MvcGameConfig.CANNON_POS_X + 20, missile.getPosition().getX());
        model.setMemento(second);
        Assert.assertEquals(MvcGameConfig.CANNON_POS_X + 40, missile.getPosition().getX());
        missile.moveFromStart(new Position(0, 0));
        Assert.assertEquals(MvcGameConfig.CANNON_POS_X, missile.getPosition().getX());
    }

    @Test
    public void snapshotsRestoreEnemyAndExplosionMembershipPositionAndScore() {
        GameModel model = new GameModel();
        model.toggleMovingStrategy();
        model.spawnNewEnemy();
        AbstractEnemy enemy = model.getGameObjects().stream()
            .filter(AbstractEnemy.class::isInstance).map(AbstractEnemy.class::cast)
            .findFirst().orElseThrow();
        enemy.getPosition().setX(200);
        enemy.getPosition().setY(200);
        Object beforeHit = model.createMemento();
        model.getMissiles().add(new MissileA(
            new Position(200 - MvcGameConfig.MOVE_STEP, 200), 0, 0,
            model.getMovingStrategy(), MissileSkinType.FIRST
        ));
        model.update();
        AbstractImpactObject impact = model.getGameObjects().stream()
            .filter(AbstractImpactObject.class::isInstance).map(AbstractImpactObject.class::cast)
            .findFirst().orElseThrow();
        Object afterHit = model.createMemento();
        impact.getPosition().setX(500);
        enemy.getPosition().setX(600);

        model.setMemento(beforeHit);
        Assert.assertTrue(model.getGameObjects().contains(enemy));
        Assert.assertFalse(model.getGameObjects().contains(impact));
        Assert.assertEquals(200, enemy.getPosition().getX());
        Assert.assertEquals(200, enemy.getHitbox().getX());
        Assert.assertEquals(0, model.getGameInfo().getScore());

        model.setMemento(afterHit);
        Assert.assertFalse(model.getGameObjects().contains(enemy));
        Assert.assertTrue(model.getGameObjects().contains(impact));
        Assert.assertEquals(200, impact.getPosition().getX());
        Assert.assertEquals(enemy.getScoreForKill(), model.getGameInfo().getScore());
    }

    @Test
    public void explosionRestoresItsAnimationAge() {
        AgedImpact impact = new AgedImpact();
        impact.setAgeMillis(100);
        Object snapshot = impact.createMemento();
        impact.advanceTimeMillis(10_000);
        Assert.assertTrue(impact.isExpired());

        impact.restoreMemento(snapshot);

        Assert.assertFalse(impact.isExpired());
        Assert.assertEquals(100, impact.getAge());
    }

    @Test
    public void restoreReplaysRandomMovementOfMissileWithDifferentActiveStrategy() {
        GameModel model = new GameModel();
        model.toggleMovingStrategy();
        model.toggleMovingStrategy();
        model.cannonShoot();
        AbstractMissile missile = model.getMissiles().iterator().next();
        missile.getPosition().setX(500);
        missile.getPosition().setY(500);
        model.toggleMovingStrategy();
        Object snapshot = model.createMemento();

        Position[] expected = new Position[10];
        for (int i = 0; i < expected.length; i++) {
            missile.move();
            expected[i] = new Position(missile.getPosition().getX(), missile.getPosition().getY());
        }
        model.setMemento(snapshot);
        for (Position point : expected) {
            missile.move();
            Assert.assertEquals(point.getX(), missile.getPosition().getX());
            Assert.assertEquals(point.getY(), missile.getPosition().getY());
        }
    }

    @Test
    public void restoreReturnsCommandHistoryAndDiscardsPendingCommands() {
        GameModel model = new GameModel();
        model.registerCommand(new MoveCannonUpCommand(model));
        model.update();
        Object snapshot = model.createMemento();
        model.registerCommand(new MoveCannonDownCommand(model));
        model.update();
        model.registerCommand(new CannonShootCommand(model));

        model.setMemento(snapshot);
        model.update();
        Assert.assertTrue(model.getMissiles().isEmpty());
        model.undoLastCommand();
        Assert.assertEquals(MvcGameConfig.CANNON_POS_Y, model.getCannonPosition().getY());
    }

    @Test
    public void restoreReturnsCannonSettingsAndActiveModes() {
        GameModel model = new GameModel();
        model.moveCannonUp();
        model.aimCannonUp();
        model.cannonPowerUp();
        model.toggleShootingMode();
        model.toggleMovingStrategy();
        double angle = model.getGameInfo().getCannonAngle();
        double power = model.getGameInfo().getCannonPower();
        String shootingMode = model.getGameInfo().getShootingMode();
        String movingStrategy = model.getGameInfo().getMovingStrategy();
        Object snapshot = model.createMemento();
        model.moveCannonDown();
        model.aimCannonDown();
        model.cannonPowerDown();
        model.toggleShootingMode();
        model.toggleMovingStrategy();

        model.setMemento(snapshot);

        Assert.assertEquals(MvcGameConfig.CANNON_POS_Y - MvcGameConfig.MOVE_STEP, model.getCannonPosition().getY());
        Assert.assertEquals(angle, model.getGameInfo().getCannonAngle(), 0.0);
        Assert.assertEquals(power, model.getGameInfo().getCannonPower(), 0.0);
        Assert.assertEquals(shootingMode, model.getGameInfo().getShootingMode());
        Assert.assertEquals(movingStrategy, model.getGameInfo().getMovingStrategy());
    }

    @Test
    public void restoreNotifiesRegisteredObserver(@Mocked IObserver observer) {
        GameModel model = new GameModel();
        Object snapshot = model.createMemento();
        model.moveCannonDown();
        model.registerObserver(observer);

        model.setMemento(snapshot);

        Assert.assertEquals(MvcGameConfig.CANNON_POS_Y, model.getCannonPosition().getY());
        new Verifications() {{
            observer.update(); times = 1;
        }};
    }

    private static class AgedMissile extends MissileA {
        private LocalDateTime testTime = LocalDateTime.of(2026, 1, 1, 0, 0);

        AgedMissile() {
            super(new Position(50, 100), 0, 10, new RealMovingStrategy(), MissileSkinType.FIRST);
        }

        void setAgeMillis(long age) {
            bornAt = testTime.minusNanos(age * 1_000_000);
        }

        void advanceTimeMillis(long elapsed) {
            testTime = testTime.plusNanos(elapsed * 1_000_000);
        }

        @Override
        protected LocalDateTime now() {
            return testTime;
        }
    }

    private static class AgedImpact extends ImpactA {
        private LocalDateTime testTime = LocalDateTime.of(2026, 1, 1, 0, 0);

        AgedImpact() {
            super(new Position(200, 200), 30, 30);
        }

        void setAgeMillis(long age) {
            bornAt = testTime.minusNanos(age * 1_000_000);
        }

        void advanceTimeMillis(long elapsed) {
            testTime = testTime.plusNanos(elapsed * 1_000_000);
        }

        @Override
        protected LocalDateTime now() {
            return testTime;
        }
    }
}
