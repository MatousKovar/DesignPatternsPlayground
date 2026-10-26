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

    
    public Level build() {
        List<EnemySpawn> spawns = new ArrayList<>(enemyCount);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < enemyCount; i++) {
            AbstractEnemy enemy = factory.createEnemy(new Position(minX, minY));
            long spawnTimeMillis = i * MvcGameConfig.ENEMY_SPAWN_INTERVAL_MILLIS;
            placeEnemy(enemy, spawnTimeMillis, spawns, random);
            spawns.add(new EnemySpawn(enemy, spawnTimeMillis));
        }
        return new Level(spawns);
    }

    private void placeEnemy(AbstractEnemy enemy, long spawnTimeMillis, List<EnemySpawn> spawns, ThreadLocalRandom random) {
        Hitbox hitbox = enemy.getHitbox();
        int height = hitbox.getHeight();

        for (int attempt = 0; attempt < MAX_PLACEMENT_ATTEMPTS; attempt++) {
            int x = MvcGameConfig.MAX_X;
            int y = random.nextInt(minY, maxY - height + 1);
            enemy.getPosition().setX(x);
            enemy.getPosition().setY(y);
            if (!overlapsExistingEnemy(enemy, spawnTimeMillis, spawns)) {
                return;
            }
        }
        throw new IllegalStateException(
            "Cannot find space for enemy " + (spawns.size() + 1)
                + " after " + MAX_PLACEMENT_ATTEMPTS + " attempts"
        );
    }

    private boolean overlapsExistingEnemy(AbstractEnemy enemy, long spawnTimeMillis, List<EnemySpawn> spawns) {
        for (EnemySpawn spawn : spawns) {
            double elapsedSeconds = (spawnTimeMillis - spawn.spawnTimeMillis()) / 1_000.0;
            Hitbox existingHitbox = spawn.enemy().getHitbox();
            Position predictedPosition = spawn.enemy().positionAtTime(
                MvcGameConfig.ENEMY_MOVE_SPEED, elapsedSeconds
            );
            Hitbox predictedHitbox = new Hitbox(
                predictedPosition,
                existingHitbox.getWidth(),
                existingHitbox.getHeight()
            );

            if (enemy.getHitbox().overlaps(predictedHitbox)) {
                return true;
            }
        }
        return false;
    }
}
