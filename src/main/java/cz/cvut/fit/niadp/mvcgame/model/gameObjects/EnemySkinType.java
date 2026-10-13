package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;

public enum EnemySkinType implements IGameObjectSkinType{

    FIRST(MvcGameResources.ENEMY1_IMAGE,30,29),
    SECOND(MvcGameResources.ENEMY2_IMAGE,30,26),
    THIRD(MvcGameResources.ENEMY_KING_IMAGE,90,100);




    private final String imagePath;
    private final int hitboxWidth;
    private final int hitboxHeight;

    EnemySkinType(String imagePath, int hitboxWidth, int hitboxHeight) {
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
