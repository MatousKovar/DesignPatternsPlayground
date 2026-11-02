# About project 
This project is a semestral work for NI-ADP subject about architecture and design patterns

# Used design patterns for the mandatory part

## Flyweight
- Flyweight pattern is typically used for data that is shared by multiple objects
- In my case I have got EnemySkinType - which is enum which consists of image path and hitbox size for the enemies
- The EnemySkinType is Enum so there exists only one instance of each enum variant which is shared
- It is used also for MissileSkinType

## Builder
- Builder uses objectFactory to construct Level object
- in this case we bave LevelBuilder.java
- Builder takes care of placing as many enemies as defined in the config, also it defines time of spawn for them
- Level is just object which contains the info about where what when everything is, but all the logic of placing is in builder

## Decorator
- In my game when key E is pressed the missile becomes explosive - on impact its hitbox expands 
- this behavior is achieved by decorating the normal missile 
- the MissileDecorator implements same interface as classic missile - AbstractMisile
- it accepts already created missile in the constructor
- it overwrites getImpactedEnemies and createImpact methods all other methods are just delegated to wrapee


# Five added features
## Menu and End screen
- switching between the screens is implemented by creating IAppState - and we have GameState, MenuState and GameEndState
- managing states is AppStateManager
- also ImageButton class was created to encapsulate button hover logic and command execution

## Animated collission
- Animated Collission was added - AbstractImpactObject and ImpactA
- The Drawer changes its apperance based on time it is alive

## Explosive missiles
- As explained in the Decorator design pattern - toggled using E. Limit 3 per round

## Preview of shot trajectory
- cannon accepts MovingStrategy and returns list of points that show where the missile will fly

## Random generated levels
- When game starts the levels are unique each time.

## Added game features



## Todo

- Přidat nepřátele
- Přidat kolize (po zásahu nepřátele raketou)
- Přidat GameInfo (text zobrazující skóre a další informace jako například úhel kanónu, sílu kanónu, mód kanónu atd.)
- Vytvořit zbylé Commandy pro ovládání Modelu
- Vylepšit ukládání věcí do Mementa (pro krok zpět)
- Přidat visit metody pro nepřátele, kolize a herní info
- Vytvořit 3 testy (tak aby bylo celkem 5 testů a alespoň v dvou testech bylo použito Mockování)
- Přidat 3 další návrhové vzory
- Přidat 5 zajímavých funkcionalit hry


##