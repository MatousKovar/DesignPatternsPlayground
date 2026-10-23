package cz.cvut.fit.niadp.mvcgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.GameObjectsFactoryA;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.builder.Level;
import cz.cvut.fit.niadp.mvcgame.builder.LevelBuilder;
import cz.cvut.fit.niadp.mvcgame.command.AbstractGameCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractImpactObject;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.GameObject;
import cz.cvut.fit.niadp.mvcgame.model.GameInfo;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.proxy.IGameModel;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.strategy.RandomMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.strategy.RealMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.strategy.SimpleMovingStrategy;

public class GameModel implements IGameModel{
    private static final int TRAJECTORY_POINT_COUNT = 20;
    private static final double TRAJECTORY_TIME_STEP = 0.5;

    private final AbstractCannon cannon;
    private final Set<AbstractMissile> missiles;
    private final Set<IObserver> observers;
    private final Set<AbstractEnemy> enemies;
    private Level level;
    private final Set<AbstractImpactObject> impactAnimations;
    private IGameObjectsFactory factory;
    protected IMovingStrategy movingStrategy;
    protected final Queue<AbstractGameCommand> unexecutedCommands;
    protected final Stack<AbstractGameCommand> executedCommands;
    protected int score;
    private GameInfo gameInfo;
    private boolean explosiveShotEnabled;
    private int remainingExplosiveShots = MvcGameConfig.EXPLOSIVE_SHOTS_IN_LEVEL_LIMIT;

    public GameModel(){
        factory = new GameObjectsFactoryA(this);
        score = 0;
        observers = new HashSet<IObserver>();
        cannon = factory.createCannon();
        missiles = new HashSet<AbstractMissile>();
        enemies = new HashSet<AbstractEnemy>();
        impactAnimations = new HashSet<>();
        movingStrategy = new RealMovingStrategy();
        gameInfo = new GameInfo(cannon,movingStrategy);
        gameInfo.setExplosiveShotsLeft(remainingExplosiveShots);
        unexecutedCommands = new LinkedBlockingQueue<>();
        executedCommands = new Stack<>();
    }

    public void update() {
        if (level != null) {
            enemies.addAll(level.update(currentTimeNanos()));
        }
        moveEnemies();
        moveMissiles();
        executeCommands();
        checkCollissions();
        removeExpiredImpactAnimations();
    }

    private void checkCollissions()
    {
        Set<AbstractEnemy> enemiesToRemove = new HashSet<>();
        Set<AbstractMissile> missilesToRemove = new HashSet<>();
        boolean collisionOccurred = false;

        for (AbstractMissile missile : missiles) {
            for (AbstractEnemy enemy : enemies) {
                if (enemy.getHitbox().overlaps(missile.getHitbox())) {
                    missilesToRemove.add(missile);
                    collisionOccurred = true;

                    for (AbstractEnemy affectedEnemy : missile.getAffectedEnemies(enemy, enemies)) {
                        if (enemiesToRemove.add(affectedEnemy)) {
                            gameInfo.increaseScore(affectedEnemy.getScoreForKill());
                            impactAnimations.add(factory.createImpact(
                                affectedEnemy.getPosition(),
                                affectedEnemy.getHitbox().getWidth(),
                                affectedEnemy.getHitbox().getHeight()
                            ));
                        }
                    }
                    break;
                }
            }
        }

        boolean removedEnemies = enemies.removeAll(enemiesToRemove);
        boolean removedMissiles = missiles.removeAll(missilesToRemove);
        if (collisionOccurred || removedEnemies || removedMissiles) {
            notifyObservers();
        }
    }

    private void removeExpiredImpactAnimations() {
        boolean removedImpacts = impactAnimations.removeIf(AbstractImpactObject::isExpired);
        if (removedImpacts) {
            notifyObservers();
        }
    }

    private void executeCommands(){
        while (!unexecutedCommands.isEmpty()) {
            executedCommands.push(unexecutedCommands.poll().doExecute());
        }
    }

    private void moveEnemies() {
        long now = currentTimeNanos();
        for (AbstractEnemy enemy : enemies) {
            enemy.update(MvcGameConfig.ENEMY_MOVE_SPEED, now);
        }
    }

    protected void moveMissiles(){
        missiles.forEach(missile -> missile.move());
        destroyMissiles();
        notifyObservers();
    }

