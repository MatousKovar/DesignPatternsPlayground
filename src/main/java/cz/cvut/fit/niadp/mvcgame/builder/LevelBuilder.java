package cz.cvut.fit.niadp.mvcgame.builder;

public class LevelBuilder {
    private int enemyCount = 5;

    public LevelBuilder(int enemyCount){
        this.enemyCount = enemyCount;
    }
    public LevelBuilder enemyCount(int count){
        if (count <= 0)
            throw new IllegalArgumentException("pocet nepratel musi byt kladne cislo");
        
        this.enemyCount  = count;
        return this;
    }
}
