package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

public abstract class AbstractEnemy extends GameObject {
    protected EnemySkinType type;
    protected Hitbox hitbox;
    
    public EnemySkinType getEnemySkinType(){
        return type;
    };

    public Hitbox getHitbox(){
        return this.hitbox;
    }

    public abstract int getScoreForKill();
    
}
