package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;

public enum MissileSkinType {

    FIRST(MvcGameResources.MISSILE_RESOURCE,30,29);
    
    private final int width;
    private final int height;
    private final String imagePath; 

    private MissileSkinType(String imagePath, int width, int height){
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

   
}
