package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

public abstract class AbstractEnemy extends GameObject {
    protected EnemyType type;
    protected Hitbox hitbox;
    
    public EnemyType getEnemyType(){
        return type;
    };
    
}
