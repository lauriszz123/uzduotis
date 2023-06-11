# JAVA programavimo užduotis

## Funkciniai reikalavimai:

Palaikyti dviejų tipų vartotojus: user, admin

Vartotojas user gali:

1. Gauti žinutes (kas, kada, ką)
2. Įrašyti naują žinutę

Vartotojas admin gali:

1. Užregistruoti naują user
2. Pašalinti user
3. Ištraukti statistiką:
   - vartotojas
   - žinučių kiekis
   - pirmosios žinutės laikas
   - paskutinės žinutės laikas
   - vidutinis žinutės ilgis
   - paskutiniosios žinutės tekstas

## Kurimas produkcijai

***COMPATIBILITY***

***Naudota GNU bash, version 3.2.57(1)-release (x86_64-apple-darwin22) produkcijos MAKEFILE sukurti.***

***Bei MacBook Pro 2017 programuoti projekta.***

Tereikia tik paleisti Makefile kuris yra `root` aplankale


```bash
make
```

Makefile susideda is triju darbu:

- clean
- build
- prod

Pagrindinis `entry point`:

- all

## Dokumentacija

Dokumentacija galima rasti paleidus aplikacija per IntelliJ: `http://localhost:8080/swagger-ui.html` arba aplankale `dist/docs`

## Kaip paleisti programa

Produkcijos programa randasi `dist/jar/uzduotis.jar`.

Pirmiausia patikrinkite ar turite [H2 Database](https://www.h2database.com/), jei turite, reikia ikelti database SQL komandas, jas galite rasti `dist/database/database.sql`:

```bash
java -cp ./bin/h2*.jar org.h2.tools.RunScript -url "jdbc:h2:file:~/test" -user sa -password "" -script dist/database/database.sql
```

Tada turite paleisti H2 Database, bukite `h2` aplankale (kitaip sakan `H2 root` aplankale):

```bash
java -jar ./bin/h2*.jar
```

Tada galite paleisti programa:

```bash
java -jar ./dist/jar/uzduotis.jar
```

## Testavimas

Testavimui buvo naudota `Postman` programa, visa workspace galima rasti: `Postmap API` aplankale.

Postman REST API `Login` ir `Logout` komandos yra automatizuotos priskirti `session token` prie `global variables` del patogesnio testavimo.