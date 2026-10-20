package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;

public class Level {
    private final List<EnemySpawn> enemySpawns;
    private final List<EnemySpawn> pendingSpawns = new ArrayList<>();
    private Long startedAtNanos;

    public Level(List<EnemySpawn> enemySpawns) {
        this.enemySpawns = List.copyOf(enemySpawns);
    }

    public List<EnemySpawn> getEnemySpawns() {
        return enemySpawns;
    }

    public void start(long nowNanos) {
        startedAtNanos = nowNanos;
        pendingSpawns.clear();
        pendingSpawns.addAll(enemySpawns);
    }

    public List<AbstractEnemy> update(long nowNanos) {
        List<AbstractEnemy> spawnedEnemies = new ArrayList<>();
        if (startedAtNanos == null) {
            return spawnedEnemies;
        }

        long elapsedMillis = (nowNanos - startedAtNanos) / 1_000_000;
        Iterator<EnemySpawn> iterator = pendingSpawns.iterator();
        while (iterator.hasNext()) {
            EnemySpawn spawn = iterator.next();
            if (spawn.spawnTimeMillis() <= elapsedMillis) {
                spawn.enemy().spawn(nowNanos);
                spawnedEnemies.add(spawn.enemy());
                iterator.remove();
            }
        }
        return spawnedEnemies;
    }
}
