package cz.cvut.fit.niadp.mvcgame.builder;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;

public record EnemySpawn(AbstractEnemy enemy, long spawnTimeMillis) {}
