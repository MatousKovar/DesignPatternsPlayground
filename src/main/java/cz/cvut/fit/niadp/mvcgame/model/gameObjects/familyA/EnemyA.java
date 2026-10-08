package cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA;

import java.util.concurrent.ThreadLocalRandom;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.EnemySkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.Hitbox;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;

public class EnemyA extends AbstractEnemy {

    private IGameObjectsFactory gameFactory; // Todo - enemy kdyz umre tak bude na svem miste vytvaret nejaky time limited object

    public EnemyA (Position position, IGameObjectsFactory gameFactory){
        this.position = position;
        this.gameFactory = gameFactory;
        
        //random skin on creatiPon - to by melo resit mozna neco jineho nez enemy ten sking???
        EnemySkinType[] types = EnemySkinType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(types.length);
        this.type = types[randomIndex];
        this.hitbox = new Hitbox(position, type.getHitboxWidth(), type.getHitboxHeight());
    }

    @Override 
    public void accept(IVisitor visitor){
        visitor.visit(this);
    }
}