    protected void destroyMissiles(){
        missiles.removeAll(
            missiles.stream().filter(missile -> 
                missile.getPosition().getX() > MvcGameConfig.MAX_X 
                || missile.getPosition().getX() < MvcGameConfig.MIN_X
                || missile.getPosition().getY() > MvcGameConfig.MAX_Y
                || missile.getPosition().getY() < MvcGameConfig.MIN_Y
            )
            .toList()
        );
    }

    @Override
    public List<Position> getTrajectoryPreview() {
        if (!(movingStrategy instanceof RealMovingStrategy)
                && !(movingStrategy instanceof SimpleMovingStrategy)) {
            return List.of();
        }

        List<Position> points = new ArrayList<>(TRAJECTORY_POINT_COUNT);
        Position origin = cannon.getCannonPosition();
        double angle = cannon.getCannonAngle();
        int power = cannon.getCannonPower();
        Position previousPoint = origin;
        for (int i = 1; i <= TRAJECTORY_POINT_COUNT; i++) {
            double time = i * TRAJECTORY_TIME_STEP;
            Position displacement = movingStrategy.calculateDisplacement(angle, power, time);
            Position base = origin;
            if (movingStrategy instanceof SimpleMovingStrategy) {
                base = previousPoint;
            }
            Position point = new Position(
                base.getX() + displacement.getX(),
                base.getY() + displacement.getY()
            );
            points.add(point);
            previousPoint = point;
        }
        return points;
    }

    @Override
    public boolean hasEnemyEscaped() {
        return enemies.stream().anyMatch(enemy ->
            enemy.getHitbox().getX() + enemy.getHitbox().getWidth() <= MvcGameConfig.MIN_X
        );
    }

    public Position getCannonPosition(){
        return cannon.getPosition();
    }

    public void moveCannonUp(){
        cannon.moveUp();
        notifyObservers();
    }

    public void moveCannonDown(){
        cannon.moveDown();
        notifyObservers();
    }

    public void cannonShoot(){
        for (AbstractMissile missile : cannon.shoot()) {
            if (explosiveShotEnabled && remainingExplosiveShots > 0) {
                missile = factory.createExplosiveMissile(missile);
                remainingExplosiveShots--;
            }
            missiles.add(missile);
        }
        gameInfo.setExplosiveShotsLeft(remainingExplosiveShots);
        notifyObservers();
    }

    @Override
    public void generateLevel() {
        Level level = new LevelBuilder(factory).build();
        loadLevel(level);
    }

    @Override
    public void loadLevel(Level level) {
        this.level = Objects.requireNonNull(level, "Level must not be null");
        remainingExplosiveShots = MvcGameConfig.EXPLOSIVE_SHOTS_IN_LEVEL_LIMIT;
        gameInfo.setExplosiveShotsLeft(remainingExplosiveShots);
        enemies.clear();
        long now = currentTimeNanos();
        level.start(now);
        enemies.addAll(level.update(now));
        notifyObservers();
    }

    protected long currentTimeNanos() {
        return System.nanoTime();
    }

    public void spawnNewEnemy(){
        int x_size = (MvcGameConfig.MAX_X - MvcGameConfig.MIN_X);
        int y_size = (MvcGameConfig.MAX_Y - MvcGameConfig.MIN_Y);

        int minSpawnX = MvcGameConfig.MIN_X + x_size * 20 / 100;
        int minSpawnY = MvcGameConfig.MIN_Y + y_size * 20 / 100;
        int maxSpawnY = MvcGameConfig.MAX_Y - y_size * 20 / 100;

        int x = ThreadLocalRandom.current().nextInt(minSpawnX, MvcGameConfig.MAX_X);
        int y = ThreadLocalRandom.current().nextInt(minSpawnY, maxSpawnY);

        Position spawnPosition = new Position(x, y);
        AbstractEnemy new_enemy = factory.createEnemy(spawnPosition);
        new_enemy.spawn(currentTimeNanos());
        enemies.add(new_enemy);
        notifyObservers();
    }

    public void aimCannonUp() {
        cannon.aimUp();
        notifyObservers();
    }
    public void aimCannonDown() {
        cannon.aimDown();
        notifyObservers();
    }
    public void cannonPowerUp() {
        cannon.powerUp();
        notifyObservers();
    }
    public void cannonPowerDown() {
        cannon.powerDown();
        notifyObservers();
    }

    public Set<AbstractMissile> getMissiles(){
        return missiles;
    }

    public Set<GameObject> getGameObjects(){
        var gameObjects = new HashSet<GameObject>();
        gameObjects.addAll(missiles);
        gameObjects.addAll(enemies);
        gameObjects.addAll(impactAnimations);
        gameObjects.add(cannon);
        return gameObjects;
    }

