package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

public abstract class AbstractEnemy extends GameObject {
    protected EnemySkin skin;
    
    public EnemySkin getEnemySkin(){
        return skin;
    };
    
}
