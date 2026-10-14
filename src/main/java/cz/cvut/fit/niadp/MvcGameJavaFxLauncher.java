package cz.cvut.fit.niadp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.ArrayList;

import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.bridge.GameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.JavaFxGraphics;
import cz.cvut.fit.niadp.mvcgame.screen.AppStateManager;
import cz.cvut.fit.niadp.mvcgame.screen.GameState;
import cz.cvut.fit.niadp.mvcgame.screen.MenuState;
import cz.cvut.fit.niadp.mvcgame.screen.PauseState;

public class MvcGameJavaFxLauncher extends Application {

    private static final MvcGame theMvcGame = new MvcGame();

    @Override
    public void init() {
        theMvcGame.init();
    }

    @Override
    public void start(Stage stage) {
        String winTitle = theMvcGame.getWindowTitle();
        int winWidth = theMvcGame.getWindowWidth();
        int winHeigth = theMvcGame.getWindowHeight();
        stage.setTitle( winTitle );
        Group root = new Group();
        Scene theScene = new Scene( root );
        stage.setScene( theScene );
        stage.setResizable(false);
        Canvas canvas = new Canvas( winWidth, winHeigth );
        root.getChildren().add( canvas );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        IGameGraphics gameGraphics = new GameGraphics(new JavaFxGraphics(gc));
        AppStateManager appStateManager = new AppStateManager(new MenuState(gameGraphics),new GameState(theMvcGame), new PauseState(gameGraphics)); // Init page states
        ArrayList<String> pressedKeysCodes = new ArrayList<>();
        //register event handlers in JavaFX
        theScene.setOnKeyPressed(
                e -> {
                    String code = e.getCode().toString();
                    // only add once... prevent duplicates
                    if (!pressedKeysCodes.contains(code))
                        pressedKeysCodes.add(code);
                }
        );
        theScene.setOnKeyReleased(
                e -> {
                    String code = e.getCode().toString();
                    pressedKeysCodes.remove( code );
                }
        );
        canvas.setOnMouseMoved(event ->
            appStateManager.processMouseMove(event.getX(), event.getY())
        );
        canvas.setOnMouseDragged(event ->
            appStateManager.processMouseMove(event.getX(), event.getY())
        );
        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                appStateManager.processMousePress(event.getX(), event.getY());
            }
        });
        canvas.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                appStateManager.processMouseRelease(event.getX(), event.getY());
            }
        });
        canvas.setOnMouseExited(event ->
            appStateManager.processMouseMove(Double.NaN, Double.NaN)
        );
        stage.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                appStateManager.resetMouseInteraction();
            }
        });
        // the game-loop
        theMvcGame.setGraphicsContext(gameGraphics);
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                appStateManager.processPressedKeys(pressedKeysCodes);
                // theMvcGame.processPressedKeys(pressedKeysCodes);
            }
        }.start();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
