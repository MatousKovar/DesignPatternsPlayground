package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.GameObjectsFactoryA;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
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

        Level level = new LevelBuilder(factory).setEnemyCount(8).build();

        Assert.assertEquals(8, createdEnemies.size());
        Assert.assertEquals(8, level.getEnemySpawns().size());
        Assert.assertEquals(0, level.getEnemySpawns().get(0).spawnTimeMillis());
        for (int i = 0; i < createdEnemies.size(); i++) {
            EnemySpawn spawn = level.getEnemySpawns().get(i);
            Assert.assertSame(createdEnemies.get(i), spawn.enemy());
            if (i > 0) {
                long previousSpawnTime = level.getEnemySpawns().get(i - 1).spawnTimeMillis();
                Assert.assertTrue(
                    spawn.spawnTimeMillis() - previousSpawnTime >= MvcGameConfig.ENEMY_MIN_SPAWN_INTERVAL_MILLIS
                );
            }
        }
    }
}
