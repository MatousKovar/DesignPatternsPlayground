package cz.cvut.fit.niadp.mvcgame.decorator;

import java.util.HashSet;
import java.util.Set;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.Hitbox;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;

public class ExplosiveMissileDecorator extends MissileDecorator{

    public ExplosiveMissileDecorator(AbstractMissile missile) {
        super(missile);
    }

    @Override
    public Set<AbstractEnemy> getAffectedEnemies(AbstractEnemy hitEnemy, Set<AbstractEnemy> enemies) {
        //important initialization: for keeping the effects of the wrapped class, not only the explosive missile
        Set<AbstractEnemy> result = new HashSet<AbstractEnemy>(super.getAffectedEnemies(hitEnemy, enemies)); 
        for (AbstractEnemy enemy : enemies){
            if(calculateDistance(hitEnemy.getHitbox(), enemy.getHitbox()) <= MvcGameConfig.EXPLOSION_RADIUS){
                result.add(enemy);
            }
        }
        return result;
    }

    @Override
    public MissileSkinType getMissileSkinType() {
        return MissileSkinType.EXPLOSIVE;
    }

    private double calculateDistance(Hitbox a, Hitbox b){
        double centerAX = a.getX() + a.getWidth() / 2.0;
        double centerAY = a.getY() + a.getHeight() / 2.0;
        double centerBX = b.getX() + b.getWidth() / 2.0;
        double centerBY = b.getY() + b.getHeight() / 2.0;
        return Math.hypot(centerAX - centerBX, centerAY - centerBY);
    }

    
}
