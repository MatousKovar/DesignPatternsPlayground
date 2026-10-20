package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.GameObjectsFactoryA;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.Hitbox;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.EnemyA;

public class LevelBuilderTest {

    @Test
    public void buildKeepsEnemiesCreatedBySuppliedFactory() {
        List<AbstractEnemy> createdEnemies = new ArrayList<>();
        GameObjectsFactoryA factory = new GameObjectsFactoryA(new GameModel()) {
            @Override
            public EnemyA createEnemy(Position position) {
                EnemyA enemy = super.createEnemy(position);
                createdEnemies.add(enemy);
                return enemy;
            }
        };

        Level level = new LevelBuilder(factory).enemyCount(8).build();

        Assert.assertEquals(8, createdEnemies.size());
        Assert.assertEquals(8, level.getEnemySpawns().size());
        for (int i = 0; i < createdEnemies.size(); i++) {
            EnemySpawn spawn = level.getEnemySpawns().get(i);
            Assert.assertSame(createdEnemies.get(i), spawn.enemy());
            Assert.assertEquals(0, spawn.spawnTimeMillis());
        }
    }
}
