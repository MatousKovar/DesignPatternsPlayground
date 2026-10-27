package cz.cvut.fit.niadp.mvcgame.view;

import java.util.Objects;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.ImageSize;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class ImageButtonFactory {
    private final IGameGraphics gameGraphics;

    public ImageButtonFactory(IGameGraphics gameGraphics) {
        this.gameGraphics = gameGraphics;
    }

    public ImageButton create(String imagePath, Position centerPosition, int height) {
        ImageSize size = gameGraphics.getImageSize(imagePath, height);
        Position position = new Position(
            (int) Math.round(centerPosition.getX() - size.width() / 2),
            (int) Math.round(centerPosition.getY() - size.height() / 2)
        );
        return new ImageButton(gameGraphics, imagePath, position, height);
    }
}
