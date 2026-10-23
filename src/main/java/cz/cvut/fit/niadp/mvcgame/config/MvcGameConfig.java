package cz.cvut.fit.niadp.mvcgame.config;

public class MvcGameConfig {

    private MvcGameConfig(){

    }

    public static final int MAX_X = 1512;
    public static final int MAX_Y = 982;
    public static final int MIN_X = 0;
    public static final int MIN_Y = 0;
    public static final int MOVE_STEP = 10;

    public static final int CANNON_POS_X = 50;
    public static final int CANNON_POS_Y = MAX_Y / 2;

    public static final int DEFAULT_ENEMY_COUNT = 5;
    public static final double ENEMY_MOVE_SPEED = 40.0;
    public static final long ENEMY_SPAWN_INTERVAL_MILLIS = 1_000;
    public static final int ENEMY_SPAWN_MIN_X = 600;
    public static final int ENEMY_SPAWN_MIN_Y = 150;
    public static final int ENEMY_SPAWN_MAX_X = 1300;
    public static final int ENEMY_SPAWN_MAX_Y = 800;

    public static final double INIT_ANGLE = 0;
    public static final int INIT_POWER = 10;
    public static final double ANGLE_STEP = Math.PI / 18;
    public static final int POWER_STEP = 1;
    public static final double GRAVITY = 9.81;
    public static final int MAGIC_TIME_CONST = 100;

    public static final int EXPLOSION_RADIUS = 120;
    public static final int EXPLOSIVE_SHOTS_IN_LEVEL_LIMIT = 3;
}
