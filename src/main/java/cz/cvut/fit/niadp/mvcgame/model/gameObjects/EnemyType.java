package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;

public enum EnemyType {

    FIRST(MvcGameResources.ENEMY1_IMAGE,30,29),
    SECOND(MvcGameResources.ENEMY2_IMAGE,30,26);

    private final String imagePath;
    private final int hitboxWidth;
    private final int hitboxHeight;

    EnemyType(String imagePath, int hitboxWidth, int hitboxHeight) {
        this.imagePath = imagePath;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getHitboxWidth() {
        return hitboxWidth;
    }

    public int getHitboxHeight() {
        return hitboxHeight;
    }
}
