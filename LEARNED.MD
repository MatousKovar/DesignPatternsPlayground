## Proxy structural design pattern 
- Proxy class drzi v sobe nejaky dalsi objekt a ma **stejne rozhrani jako tento objekt** - napriklad databaze, nejaky objekt z knihovny
- pri komunikaci s objektem je napriklad potreba udelat nejaka funkcionalita a tak misto toho aby vsichni klienti meli copy paste toho kodu u sebe tak to bude v proxy a ta potom vola ten samotny objekt
- zde se pouziva u GameModelProxy.java - realny model i proxy dedi z IGameModel.java - proxy zatim jen pouziva vsechny metody subjektu
- je to stejny princip jako v pocitacovych sitich - caching, forwarding, blocking, logging 


## Singleton holder v CareTaker
- pouziva static class - znamena to ze instance muze existovat i kdyz neexistuje instance Outer class - v tomto pripade CareTaker
- je to lazy - vytvori se az kdyz se zavola SingletonHolder.INSTANCE a je to final a static - takze se to ne
- singleton je znicenej az pomoci garbage collectoru - tim ze je INSTANCE static tak se nevaze k zadnemu objektu ale primo ke tride

## Input handling
- java FX precte nejakou klavesu posle to do MvcGame class pomoci processPressedKey v herni smycce a MvcGame vola controller a jeho metodu processPressedKey

## Memento a CareTaker
- memento je classa ktera vi co se ma ulozit za snapshot jine classy - my to pouzivame u ulozeni stavu modleu
- dale se vyskytuje caretaker - to je vicemene ukladani historie a dalsi prace s tim mementem - treba ukladani do soubory a podobne 
- Nicmene zaklad je tohle: Memento - vi co ma ulozit do sebe z modelu, Model: umi vytvorit memento a nacist, Caretaker - vola ukladani a nacitani modelu a uklada si historii

## Observer
- Mas observera a observable
- observable si registruje observery vsechny a pri zmene vola notifyObservers - to vola na jednotlive observery jejich metodu update

## Abstract factory
- vytvari objekty aniz by zbytek aplikace znal konkretni tridu
- hodi se kdyz mame urcite rodiny objektu - napriklad rodina kanonu a k nemu nalezejicich raket
- zde to je IGameObjectsFactory.java
- z abstraktni tovarny se potom muze udelat konkretni tovarna - GameObjectsFactoryA.java
- abstraktni udava co musi konkretni umet - klasika
- **to hlavni kdy to pouzit je kdyz mam prave nejakou rodinu objektu, ktera spolu logicky souvisi a muzu mit vice takovych rodin - napr prave kanon a strela**
- TODO - pridat dalsi kanon a mas quest hotovej
 
## Visitor 
- pridavani funkce objektum bez zmeneni objektu
- objekt ktery je visited tak ma metodu accept a visitor objekt implementuje visit metodu
- v acceptu jen volam visitor.visit a predavam sebe
- zde je GameDrawer visitor - ma na starosti vykreslovani a implementuje jak se to ma pro jednotlive tridy chovat
- jednotlive objekty jako kanon, nebo raketa implementuji accept:
```
public void accept(IVisitor visitor) {
    visitor.visit(this);
}
```
- podivej na classu GameView - ktera ma uvnitr GameDrawer jako visitora
- vyhoda ze herni objekty takhle vubec nemusi vedet nic o vykreslovaci grafickej vrstve - vsechno resi visitor

## Strategy
- chci pouzivat ruzne algoritmy ktere chci mezi sebou menit - napriklad ruzne formy navigace, nebo v tomto pripade strely
- maji stejne rozhrani a objekt ktery je pouziva se vubec nemusi menit
- IMovingStrategy.java - definuje rozhrani
- objekt co strategii pouziva ma atribut strategy ktery se nastavuje v konstruktoru a strategie ma metodu run nebo move nebo cokoliv, ktera obsahuje samotny algoritmus - je mimo samotnou classu raktey

## State
- chci v kazdem stavu objektu jine chovani metod - switch spatnej napad
- reseni - mam state objekty ktere obsahuji pozadovane chovani ve vsech moznych stavech
- takze to chovani se vola na tech stavovych objektech 
- zde je to v IShootingMode.java
- TODO stav se meni podle instanceof - coz neni uplne ciste - prechod by eml resit stavovy automat a tedy samotny stav objekt


## State vs strategy
Strategy:
  volí algoritmus, například způsob pohybu rakety

State:
  objekt má aktuální režim, který mění jeho chování

