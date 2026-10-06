package cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA;

import java.util.concurrent.ThreadLocalRandom;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.EnemySkin;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;

public class EnemyA extends AbstractEnemy {

    private IGameObjectsFactory gameFactory; // Todo - enemy kdyz umre tak bude na svem miste vytvaret nejaky time limited object

    public EnemyA (Position position, IGameObjectsFactory gameFactory){
        this.position = position;
        this.gameFactory = gameFactory;

        //random skin on creation
        EnemySkin[] skins = EnemySkin.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(skins.length);
        this.skin = skins[randomIndex];
    }

    @Override 
    public void accept(IVisitor visitor){
        visitor.visit(this);
    }
}
