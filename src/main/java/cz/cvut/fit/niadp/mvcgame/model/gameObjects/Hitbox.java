package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public class Hitbox {
    private Position position;
    private int width;
    private int height;

    public Hitbox(Position position, int width, int height){
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public int getX(){
        return position.getX();
    }

    public int getY(){
        return position.getY();
    }

    public boolean overlaps(Hitbox other){
        return getX() < other.getX() + other.width
            && getX() + width > other.getX()
            && getY() < other.getY() + other.height
            && getY() + height > other.getY();
    }
    
}
