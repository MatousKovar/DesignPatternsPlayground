package cz.cvut.fit.niadp.mvcgame.model;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.GameObjectsFactoryA;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectsFactory;
import cz.cvut.fit.niadp.mvcgame.command.AbstractGameCommand;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbstractEnemy;
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
    private final AbstractCannon cannon;
    private final Set<AbstractMissile> missiles;
    private final Set<IObserver> observers;
    private final Set<AbstractEnemy> enemies;
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
        movingStrategy = new RealMovingStrategy();
        gameInfo = new GameInfo(cannon,movingStrategy);
        unexecutedCommands = new LinkedBlockingQueue<>();
        executedCommands = new Stack<>();
    }

    public void update() {
        moveMissiles();
        executeCommands();
        checkCollissions();
    }

    private void checkCollissions()
    {
        Set<AbstractEnemy> enemiesToRemove = new HashSet<>();

        for (AbstractEnemy enemy : enemies) {
            for (AbstractMissile missile : missiles) {
                if (enemy.getHitbox().overlaps(missile.getHitbox())) {
                    enemiesToRemove.add(enemy);
                    gameInfo.increaseScore(enemy.getScoreForKill());
                    break;
                }
            }
        }

        boolean removedEnemies = enemies.removeAll(enemiesToRemove);
        if (removedEnemies) {
            notifyObservers();
        }
    }

    private void executeCommands(){
        while (!unexecutedCommands.isEmpty()) {
            executedCommands.push(unexecutedCommands.poll().doExecute());
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
        /*if(movingStrategy instanceof SimpleMovingStrategy){
            movingStrategy = new RandomMovingStrategy();
        }else if(movingStrategy instanceof RandomMovingStrategy){
            movingStrategy = new RealMovingStrategy();
        }else if( movingStrategy instanceof RealMovingStrategy){
            movingStrategy = new SimpleMovingStrategy();
        }else{

        }*/
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

    private static class Memento{
        private int cannonPositionX;
        private int cannonPositionY;
        private Set<AbstractMissile> missiles;
        private Set<IObserver> observers;
        private Set<AbstractEnemy> enemies;
        
        // fronta prikazu a takovy ty veci mozna jeste?
    }

    public Object createMemento(){
        Memento gameModelSnapshot = new Memento();
        gameModelSnapshot.cannonPositionX = cannon.getPosition().getX();
        gameModelSnapshot.cannonPositionY = cannon.getPosition().getY();
        gameModelSnapshot.missiles = this.missiles;
        gameModelSnapshot.observers = this.observers;
        gameModelSnapshot.enemies = this.enemies; 
        
        return gameModelSnapshot;
    }

    public void setMemento(Object memento){
        Memento gameModelSnapshot = (Memento)memento;
        cannon.getPosition().setX(gameModelSnapshot.cannonPositionX);
        cannon.getPosition().setY(gameModelSnapshot.cannonPositionY);
        this.missiles.addAll(gameModelSnapshot.missiles);
        this.observers.addAll(gameModelSnapshot.observers);
        this.enemies.addAll(gameModelSnapshot.enemies);
    }

    @Override
    public void registerCommand(AbstractGameCommand command) {
        unexecutedCommands.add(command);
    }

    @Override
    public void undoLastCommand() {
        if(!executedCommands.isEmpty()){
            executedCommands.pop().unExecute();
            notifyObservers();
        }
    }
}
