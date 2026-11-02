package cz.cvut.fit.niadp.mvcgame.model;

import org.junit.Assert;
import org.junit.Test;

import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import mockit.Injectable;
import mockit.Verifications;

public class GameModelObserverMockedTest {

    @Test
    public void toggleExplosiveShotNotifiesEveryRegisteredObserverOnce(
            @Injectable IObserver firstObserver,
            @Injectable IObserver secondObserver) {
        GameModel model = new GameModel();
        model.registerObserver(firstObserver);
        model.registerObserver(secondObserver);
        Assert.assertFalse(model.isExplosiveShotEnabled());

        model.toggleExplosiveShot();

        Assert.assertTrue(model.isExplosiveShotEnabled());
        new Verifications() {{
            firstObserver.update();
            times = 1;
            secondObserver.update();
            times = 1;
        }};
    }
}
