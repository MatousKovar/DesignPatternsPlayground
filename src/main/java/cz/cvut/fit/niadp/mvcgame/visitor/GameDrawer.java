package cz.cvut.fit.niadp.mvcgame.visitor;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.GameObject;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.EnemySkin;

public class GameDrawer implements IVisitor{
    protected IGameGraphics gameGraphics;

    public void setGraphicsContext(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
    }

    @Override
    public void visit(AbstractCannon cannon) {
        drawGameObject(cannon, MvcGameResources.CANNON_RESOURCE);
    }

    @Override
    public void visit(AbstractMissile missile) {
        drawGameObject(missile, MvcGameResources.MISSILE_RESOURCE);
    }

    @Override 
    public void visit(AbstractEnemy enemy){
        if (enemy.getEnemySkin() == EnemySkin.FIRST)
            drawGameObject(enemy, MvcGameResources.ENEMY1_IMAGE);
        else 
            drawGameObject(enemy, MvcGameResources.ENEMY2_IMAGE);
    }

    protected void drawGameObject(GameObject gameObject, String resource){
        if(gameGraphics != null)
            gameGraphics.drawImage(resource, gameObject.getPosition());
    }

}

