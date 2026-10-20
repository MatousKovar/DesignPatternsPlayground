package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LevelBuilder {
    private int enemyCount = 5;
    private int minX = 600;
    private int minY = 150;
    private int maxX = 1300;
    private int maxY = 800;

    public LevelBuilder enemyCount(int count){
        if (count <= 0)
            throw new IllegalArgumentException("pocet nepratel musi byt kladne cislo");

        this.enemyCount  = count;
        return this;
    }

    public Level build() {
        List<EnemySpawn> spawns = new ArrayList<>(enemyCount);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < enemyCount; i++) {
            int x = random.nextInt(minX, maxX);
            int y = random.nextInt(minY, maxY);
            spawns.add(new EnemySpawn(x, y, 0));
        }
        return new Level(spawns);
    }
}
