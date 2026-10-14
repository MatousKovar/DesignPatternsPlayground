package cz.cvut.fit.niadp.mvcgame.screen;

import java.util.List;

public interface IAppState{ 
    public void processPressedKeys(List<String> keys);

    public void processMouseClick(double x, double y, AppStateManager navigator);

    public void render();

}