package cz.cvut.fit.niadp.mvcgame.builder;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public class Level {
    private final List<Position> enemyPositions;

    public Level(List<Position> positions){
        enemyPositions = new ArrayList<Position>();
        for(Position x : positions){
            enemyPositions.add(new Position(x.getX(), x.getY()));
        }
    }

    public List<Position> getEnemyPositions(){
        var tmp = new ArrayList<Position>();
        for(Position x : enemyPositions){
            tmp.add(new Position(x.getX(), x.getY()));
        }
        return tmp;
    }
}
