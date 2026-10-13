package cz.cvut.fit.niadp.mvcgame.model;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.command.MoveCannonUpCommand;
import cz.cvut.fit.niadp.mvcgame.command.CannonShootCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.MissileA;
import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;
import cz.cvut.fit.niadp.mvcgame.strategy.SimpleMovingStrategy;

public class GameModelBasicTest {

    @Test
    public void undoLastCommandTest(){
        IGameModel gameModel = new GameModel();
        int beginCannonPositionX = gameModel.getCannonPosition().getX();
        int beginCannonPositionY = gameModel.getCannonPosition().getY();
        gameModel.registerCommand(new MoveCannonUpCommand(gameModel));
        gameModel.update();
        Assert.assertEquals(beginCannonPositionX, gameModel.getCannonPosition().getX());
        Assert.assertEquals(beginCannonPositionY - MvcGameConfig.MOVE_STEP, gameModel.getCannonPosition().getY());
        gameModel.undoLastCommand();
        Assert.assertEquals(beginCannonPositionX, gameModel.getCannonPosition().getX());
        Assert.assertEquals(beginCannonPositionY, gameModel.getCannonPosition().getY());
    }

    @Test
    public void restoreMementoRestoresScoreTest() {
        GameModel gameModel = new GameModel();
        Object memento = gameModel.createMemento();

        gameModel.getGameInfo().increaseScore(10);
        gameModel.setMemento(memento);

        Assert.assertEquals(0, gameModel.getGameInfo().getScore());
    }

    @Test
    public void missileHitRemovesEnemyAndAwardsScoreTest() {
        GameModel gameModel = new GameModel();
        AbstractEnemy enemy = spawnEnemyAt(gameModel, 200, 200);
        MissileA missile = new MissileA(
            new Position(enemy.getPosition().getX() - MvcGameConfig.MOVE_STEP, enemy.getPosition().getY()),
            0,
            0,
            new SimpleMovingStrategy(),
            MissileSkinType.FIRST
        );
        gameModel.getMissiles().add(missile);

        gameModel.update();

        Assert.assertFalse(gameModel.getGameObjects().contains(enemy));
        Assert.assertFalse(gameModel.getMissiles().contains(missile));
        Assert.assertEquals(10, gameModel.getGameInfo().getScore());
    }

    @Test
    public void missileThatMissesDoesNotRemoveEnemyOrAwardScoreTest() {
        GameModel gameModel = new GameModel();
        AbstractEnemy enemy = spawnEnemyAt(gameModel, 200, 200);
        MissileA missile = new MissileA(
            new Position(enemy.getPosition().getX() - 100, enemy.getPosition().getY()),
            0,
            0,
            new SimpleMovingStrategy(),
            MissileSkinType.FIRST
        );
        gameModel.getMissiles().add(missile);

        gameModel.update();

        Assert.assertTrue(gameModel.getGameObjects().contains(enemy));
        Assert.assertTrue(gameModel.getMissiles().contains(missile));
        Assert.assertEquals(0, gameModel.getGameInfo().getScore());
    }

    @Test
    public void undoShotThatKillsEnemyRestoresEnemyAndScoreTest() {
        GameModel gameModel = new GameModel();
        gameModel.toggleMovingStrategy(); // use deterministic, constant-step missile movement
        AbstractEnemy enemy = spawnEnemyAt(
            gameModel,
            MvcGameConfig.CANNON_POS_X + 150,
            MvcGameConfig.CANNON_POS_Y
        );

        gameModel.registerCommand(new CannonShootCommand(gameModel));
        gameModel.update();
        for (int i = 0; i < 20 && gameModel.getGameObjects().contains(enemy); i++) {
            gameModel.update();
        }

        Assert.assertFalse("The shot should kill the enemy", gameModel.getGameObjects().contains(enemy));
        Assert.assertEquals(10, gameModel.getGameInfo().getScore());

        gameModel.undoLastCommand();

        Assert.assertTrue("Undo should restore the enemy", gameModel.getGameObjects().contains(enemy));
        Assert.assertEquals("Undo should restore the previous score", 0, gameModel.getGameInfo().getScore());
    }

    private AbstractEnemy spawnEnemyAt(GameModel gameModel, int x, int y) {
        gameModel.spawnNewEnemy();
        AbstractEnemy enemy = gameModel.getGameObjects().stream()
            .filter(AbstractEnemy.class::isInstance)
            .map(AbstractEnemy.class::cast)
            .findFirst()
            .orElseThrow();
        enemy.getPosition().setX(x);
        enemy.getPosition().setY(y);
        return enemy;
    }

    
}
