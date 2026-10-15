package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.ImageSize;
import cz.cvut.fit.niadp.mvcgame.controller.GameController;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;
import cz.cvut.fit.niadp.mvcgame.visitor.GameDrawer;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;

public class GameView implements IObserver, IView{ 
    private static final int TRAJECTORY_DOT_RADIUS = 3;

    private final IGameModel model;
    private final GameController controller;
    private IGameGraphics gameGraphics;
    protected GameDrawer gameDrawer;

    public GameView(IGameModel model){
        this.model = model;
        this.controller = new GameController(model);
        this.model.registerObserver(this);
        this.gameDrawer = new GameDrawer();
    }

    public GameController getController(){
        return controller;
    }

    public void render() {
        if(gameGraphics != null) {
            gameGraphics.clear();
            gameGraphics.drawImage(MvcGameResources.BACKGROUND_IMAGE, new Position(0,0));
            ImageSize missileSize = gameGraphics.getImageSize(MvcGameResources.MISSILE_RESOURCE);
            for (Position point : model.getTrajectoryPreview()) {
                Position center = new Position(
                    (int) Math.round(point.getX() + missileSize.width() / 2),
                    (int) Math.round(point.getY() + missileSize.height() / 2)
                );
                gameGraphics.drawCircle(center, TRAJECTORY_DOT_RADIUS);
            }
            model.getGameObjects().forEach(gameObject -> gameObject.accept(gameDrawer));
            model.getGameInfo().accept(gameDrawer);
        }
    }

    public void setGraphicsContext(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
        this.gameDrawer.setGraphicsContext(gameGraphics);
        this.render();
    }

    @Override
    public void update(){
        render();
    }
}
