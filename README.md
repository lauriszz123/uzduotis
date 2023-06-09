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

Produkcijos programa randasi `dist/jar/uzduotis.jar` ja paleisti galima su komanda:
```bash
java -jar ./dist/jar/uzduotis.jar
```

## Testavimas

Testavimui buvo naudota `Postman` programa, visa workspace galima rasti: `Postmap API` aplankale.

Postman REST API `Login` ir `Logout` komandos yra automatizuotos priskirti `session token` prie `global variables` del patogesnio testavimo.