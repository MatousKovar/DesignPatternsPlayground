package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;

public class Level {
    private record SpawnMemento(EnemySpawn spawn, Object enemyMemento) {}

    private record Memento(Long elapsedNanos, List<SpawnMemento> pendingSpawns) {}

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

    public Object createMemento(long nowNanos) {
        Long elapsedNanos = null;
        List<EnemySpawn> spawnsToSave = enemySpawns;
        if (startedAtNanos != null) {
            elapsedNanos = nowNanos - startedAtNanos;
            spawnsToSave = pendingSpawns;
        }

        List<SpawnMemento> savedSpawns = new ArrayList<>();
        for (EnemySpawn spawn : spawnsToSave) {
            savedSpawns.add(new SpawnMemento(spawn, spawn.enemy().createMemento()));
        }

        return new Memento(elapsedNanos, List.copyOf(savedSpawns));
    }

    public void restoreMemento(Object memento, long nowNanos) {
        Memento snapshot = (Memento) memento;
        startedAtNanos = null;
        if (snapshot.elapsedNanos() != null) {
            startedAtNanos = nowNanos - snapshot.elapsedNanos();
        }

        pendingSpawns.clear();
        for (SpawnMemento savedSpawn : snapshot.pendingSpawns()) {
            EnemySpawn spawn = savedSpawn.spawn();
            spawn.enemy().restoreMemento(savedSpawn.enemyMemento());
            pendingSpawns.add(spawn);
        }
    }
}
