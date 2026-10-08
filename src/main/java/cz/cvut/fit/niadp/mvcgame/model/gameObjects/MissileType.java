package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;

public enum MissileType {

    FIRST(MvcGameResources.MISSILE_RESOURCE,30,29);

    private MissileType(String imagePath, int width, int height){
        this.height = height;
        this.width = width;
        this.imagePath = imagePath;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    private int width;
    private int height;
    private String imagePath; 
}
