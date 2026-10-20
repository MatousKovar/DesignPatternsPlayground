package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.List;

public class Level {
    private final List<EnemySpawn> enemySpawns;

    public Level(List<EnemySpawn> enemySpawns) {
        this.enemySpawns = List.copyOf(enemySpawns);
    }

    public List<EnemySpawn> getEnemySpawns() {
        return enemySpawns;
    }
}
