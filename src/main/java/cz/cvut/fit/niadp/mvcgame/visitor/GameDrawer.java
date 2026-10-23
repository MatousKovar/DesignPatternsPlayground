package cz.cvut.fit.niadp.mvcgame.visitor;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameResources;
import cz.cvut.fit.niadp.mvcgame.model.GameInfo;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractImpactObject;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.GameObject;

public class GameDrawer implements IVisitor{
    private static final int GAME_INFO_WIDTH = 300;
    private static final int GAME_INFO_HEIGHT = 172;
    private static final int GAME_INFO_MARGIN = 20;
    private static final int GAME_INFO_TEXT_PADDING = 12;
    private static final int GAME_INFO_LINE_HEIGHT = 22;
    private static final int IMPACT_SHEET_COLUMNS = 5;
    private static final int IMPACT_SHEET_ROWS = 3;
    private static final int IMPACT_FRAME_COUNT = IMPACT_SHEET_COLUMNS * IMPACT_SHEET_ROWS;
    private static final int IMPACT_FRAME_WIDTH = 192;
    private static final int IMPACT_FRAME_HEIGHT = 192;

    protected IGameGraphics gameGraphics;

    public void setGraphicsContext(IGameGraphics gameGraphics){
        this.gameGraphics = gameGraphics;
    }

    @Override
    public void visit(AbstractCannon cannon) {
        drawGameObject(cannon, MvcGameResources.CANNON_RESOURCE);
    }

    @Override
    public void visit(AbstractMissile missile) {
        drawGameObject(missile, missile.getMissileSkinType().getImagePath());
    }

    @Override 
    public void visit(AbstractEnemy enemy){
        drawGameObject(enemy, enemy.getEnemySkinType().getImagePath());
    }

    protected void drawGameObject(GameObject gameObject, String resource){
        if(gameGraphics != null)
            gameGraphics.drawImage(resource, gameObject.getPosition());
    }

    @Override
    public void visit(GameInfo info) {
        if (gameGraphics == null) {
            return;
        }

        int left = MvcGameConfig.MAX_X - GAME_INFO_WIDTH - GAME_INFO_MARGIN;
        int top = GAME_INFO_MARGIN;
        Position leftTop = new Position(left, top);
        Position rightBottom = new Position(MvcGameConfig.MAX_X - GAME_INFO_MARGIN - 10, top + GAME_INFO_HEIGHT);

        gameGraphics.drawRectangle(leftTop, rightBottom);

        int textX = left + GAME_INFO_TEXT_PADDING;
        int textY = top + GAME_INFO_LINE_HEIGHT + 3;
        gameGraphics.drawText("Score: " + info.getScore(), new Position(textX, textY));
        gameGraphics.drawText(
            String.format("Cannon angle: %.1f°", Math.toDegrees(info.getCannonAngle())),
            new Position(textX, textY + GAME_INFO_LINE_HEIGHT)
        );
        gameGraphics.drawText(
            "Cannon power: " + info.getCannonPower(),
            new Position(textX, textY + 2 * GAME_INFO_LINE_HEIGHT)
        );
        gameGraphics.drawText(
            "Moving strategy: " + info.getMovingStrategy(),
            new Position(textX, textY + 3 * GAME_INFO_LINE_HEIGHT)
        );
        gameGraphics.drawText(
            "Shooting mode: " + info.getShootingMode(),
            new Position(textX, textY + 4 * GAME_INFO_LINE_HEIGHT)
        );
        gameGraphics.drawText(
            "Explosive shots left: " + info.getExplosiveShotsLeft(),
            new Position(textX, textY + 5 * GAME_INFO_LINE_HEIGHT)
        );
    }

    @Override
    public void visit(AbstractImpactObject impact){
        if (gameGraphics == null) {
            return;
        }

        double progress = (double) impact.getAge() / impact.getLifetimeDurationMillis();
        progress = Math.max(0.0, Math.min(progress, 1.0));

        int frameIndex = Math.min( IMPACT_FRAME_COUNT - 1, (int) (progress * IMPACT_FRAME_COUNT));
        int column = frameIndex % IMPACT_SHEET_COLUMNS;
        int row = frameIndex / IMPACT_SHEET_COLUMNS;

        //draws animation part based on lifetime of object
        gameGraphics.drawImageRegion(
            MvcGameResources.IMPACT_IMAGE,
            impact.getPosition(),
            column * IMPACT_FRAME_WIDTH,
            row * IMPACT_FRAME_HEIGHT,
            IMPACT_FRAME_WIDTH,
            IMPACT_FRAME_HEIGHT,
            impact.getWidth(),
            impact.getHeight()
        );
    }

}

