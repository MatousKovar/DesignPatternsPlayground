package cz.cvut.fit.niadp.mvcgame.bridge;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public interface IGameGraphics {
    void drawImage(String path, Position position);
    void drawImageAtHeight(String path, Position position, int destinationHeight);
    ImageSize getImageSize(String path);

    default ImageSize getImageSize(String path, int destinationHeight) {
        if (destinationHeight <= 0) {
            throw new IllegalArgumentException("Destination height must be positive");
        }

        ImageSize originalSize = getImageSize(path);
        if (originalSize.height() <= 0) {
            throw new IllegalStateException("Image height must be positive");
        }

        double scale = destinationHeight / originalSize.height();
        return new ImageSize(originalSize.width() * scale, destinationHeight);
    }

    void drawText(String text, Position position);
    void drawRectangle(Position leftTop, Position rightBottom);
    void drawImageRegion(
        String path,
        Position destination,
        int sourceX,
        int sourceY,
        int sourceWidth,
        int sourceHeight,
        int destinationWidth,
        int destinationHeight
    );
    
    void drawImageRegion(
        String path,
        Position destination,
        int sourceX,
        int sourceY,
        int sourceWidth,
        int sourceHeight
    );
    

    void clear();
}
