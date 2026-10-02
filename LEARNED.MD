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

