package cz.cvut.fit.niadp.mvcgame.abstractFactory;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractImpactObject;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.CannonA;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.EnemyA;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.ImpactA;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.MissileA;
import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;
import cz.cvut.fit.niadp.mvcgame.strategy.IEnemyMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.strategy.MoveLeftEnemyStrategy;

public class GameObjectsFactoryA implements IGameObjectsFactory{

    protected final IGameModel model;
    private final IEnemyMovingStrategy enemyMovingStrategy = new MoveLeftEnemyStrategy();

    public GameObjectsFactoryA(IGameModel model){
        this.model = model;
    }

    @Override
    public CannonA createCannon() {
        return new CannonA(new Position(MvcGameConfig.CANNON_POS_X, MvcGameConfig.CANNON_POS_Y), this);
    }

    @Override
    public MissileA createMissile(double initAngle, int initVelocity) {
        return new MissileA(
            new Position(model.getCannonPosition().getX(), model.getCannonPosition().getY()),
            initAngle,
            initVelocity,
            model.getMovingStrategy(),
            MissileSkinType.FIRST
        );
    }

    @Override
    public EnemyA createEnemy(Position position){
        EnemyA enemy = new EnemyA(position);
        enemy.setMovingStrategy(enemyMovingStrategy);
        return enemy;
    }

    @Override
    public AbstractImpactObject createImpact(Position position, int width, int height) {
        return new ImpactA(new Position(position.getX(), position.getY()),width,height);
    }


}
