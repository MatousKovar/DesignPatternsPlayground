package cz.cvut.fit.niadp.mvcgame.builder;

public enum Difficulty {
    EASY(0.7, 0.75, 1.3),
    MEDIUM(1.0, 1.0, 1.0),
    HARD(1.4, 1.25, 0.75);

    private final double enemyCountMultiplier;
    private final double enemySpeedMultiplier;
    private final double spawnIntervalMultiplier;

    Difficulty(double enemyCountMultiplier, double enemySpeedMultiplier, double spawnIntervalMultiplier) {
        this.enemyCountMultiplier = enemyCountMultiplier;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
        this.spawnIntervalMultiplier = spawnIntervalMultiplier;
    }

    public double getEnemyCountMultiplier() {
        return enemyCountMultiplier;
    }

    public double getEnemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }

    public double getSpawnIntervalMultiplier() {
        return spawnIntervalMultiplier;
    }

    public Difficulty next() {
        switch (this) {
            case EASY:
                return MEDIUM;
            case MEDIUM:
                return HARD;
            case HARD:
                return EASY;
            default:
                return MEDIUM;
        }
    }
}
