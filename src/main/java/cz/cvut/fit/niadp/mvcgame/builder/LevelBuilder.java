package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.Hitbox;

public class LevelBuilder {
    private static final int MAX_PLACEMENT_ATTEMPTS = 1_000;

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
            AbstractEnemy enemy = factory.createEnemy(new Position(minX, minY));
            placeEnemy(enemy, spawns, random);
            spawns.add(new EnemySpawn(enemy, 0));
        }
        return new Level(spawns);
    }

    private void placeEnemy(AbstractEnemy enemy, List<EnemySpawn> spawns, ThreadLocalRandom random) {
        Hitbox hitbox = enemy.getHitbox();
        int width = hitbox.getWidth();
        int height = hitbox.getHeight();
        if (width <= 0 || height <= 0 || width > maxX - minX || height > maxY - minY) {
            throw new IllegalStateException("Enemy hitbox does not fit inside the spawn area");
        }

        for (int attempt = 0; attempt < MAX_PLACEMENT_ATTEMPTS; attempt++) {
            int x = random.nextInt(minX, maxX - width + 1);
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
