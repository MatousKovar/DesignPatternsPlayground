package cz.cvut.fit.niadp.mvcgame.bridge;

import java.util.HashMap;
import java.util.Map;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class JavaFxGraphics  implements IGameGraphicsImplementor {
    
    private final GraphicsContext graphicsContext;
    private final Map<String, Image> imageCache = new HashMap<>();

    public JavaFxGraphics(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void drawImage(String path, Position position) {
        graphicsContext.drawImage(getImage(path), position.getX(), position.getY());
    }

    @Override
    public void drawImageRegion(String path,Position destination,int sourceX,int sourceY,int sourceWidth,int sourceHeight) {
        graphicsContext.drawImage(getImage(path),
            sourceX,
            sourceY,
            sourceWidth,
            sourceHeight,
            destination.getX(),
            destination.getY(),
            sourceWidth,
            sourceHeight
        );
    }

    private Image getImage(String path) {
        return imageCache.computeIfAbsent(path, Image::new);
    }

    @Override
    public void drawText(String text, Position position) {
        graphicsContext.fillText(text, position.getX(), position.getY());
    }

    @Override
    public void drawRectangle(Position leftTop, Position rightBottom) {
        Paint previousStroke = graphicsContext.getStroke();
        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.strokeRect(
            leftTop.getX(),
            leftTop.getY(),
            rightBottom.getX() - leftTop.getX(),
            rightBottom.getY() - leftTop.getY()
        );
        graphicsContext.setStroke(previousStroke);
    }

    @Override
    public void drawLine(Position startPosition, Position endPosition) {
        graphicsContext.strokeLine(startPosition.getX(), startPosition.getY(), endPosition.getX(), endPosition.getY());
    }

    @Override
    public void clear() {
        graphicsContext.clearRect(MvcGameConfig.MIN_X, MvcGameConfig.MIN_Y, MvcGameConfig.MAX_X, MvcGameConfig.MAX_Y);
    }

}
