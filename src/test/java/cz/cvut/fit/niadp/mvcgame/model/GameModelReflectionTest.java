package cz.cvut.fit.niadp.mvcgame.model;
import org.junit.Assert;
import org.junit.Test;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.MissileSkinType;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.familyA.MissileA;
import cz.cvut.fit.niadp.mvcgame.strategy.SimpleMovingStrategy;
import java.lang.reflect.Method;

public class GameModelReflectionTest {
    private static final String MOVE_MISSILES_METHOD_NAME = "moveMissiles";
    private static final int MISSILE_COUNT = 50;
    private static final int MOVE_COUNT = MvcGameConfig.MAX_X / MvcGameConfig.MOVE_STEP + 1;

    @Test
    public void moveMissilesTest() throws ReflectiveOperationException {
        GameModel model = new GameModel();
        Method method = model.getClass().getDeclaredMethod(MOVE_MISSILES_METHOD_NAME);
        method.setAccessible(true);

        for (int i = 0; i < MISSILE_COUNT; i++) {
            model.getMissiles().add(new MissileA(
                new Position(MvcGameConfig.CANNON_POS_X, MvcGameConfig.CANNON_POS_Y),
                0,
                MvcGameConfig.INIT_POWER,
                new SimpleMovingStrategy(),
                MissileSkinType.FIRST
            ));
        }
        Assert.assertEquals(MISSILE_COUNT, model.getMissiles().size());

        method.invoke(model);
        for (var missile : model.getMissiles()) {
            Assert.assertEquals(
                MvcGameConfig.CANNON_POS_X + MvcGameConfig.MOVE_STEP,
                missile.getPosition().getX()
            );
        }

        for (int i = 1; i < MOVE_COUNT; i++) {
            method.invoke(model);
        }
        method.setAccessible(false);
        Assert.assertTrue(model.getMissiles().isEmpty());
        Assert.assertEquals(1, model.getGameObjects().size());
    }
}
