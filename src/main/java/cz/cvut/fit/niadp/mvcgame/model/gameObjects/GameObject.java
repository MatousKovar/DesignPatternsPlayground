package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitable;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public abstract class GameObject implements IVisitable {
    private record Memento(int x, int y) {}

    protected Position position;
    
    public abstract void accept(IVisitor visitor);

    public void move(Vector vector){
        position.add(vector);
    }

    public Position getPosition(){
        return position;
    }

    public Object createMemento() {
        return new Memento(position.getX(), position.getY());
    }

    public void restoreMemento(Object memento) {
        Memento snapshot = (Memento) memento;
        position.setX(snapshot.x());
        position.setY(snapshot.y());
    }

}
