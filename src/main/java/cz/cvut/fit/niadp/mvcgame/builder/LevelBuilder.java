package cz.cvut.fit.niadp.mvcgame.builder;

public class LevelBuilder {
    private int enemyCount = 5;
    private int minX = 600;
    private int minY = 150;
    private int maxX = 1300;
    private int maxY = 800;

    public LevelBuilder enemyCount(int count){
        if (count <= 0)
            throw new IllegalArgumentException("pocet nepratel musi byt kladne cislo");

        this.enemyCount  = count;
        return this;
    }
}
