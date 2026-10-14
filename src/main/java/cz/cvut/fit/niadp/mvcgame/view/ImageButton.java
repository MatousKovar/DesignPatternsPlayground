package cz.cvut.fit.niadp.mvcgame.view;

import java.util.Objects;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.ImageSize;
import cz.cvut.fit.niadp.mvcgame.command.IButtonCommand;
import cz.cvut.fit.niadp.mvcgame.model.Position;

public class ImageButton {
    private static final double HOVER_SCALE = 1.06;
    private static final double PRESSED_SCALE = 0.96;

    private final IGameGraphics graphics;
    private final String imagePath;
    private IButtonCommand command;
    private Position position;
    private ImageSize size;
    private int height;
    private boolean hovered;
    private boolean pressed;

    public ImageButton(IGameGraphics graphics, String imagePath, Position position, int height) {
        this.graphics = Objects.requireNonNull(graphics, "Graphics must not be null");
        this.imagePath = Objects.requireNonNull(imagePath, "Image path must not be null");
        setPosition(position);
        setHeight(height);
        this.command = null;
    }

    public void render() {
        double scale = 1.0;
        if (pressed && hovered) {
            scale = PRESSED_SCALE;
        } else if (hovered) {
            scale = HOVER_SCALE;
        }

        int renderedHeight = Math.max(1, (int) Math.round(height * scale));
        ImageSize renderedSize = graphics.getImageSize(imagePath, renderedHeight);
        Position renderedPosition = new Position(
            (int) Math.round(position.getX() + (size.width() - renderedSize.width()) / 2),
            (int) Math.round(position.getY() + (size.height() - renderedSize.height()) / 2)
        );
        graphics.drawImageAtHeight(imagePath, renderedPosition, renderedHeight);
    }

    public boolean contains(double x, double y) {
        return x >= position.getX() && x < position.getX() + size.width()
            && y >= position.getY() && y < position.getY() + size.height();
    }

    public void processMouseMove(double x, double y) {
        hovered = contains(x, y);
    }

    public void processMousePress(double x, double y) {
        processMouseMove(x, y);
        pressed = hovered;
    }

    public boolean processMouseRelease(double x, double y) {
        processMouseMove(x, y);
        boolean clicked = pressed && hovered;
        pressed = false;
        if(clicked){
            if (command == null)
               return false;
            command.execute();
        }
        return clicked;
    }

    public void resetInteraction() {
        hovered = false;
        pressed = false;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    public Position getPosition() {
        return new Position(position.getX(), position.getY());
    }

    public ImageSize getSize() {
        return size;
    }

    public void setPosition(Position position) {
        Objects.requireNonNull(position, "Position must not be null");
        this.position = new Position(position.getX(), position.getY());
        resetInteraction();
    }

    public void setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Button height must be positive");
        }
        ImageSize newSize = graphics.getImageSize(imagePath, height);
        if (!Double.isFinite(newSize.width()) || newSize.width() <= 0
                || !Double.isFinite(newSize.height()) || newSize.height() <= 0) {
            throw new IllegalArgumentException("Button image dimensions must be positive and finite");
        }
        this.height = height;
        this.size = newSize;
        resetInteraction();
    }

    public void setCommand(IButtonCommand buttonCommand){
        this.command = Objects.requireNonNull(buttonCommand, "Command must not be null");
    }
}
