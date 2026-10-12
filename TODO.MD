# Povinná část semestrální práce

- [x] Pozadi    
- [x] Přidat nepřátele
- [x] Přidat kolize (po zásahu nepřátele raketou)
- [ ] Přidat GameInfo (text zobrazující skóre a další informace jako například úhel kanónu, sílu kanónu, mód kanónu atd.)
- [ ] Vytvořit zbylé Commandy pro ovládání Modelu
- [ ] Vylepšit ukládání věcí do Mementa (pro krok zpět)
- [ ] Přidat visit metody pro nepřátele, kolize a herní info
- [ ] Vytvořit 3 testy (tak aby bylo celkem 5 testů a alespoň v dvou testech bylo použito Mockování)
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

## Flyweight
- mame objekt enemy a missle, tyto objekty mohou mit type - hlavne co se tyce skinu - to je cesta ke skinu a velikost hitboxu
- takze MissileType a EnemyType je flyweight object

## Nápady po dokončení povinné části
- [ ] Přidat menu s volbami Play a Level editor.
- [ ] Play spustí náhodně vygenerovaný level.


## Nepovinna cast
- [ ] Level editor umožní vytvořit vlastní level a potom si ho zahrát.
