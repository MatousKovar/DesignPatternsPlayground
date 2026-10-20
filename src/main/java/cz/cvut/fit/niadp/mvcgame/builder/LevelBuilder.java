package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;

public class LevelBuilder {
    private final IGameObjectsFactory factory;

    private int enemyCount = 5;
    private int minX = 600;
    private int minY = 150;
    private int maxX = 1300;
    private int maxY = 800;

    public LevelBuilder(IGameObjectsFactory factory) {
        this.factory = Objects.requireNonNull(factory, "Factory must not be null");
    }

    public LevelBuilder enemyCount(int count){
        if (count <= 0)
            throw new IllegalArgumentException("pocet nepratel musi byt kladne cislo");

        this.enemyCount  = count;
        return this;
    }

    //todo v budoucnu se tady x  da MAX_X!
    public Level build() {
        List<EnemySpawn> spawns = new ArrayList<>(enemyCount);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < enemyCount; i++) {
            int x = random.nextInt(minX, maxX);
            int y = random.nextInt(minY, maxY);
            AbstractEnemy enemy = factory.createEnemy(new Position(x, y));
            spawns.add(new EnemySpawn(enemy, 0));
        }
        return new Level(spawns);
    }
}
