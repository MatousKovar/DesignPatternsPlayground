package cz.cvut.fit.niadp.mvcgame.visitor;

import cz.cvut.fit.niadp.mvcgame.model.GameInfo;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractImpactObject;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;

public interface IVisitor {
    public void visit(AbstractCannon cannon);
    public void visit(AbstractMissile missile);
    public void visit(AbstractEnemy enemy); 
    public void visit(GameInfo info);
    void visit(AbstractImpactObject impact);
}
