package cz.cvut.fit.niadp.mvcgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
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
import cz.cvut.fit.niadp.mvcgame.builder.EnemySpawn;
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
    private final Map<AbstractEnemy, Long> enemySpawnTimes = new IdentityHashMap<>();
    private final List<EnemySpawn> pendingSpawns = new ArrayList<>();
    private long levelStartedAtNanos;
    private final Set<AbstractImpactObject> impactAnimations;
    private IGameObjectsFactory factory;
    protected IMovingStrategy movingStrategy;
    protected final Queue<AbstractGameCommand> unexecutedCommands;
    protected final Stack<AbstractGameCommand> executedCommands;
    protected int score;
    private GameInfo gameInfo;

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
        unexecutedCommands = new LinkedBlockingQueue<>();
        executedCommands = new Stack<>();
    }

    public void update() {
        spawnDueEnemies();
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

                    if (enemiesToRemove.add(enemy)) {
                        gameInfo.increaseScore(enemy.getScoreForKill());
                        impactAnimations.add(factory.createImpact(enemy.getPosition(),
                                                                  enemy.getHitbox().getWidth(),
                                                                  enemy.getHitbox().getHeight()));
                    }
                    break;
                }
            }
        }

        boolean removedEnemies = enemies.removeAll(enemiesToRemove);
        enemiesToRemove.forEach(enemySpawnTimes::remove);
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
        enemySpawnTimes.keySet().retainAll(enemies);
        for (AbstractEnemy enemy : enemies) {
            long spawnedAt = enemySpawnTimes.computeIfAbsent(enemy, ignored -> now);
            double elapsedSeconds = (now - spawnedAt) / 1_000_000_000.0;
            enemy.move(MvcGameConfig.ENEMY_MOVE_SPEED, elapsedSeconds);
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
        missiles.addAll(cannon.shoot());
        notifyObservers();
    }

    @Override
    public void generateLevel() {
        Level level = new LevelBuilder(factory).build();
        loadLevel(level);
    }

    @Override
    public void loadLevel(Level level) {
        List<EnemySpawn> spawns = level.getEnemySpawns();
        enemies.clear();
        enemySpawnTimes.clear();
        pendingSpawns.clear();
        pendingSpawns.addAll(spawns);
        levelStartedAtNanos = currentTimeNanos();
        spawnDueEnemies();
        notifyObservers();
    }

    private void spawnDueEnemies() {
        if (pendingSpawns.isEmpty()) {
            return;
        }
        long elapsedMillis = (currentTimeNanos() - levelStartedAtNanos) / 1_000_000;
        Iterator<EnemySpawn> iterator = pendingSpawns.iterator();
        while (iterator.hasNext()) {
            EnemySpawn spawn = iterator.next();
            if (spawn.spawnTimeMillis() <= elapsedMillis) {
                enemies.add(spawn.enemy());
                enemySpawnTimes.put(spawn.enemy(), currentTimeNanos());
                iterator.remove();
            }
        }
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
        enemies.add(new_enemy);
        enemySpawnTimes.put(new_enemy, currentTimeNanos());
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

    private record Memento(
        Object cannonMemento,
        Map<AbstractMissile, Object> missiles,
        Map<AbstractEnemy, Object> enemies,
        Map<AbstractImpactObject, Object> impacts,
        Object gameInfoMemento,
        IMovingStrategy movingStrategy,
        Map<IMovingStrategy, Object> strategyMementos,
        List<AbstractGameCommand> commandHistory
    ) {}

    public Object createMemento(){
        Map<IMovingStrategy, Object> strategies = new IdentityHashMap<>();
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
            List.copyOf(executedCommands)
        );
    }

    public void setMemento(Object memento){
        Memento snapshot = (Memento) memento;
        cannon.restoreMemento(snapshot.cannonMemento());
        restoreObjects(missiles, snapshot.missiles());
        restoreObjects(enemies, snapshot.enemies());
        restoreObjects(impactAnimations, snapshot.impacts());
        snapshot.strategyMementos().forEach(IMovingStrategy::restoreMemento);
        gameInfo.restoreMemento(snapshot.gameInfoMemento());
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
