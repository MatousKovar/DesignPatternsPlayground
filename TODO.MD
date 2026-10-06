# Povinná část semestrální práce

- [x] Pozadi    
- [ ] Přidat nepřátele
- [ ] Přidat kolize (po zásahu nepřátele raketou)
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