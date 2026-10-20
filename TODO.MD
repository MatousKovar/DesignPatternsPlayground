# Povinná část semestrální práce

- [x] Pozadi    
- [x] Přidat nepřátele
- [x] Přidat kolize (po zásahu nepřátele raketou)
- [x] Přidat GameInfo (text zobrazující skóre a další informace jako například úhel kanónu, sílu kanónu, mód kanónu atd.)
- [x] Vytvořit zbylé Commandy pro ovládání Modelu
- [x] Vylepšit ukládání věcí do Mementa (pro krok zpět)
- [x] Přidat visit metody pro nepřátele, kolize a herní info
- [x] Vytvořit 3 testy (tak aby bylo celkem 5 testů a alespoň v dvou testech bylo použito Mockování)
- [ ] Přidat 3 další návrhové vzory
- [ ] Přidat 5 zajímavých funkcionalit hry

## Enemy
- nova trida vytvorena AbstractEnemy
- enemy maji vice skinu - proto EnemySkin.java enum
- visitor podle skinu vykresluje jakej druh enemy se ma vykreslit
- mozna by bylo dobre mit state a udelat tam ze treba enemy ma vice zivotu nebo tak neco
- enemy dostava random skin pri vytvoreni
- enemy je mozne tvorit pomoci factory - controller bere SPAWN_ENEMY_STRIN

## Kolize
- pridanej atribut Hitbox k AbstractEnemy a AbstractMissile - uchovava pozici a sirku a vysku a umi overlaps - takze bude obsahovat logiku kolize
- cela kolize bude osetrena v modelu v update()
- kolize by mohla vytvorit lifetimeLimited object - exploze
- pouziva i AbstractImpactObject
- vykresluje impact objekty animovane pomoci toho jak dlouho je impact nazivu

## Commandy
- dodelano kompletni hybani delem a nastavovani sily

## Memento
- Ukládá stav kanónu, skóre, pozice střel a nepřátel, stáří střel a výbuchů, stav strategií a historii undo. Po obnovení překreslí hru.


## Flyweight
- mame objekt enemy a missle, tyto objekty mohou mit type - hlavne co se tyce skinu - to je cesta ke skinu a velikost hitboxu
- takze MissileType a EnemyType je flyweight object


## GameInfo 
- implementovano jako samostatny objekt uvnitr GameModel ktery trackuje cannon object score a moving strategy - model musi updatovat moving strategy a score kdyz se meni
- Objekt udelam Visitable a GameDrawer bude resit logiku vykresleni

## Main menu a buttony 
- Stranky menu pauza a game pouzivaji GameState, mame MenuState, PauseState a GameState
- GameState je vicemene wrapper pro klicove MVC hry
- prechody resi AppStateManager drzi instance jednotlivych stavu
- State ma uvnitr buttony, ktere funguji jako commandy, ktere vytvari AppStateManager v konstruktoru - commandy prave meni stavy AppStateManageru
- ImageButton - je trida ktera se umi renderovat a obsahuje logiku pro hover a kliknuti a obsahuje command kterej dela on click. Command prirazuje GameState

## Builder
- builder vytvori Level object kterej obsahuje enemy a kdy se maji spawnout
- level objekt je vytvoren pri startgamecommandu a je predan Modelu
- model pri update zaroven vola level, ktery na zaklade casu rekne jestli se ma spawnout enemy nebo ne a kde

## 5 zajimavych funkcionalit
### menu - Nova funkce
- HOTOVO
- vytvoi se nejakej stav TODO
- State pattern - Bude nejaky screen 

### Animovana impact animace
- HOTOVO
- animace ma nekolik framu, delka trvani animace je v ImpactObjectu, animace je stejne velka jako enemy kterou zabije
- stavi na stejnem rozhrani jako missile - LifetimeLimitedGameObject

### nahled strely kanonu
- HOTOVO

### Builder - design pattern + tvorba levelu
- HOTOVO

### Decorator - design pattern + zajimava funkcionalita
- prida strele specialni schopnost



# nepovinna cast
- udelam pohyb nepratel at to je jako nejake jine starsi hry ja nevim space odyssey nebo jak se to jmenuje






--- 


# Nepovinna cast
- [ ] Level editor umožní vytvořit vlastní level a potom si ho zahrát.
