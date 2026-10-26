package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.Hitbox;

public class LevelBuilder {
    private static final int MAX_PLACEMENT_ATTEMPTS = 1_000;

    private final IGameObjectsFactory factory;

    private int enemyCount = MvcGameConfig.DEFAULT_ENEMY_COUNT;
    private int minX = MvcGameConfig.ENEMY_SPAWN_MIN_X;
    private int minY = MvcGameConfig.ENEMY_SPAWN_MIN_Y;
    private int maxX = MvcGameConfig.ENEMY_SPAWN_MAX_X;
    private int maxY = MvcGameConfig.ENEMY_SPAWN_MAX_Y;

    public LevelBuilder(IGameObjectsFactory factory) {
        this.factory = Objects.requireNonNull(factory, "Factory must not be null");
    }

    public LevelBuilder setEnemyCount(int count){
        this.enemyCount  = count;
        return this;
    }

    //todo v budoucnu se tady x  da MAX_X!
    public Level build() {
        List<EnemySpawn> spawns = new ArrayList<>(enemyCount);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < enemyCount; i++) {
            AbstractEnemy enemy = factory.createEnemy(new Position(minX, minY));
            placeEnemy(enemy, spawns, random);
            long spawnTimeMillis = i * MvcGameConfig.ENEMY_SPAWN_INTERVAL_MILLIS;
            spawns.add(new EnemySpawn(enemy, spawnTimeMillis));
        }
        return new Level(spawns);
    }

    private void placeEnemy(AbstractEnemy enemy, List<EnemySpawn> spawns, ThreadLocalRandom random) {
        Hitbox hitbox = enemy.getHitbox();
        int height = hitbox.getHeight();

        for (int attempt = 0; attempt < MAX_PLACEMENT_ATTEMPTS; attempt++) {
            int x = MvcGameConfig.MAX_X;
            int y = random.nextInt(minY, maxY - height + 1);
            enemy.getPosition().setX(x);
            enemy.getPosition().setY(y);
            if (!overlapsExistingEnemy(enemy, spawns)) {
                return;
            }
        }
        throw new IllegalStateException(
            "Cannot find space for enemy " + (spawns.size() + 1)
                + " after " + MAX_PLACEMENT_ATTEMPTS + " attempts"
        );
    }

    private boolean overlapsExistingEnemy(AbstractEnemy enemy, List<EnemySpawn> spawns) {
        for (EnemySpawn spawn : spawns) {
            if (enemy.getHitbox().overlaps(spawn.enemy().getHitbox())) {
                return true;
            }
        }
        return false;
    }
}
