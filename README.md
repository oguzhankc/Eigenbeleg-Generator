# Eigenbeleg Generator

Backend-Repository des Webtech-Projekts „Eigenbeleg Generator“: eine Web-App, mit der angemeldete Nutzer Eigenbelege erfassen, als PDF erzeugen und in einem persönlichen Verlauf wiederfinden können.

> **Hinweis:** Das Projekt ist ein kleines Hochschulprojekt in Entwicklung und arbeitet nur mit fiktiven Testdaten. Rechtliche, steuerliche und datenschutzrechtliche Anforderungen wurden nicht geprüft; die Anwendung ersetzt keine Steuerberatung.

## Inhalt

- [Team und Abgabe](#team-und-abgabe)
- [Zweck und Zielgruppe](#zweck-und-zielgruppe)
- [Geplante Hauptfunktionen](#geplante-hauptfunktionen)
- [Zentrale Entity: Receipt](#zentrale-entity-receipt)
- [Technische Umsetzung](#technische-umsetzung)
- [Aktueller Stand und Meilenstein M1](#aktueller-stand-und-meilenstein-m1)
- [Projekt lokal starten](#projekt-lokal-starten)

## Team und Abgabe

| Angabe             | Wert                                                |
| ------------------ | --------------------------------------------------- |
| Team               | 31                                                  |
| Teammitglied       | Hüseyin Surhay Yilmaz                               |
| Teammitglied       | Oguzhan Cabar                                       |
| Übungsgruppe       | 1.Zug, 2.Gruppe                                     |
| Übungsdozent       | Herr Nasaltsev                                      |
| Projektname        | Eigenbeleg Generator                                |
| Backend-Repository | <https://github.com/oguzhankc/Eigenbeleg-Generator> |

## Zweck und Zielgruppe

**Problem:** Für jede Buchung wird ein Beleg benötigt. Fehlt er, muss ein Eigenbeleg mit bestimmten Angaben erstellt werden. Typische Fälle sind eine verlorene Quittung, ein Parkautomat ohne Quittung, Trinkgeld oder der Ankauf gebrauchter Ware von Privatpersonen ohne Rechnung. Handschriftliche Vorlagen sind fehleranfällig und schwer wiederzufinden.

**Idee:** Der Eigenbeleg Generator fragt die benötigten Angaben strukturiert ab, erzeugt ein einheitliches PDF mit eigenem Logo und speichert jeden Beleg im persönlichen Verlauf.

**Zielgruppe:** Selbstständige, Kleinunternehmer und Gewerbetreibende, etwa im Gebrauchtwarenhandel, wo regelmäßig von Privatpersonen angekauft wird.

## Geplante Hauptfunktionen

Alle Funktionen in diesem Abschnitt sind **geplant** und noch nicht umgesetzt. Den tatsächlichen Stand zeigt der Abschnitt [Aktueller Stand und Meilenstein M1](#aktueller-stand-und-meilenstein-m1).

| Funktion                  | Beschreibung |
| ------------------------- | --------------------------------------------------------------------------------- |
| Registrieren und anmelden | Jeder Nutzer sieht nur seine eigenen Belege.                                      |
| Firmendaten und Logo      | Firmendaten und Logo hinterlegen; beides erscheint automatisch auf jedem Beleg.   |
| Beleg erstellen           | Allgemeiner Eigenbeleg oder Ankaufbeleg von Privat; Pflichtfelder werden geprüft. |
| Belege anzeigen           | Verlauf als Liste und Detailansicht.                                              |
| PDF erzeugen              | PDF erstellen und jederzeit erneut herunterladen.                                 |

Nicht priorisiert und nur bei übriger Zeit: Belege bearbeiten oder löschen, Filtern und Suchen im Verlauf sowie weitere Einstellungen wie ein eigenes Belegnummern-Format oder Standardwerte.

## Zentrale Entity: `Receipt`

Ein `Receipt` steht für einen Eigenbeleg und soll später genau einem Nutzer gehören. Als weitere Entities sind `User` (Login) und `CompanyProfile` (Firmendaten inklusive Logo) geplant. Die Entity-Klasse ist umgesetzt; `ownerId` bleibt bis zum Login leer. Die Eingaberegeln aus den Use Cases (Pflichtfelder, Längen, Betrag größer 0, Datumsregeln, Anschrift beim Ankauf) sind als Bean-Validation-Annotationen hinterlegt.

| Eigenschaft   | Typ | Bedeutung |
| ------------- | ------ | --------- |
|       `id`    | `UUID` | Eindeutige ID |
|     `number`  |`String`| Fortlaufende Belegnummer, z. B. `EB-2026-0001` |
|      `type`   |  Enum  | `ALLGEMEIN`, `ANKAUF` |
|   `issueDate` |`LocalDate`| Ausstellungsdatum |
|  `paymentDate`|`LocalDate`| Datum der Zahlung bzw. des Kaufs |
|   `payeeName` |`String`| Zahlungsempfänger bzw. Verkäufer |
| `payeeAddress`|`String`| Anschrift des Zahlungsempfängers bzw. Verkäufers |
| `description` |`String`| Art der Aufwendung bzw. gekaufter Artikel |
| `amountCents` | `Long` | Bruttobetrag in Cent |
|   `vatRate`   |  Enum  | `REGELSATZ_19`, `ERMAESSIGT_7`, `OHNE_UST` (siehe [Steuerliche Auswahl](#steuerliche-auswahl-vatrate)) |
|    `reason`   |`String`| Grund, warum kein Originalbeleg vorliegt |
|`paymentMethod`|  Enum  | `BAR`, `UEBERWEISUNG`, `KARTE` |
| `issuerName`  |`String`| Name des Ausstellers, Kopie der Firmendaten beim Speichern (ab Login und Firmendaten) |
|`issuerAddress`|`String`| Anschrift des Ausstellers, Kopie der Firmendaten beim Speichern |
|  `createdAt`  |`Instant`| Erstellungszeitpunkt |
|   `ownerId`   | `UUID` | Verweis auf den Nutzer (erst mit Login relevant) |

### Steuerliche Auswahl (`vatRate`)

Beim Erstellen eines Belegs wählt der Nutzer eine von drei Optionen. Die Angabe dient nur der Dokumentation auf dem Beleg; aus Eigenbelegen ist in der Regel kein Vorsteuerabzug möglich.

| Option | Bedeutung | Typischer Fall |
| --- | --- | --- |
| `REGELSATZ_19` | 19 % Umsatzsteuer im Betrag enthalten | Parkgebühr, Büromaterial, die meisten Ausgaben |
| `ERMAESSIGT_7` | 7 % Umsatzsteuer im Betrag enthalten | Lebensmittel, Bücher, Bahntickets |
| `OHNE_UST` | Keine Umsatzsteuer im Betrag | Ankauf von Privatpersonen, Trinkgeld, Empfänger ist Kleinunternehmer |

Vorbelegung: Beim allgemeinen Eigenbeleg ist `REGELSATZ_19` voreingestellt, beim Ankaufbeleg `OHNE_UST`. Der Nutzer kann die Auswahl ändern.

## Technische Umsetzung

### Im Repository vorhanden

| Bereich | Stand laut Code |
| --- | --- |
| Sprache | Java, Toolchain Java 25 (`build.gradle`) |
| Framework | Spring Boot 4.1.1 |
| Build | Gradle mit Gradle Wrapper (`gradlew`) |
| Abhängigkeiten | `spring-boot-starter-webmvc`, `spring-boot-starter-validation`, Lombok, Spring Boot DevTools |
| Tests | JUnit 5: Kontext-Ladetest, Validierung von `Receipt`, JSON-Ausgabe und CORS der GET-Route |
| Deployment | `Dockerfile` für Render (Web Service), Port über `PORT`, erlaubte Frontend-Adresse über `FRONTEND_URL` |

### Geplant

| Bereich | Plan |
| --- | --- |
| Frontend | Vue.js mit TypeScript, eigenes Repository: <https://github.com/Surhay276/eigenbeleg-generator-frontend> |
| Datenbank | PostgreSQL auf Render (M4) |
| CI | GitHub Actions, bisher noch keine Workflows im Repository |

Schritt-für-Schritt-Anleitung für Render: [`docs/deployment.md`](docs/deployment.md).

## Aktueller Stand und Meilenstein M1

| M1-Anforderung | Status | Nachweis |
| --- | --- | --- |
| Spring-Boot-App | Erledigt | `src/main/java/htw/webtech2026/Webtech2026/Webtech2026Application.java` |
| Entity-Klasse `Receipt` | Erledigt | `model/Receipt.java` (plus Enums) |
| `GET /api/receipts` liefert eine Liste von Beispielobjekten | Erledigt | `web/ReceiptController.java` |

Für M1 ist keine Datenbank und keine Seed-Migration nötig: Die Route soll eine fest im Code angelegte Liste von Beispielbelegen als JSON zurückgeben.

## Projekt lokal starten

Voraussetzung: ein installiertes JDK 25, da `build.gradle` diese Toolchain-Version festlegt.

```bash
# Anwendung starten
./gradlew bootRun

# Tests ausführen
./gradlew test
```

Unter Windows `gradlew.bat` statt `./gradlew` verwenden. Ist `gradlew` nicht ausführbar, hilft `bash ./gradlew bootRun`. Ohne die Umgebungsvariable `PORT` läuft das Backend auf Port 8080, und ohne `FRONTEND_URL` darf das lokale Frontend unter `http://localhost:5173` die API aufrufen.

Beispieldaten: `GET http://localhost:8080/api/receipts`