    public GameInfo getGameInfo(){
        return gameInfo;
    }

    public IMovingStrategy getMovingStrategy(){
        return movingStrategy;
    }

    @Override
    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
       observers.forEach(IObserver::update);
    }

    public void toggleMovingStrategy(){
        movingStrategy = movingStrategy.getNextStrategy(this);
        gameInfo.setMovingStrategy(movingStrategy);
        notifyObservers();

    }


    public IMovingStrategy getNextMovingStrategy(SimpleMovingStrategy strategy){
        return new RandomMovingStrategy();
    }

    public IMovingStrategy getNextMovingStrategy(RandomMovingStrategy strategy){
        return new RealMovingStrategy();
    }

    public IMovingStrategy getNextMovingStrategy(RealMovingStrategy strategy){
        return new SimpleMovingStrategy();
    }

    public void toggleShootingMode(){
        cannon.toggleShootingMode();
        notifyObservers();
    }

    @Override
    public void toggleExplosiveShot() {
        explosiveShotEnabled = !explosiveShotEnabled;
        notifyObservers();
    }

    @Override
    public boolean isExplosiveShotEnabled() {
        return explosiveShotEnabled;
    }

    private record Memento(
        Object cannonMemento,
        Map<AbstractMissile, Object> missiles,
        Map<AbstractEnemy, Object> enemies,
        Map<AbstractImpactObject, Object> impacts,
        Object gameInfoMemento,
        IMovingStrategy movingStrategy,
        Map<IMovingStrategy, Object> strategyMementos,
        List<AbstractGameCommand> commandHistory,
        boolean explosiveShotEnabled,
        int remainingExplosiveShots,
        Level level,
        Object levelMemento
    ) {}

    public Object createMemento(){
        Map<IMovingStrategy, Object> strategies = new IdentityHashMap<>();
        Object levelMemento = level.createMemento(currentTimeNanos());
        strategies.put(movingStrategy, movingStrategy.createMemento());
        for (AbstractMissile missile : missiles) {
            IMovingStrategy strategy = missile.getMovingStrategy();
            if (!strategies.containsKey(strategy)) {
                strategies.put(strategy, strategy.createMemento());
            }
        }
        return new Memento(
            cannon.createMemento(),
            snapshotObjects(missiles),
            snapshotObjects(enemies),
            snapshotObjects(impactAnimations),
            gameInfo.createMemento(),
            movingStrategy,
            Collections.unmodifiableMap(strategies),
            List.copyOf(executedCommands),
            explosiveShotEnabled,
            remainingExplosiveShots,
            level,
            levelMemento
        );
    }

    public void setMemento(Object memento){
        Memento snapshot = (Memento) memento;
        explosiveShotEnabled = snapshot.explosiveShotEnabled();
        remainingExplosiveShots = snapshot.remainingExplosiveShots();
        level = snapshot.level();
        level.restoreMemento(snapshot.levelMemento(), currentTimeNanos());
        cannon.restoreMemento(snapshot.cannonMemento());
        restoreObjects(missiles, snapshot.missiles());
        restoreObjects(enemies, snapshot.enemies());
        restoreObjects(impactAnimations, snapshot.impacts());
        snapshot.strategyMementos().forEach(IMovingStrategy::restoreMemento);
        gameInfo.restoreMemento(snapshot.gameInfoMemento());
        gameInfo.setExplosiveShotsLeft(remainingExplosiveShots);
        movingStrategy = snapshot.movingStrategy();
        gameInfo.setMovingStrategy(movingStrategy);
        executedCommands.clear();
        executedCommands.addAll(snapshot.commandHistory());
        unexecutedCommands.clear();
        notifyObservers();
    }

    private <T extends GameObject> Map<T, Object> snapshotObjects(Set<T> objects) {
        Map<T, Object> snapshots = new IdentityHashMap<>();
        for (T object : objects) {
            snapshots.put(object, object.createMemento());
        }
        return Collections.unmodifiableMap(snapshots);
    }

    private <T extends GameObject> void restoreObjects(Set<T> objects, Map<T, Object> snapshots) {
        objects.clear();
        snapshots.forEach((object, snapshot) -> {
            object.restoreMemento(snapshot);
            objects.add(object);
        });
    }

    @Override
    public void registerCommand(AbstractGameCommand command) {
        unexecutedCommands.add(command);
    }

    @Override
    public void undoLastCommand() {
        if(!executedCommands.isEmpty()){
            executedCommands.pop().unExecute();
        }
    }
}
