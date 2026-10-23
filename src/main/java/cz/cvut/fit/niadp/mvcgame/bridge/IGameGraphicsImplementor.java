package cz.cvut.fit.niadp.mvcgame.bridge;

import cz.cvut.fit.niadp.mvcgame.model.Position;


public interface IGameGraphicsImplementor {
    void drawImage(String path, Position position);
    void drawRotatedImage(String path, Position position, double angleDegrees);
    void drawImageAtHeight(String path, Position position, int destinationHeight);
    ImageSize getImageSize(String path);
    void drawImageRegion(String path,Position destination,int sourceX,int sourceY,int sourceWidth,int sourceHeight);
    void drawImageRegion(String path,Position destination,int sourceX,int sourceY,int sourceWidth,int sourceHeight, int destinationWidth, int destinationHeight);
    void drawText(String text, Position position);
    void drawRectangle(Position leftTop, Position rightBottom);
    void drawCircle(Position center, int radius);
    void drawLine(Position startPosition, Position endPosition);
    void clear();
}
