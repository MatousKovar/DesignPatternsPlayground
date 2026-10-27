package cz.cvut.fit.niadp.mvcgame.view;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractButtonView implements IView {
    private final List<ImageButton> buttons = new ArrayList<>();

    protected final void registerButton(ImageButton button) {
        buttons.add(button);
    }

    protected final void renderButtons() {
        for (ImageButton button : buttons) {
            button.render();
        }
    }

    public void processMouseMove(double x, double y) {
        for (ImageButton button : buttons) {
            button.processMouseMove(x, y);
        }
    }

    public void processMousePress(double x, double y) {
        for (ImageButton button : buttons) {
            button.processMousePress(x, y);
        }
    }

    public void processMouseRelease(double x, double y) {
        for (ImageButton button : buttons) {
            button.processMouseRelease(x, y);
        }
    }

    public void resetInteraction() {
        for (ImageButton button : buttons) {
            button.resetInteraction();
        }
    }
}
