package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

public interface IAppState{ 
    public void processPressedKeys(List<String> keys);

    public void processMouseClick(double x, double y, AppStateManager navigator);

    default void processMouseMove(double x, double y) {}

    default void processMousePress(double x, double y) {}

    default void processMouseRelease(double x, double y, AppStateManager navigator) {}

    default void resetMouseInteraction() {}

    public void render();

}
