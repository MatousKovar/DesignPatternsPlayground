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
    private Difficulty difficulty = Difficulty.MEDIUM;
    private int minX = MvcGameConfig.ENEMY_SPAWN_MIN_X;
    private int minY = MvcGameConfig.ENEMY_SPAWN_MIN_Y;
    private int maxY = MvcGameConfig.ENEMY_SPAWN_MAX_Y;

    public LevelBuilder(IGameObjectsFactory factory) {
        this.factory = Objects.requireNonNull(factory, "Factory must not be null");
    }

    public LevelBuilder setEnemyCount(int count){
        this.enemyCount  = count;
        return this;
    }

    public LevelBuilder setDifficulty(Difficulty difficulty) {
        this.difficulty = Objects.requireNonNull(difficulty, "Difficulty must not be null");
        return this;
    }

    
    public Level build() {
        int levelEnemyCount = Math.max(1, (int) Math.round(enemyCount * difficulty.getEnemyCountMultiplier()));
        List<EnemySpawn> spawns = new ArrayList<>(levelEnemyCount);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long spawnTimeMillis = 0;
        for (int i = 0; i < levelEnemyCount; i++) {
            if (i > 0) {
                spawnTimeMillis += nextSpawnIntervalMillis(random);
            }
            AbstractEnemy enemy = factory.createEnemy(new Position(minX, minY));
            placeEnemy(enemy, spawnTimeMillis, spawns, random);
            spawns.add(new EnemySpawn(enemy, spawnTimeMillis));
        }
        return new Level(spawns, getEnemySpeed());
    }

    private long nextSpawnIntervalMillis(ThreadLocalRandom random) {
        double intervalMillis = MvcGameConfig.ENEMY_SPAWN_INTERVAL_MILLIS
            + random.nextGaussian() * MvcGameConfig.ENEMY_SPAWN_INTERVAL_STDDEV_MILLIS;
        double multiplier = difficulty.getSpawnIntervalMultiplier();
        long minimumIntervalMillis = Math.max(1, Math.round(MvcGameConfig.ENEMY_MIN_SPAWN_INTERVAL_MILLIS * multiplier));
        return Math.max(minimumIntervalMillis, Math.round(intervalMillis * multiplier));
    }

    private double getEnemySpeed() {
        return MvcGameConfig.ENEMY_MOVE_SPEED * difficulty.getEnemySpeedMultiplier();
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
                getEnemySpeed(), elapsedSeconds
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
