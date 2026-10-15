package cz.cvut.fit.niadp.mvcgame.bridge;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public class GameGraphics implements IGameGraphics {

    protected IGameGraphicsImplementor implementor;

    public GameGraphics(IGameGraphicsImplementor implementor){
        this.implementor = implementor;
    }

    @Override
    public void drawImage(String path, Position position) {
        implementor.drawImage(path, position);
    }

    @Override
    public void drawImageAtHeight(String path, Position position, int destinationHeight) {
        implementor.drawImageAtHeight(path, position, destinationHeight);
    }

    @Override
    public ImageSize getImageSize(String path) {
        return implementor.getImageSize(path);
    }

    @Override
    public void drawText(String text, Position position) {
        implementor.drawText(text, position);
    }

    @Override
    public void drawRectangle(Position leftTop, Position rightBottom) {
        implementor.drawRectangle(leftTop, rightBottom);
    }

    @Override
    public void drawCircle(Position center, int radius) {
        implementor.drawCircle(center, radius);
    }

    @Override
    public void clear() {
        implementor.clear();
    }


    @Override
    public void drawImageRegion(String path,Position destination,int sourceX,int sourceY,int sourceWidth,int sourceHeight, int destinationWidth, int destinationHeight) {
        implementor.drawImageRegion(path, destination, sourceX, sourceY, sourceWidth, sourceHeight, destinationWidth, destinationHeight);
    }

    @Override
    public void drawImageRegion(String path,Position destination,int sourceX,int sourceY,int sourceWidth,int sourceHeight) {
        implementor.drawImageRegion(path, destination, sourceX, sourceY, sourceWidth, sourceHeight);
    }
}
