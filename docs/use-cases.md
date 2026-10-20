# Use Cases: Eigenbeleg Generator

Dieses Dokument beschreibt, was Nutzer mit dem Eigenbeleg Generator tun können, und ordnet jeden Use Case den Abgaben M2, M3, M4 und der finalen Abgabe zu. Am Ende steht der Screenshot-Plan für die finale Abgabe.

> **Hinweis:** Hochschulprojekt mit fiktiven Testdaten. Die fachlichen Regeln (Pflichtangaben eines Eigenbelegs) sind vereinfacht und ersetzen keine Steuerberatung.

**Grundprinzip:** Den Beleg-Generator kann jede Person ohne Konto nutzen und das PDF herunterladen. Ein Konto dient in erster Linie dazu, Belege **zu speichern**: Nur angemeldete Nutzer haben einen Belegverlauf. Belege von Besuchern ohne Konto werden nicht gespeichert und erscheinen in keinem Verlauf. Die Firmendaten des Ausstellers (Name und Anschrift) sind Pflicht, denn der Beleg muss zeigen, wer ihn ausstellt. Das Logo ist dagegen optional, denn ein Eigenbeleg entsteht gerade, weil das Gegenüber keine Rechnung oder Quittung ausgestellt hat. Jeder Beleg kann eine fortlaufende Belegnummer tragen: Bei Nutzern vergibt sie das System automatisch, Besucher tragen sie selbst ein.

## Inhalt

- [Akteure](#akteure)
- [Übersicht](#übersicht)
- [Use Cases im Detail](#use-cases-im-detail)
- [Zuordnung zu den Abgaben](#zuordnung-zu-den-abgaben)
- [Screenshot-Plan für die finale Abgabe](#screenshot-plan-für-die-finale-abgabe)
- [Testideen je Use Case](#testideen-je-use-case)

## Akteure

| Akteur | Beschreibung |
| --- | --- |
| Besucher | Person ohne Konto oder nicht angemeldet. Kann den Beleg-Generator nutzen und das PDF herunterladen, **ohne** dass der Beleg gespeichert wird. Hat keinen Belegverlauf. Kann sich registrieren oder anmelden. |
| Nutzer | Angemeldete Person, z. B. Selbstständige oder Gebrauchtwarenhändler. Erstellt Belege wie ein Besucher, aber sie werden gespeichert. Sieht und verwaltet nur die eigenen Belege. |
| System | Spring-Boot-Backend mit PostgreSQL-Datenbank. Prüft Eingaben, vergibt Belegnummern (nur für gespeicherte Belege), speichert Daten und erzeugt PDFs. |

## Übersicht

| ID | Use Case | Akteur | Priorität | Frühester Meilenstein |
| --- | --- | --- | --- | --- |
| UC-01 | Registrieren | Besucher | Muss | Finale Abgabe |
| UC-02 | Anmelden und Abmelden | Besucher / Nutzer | Muss | Finale Abgabe |
| UC-03 | Firmendaten hinterlegen (Logo optional) | Nutzer | Muss | Finale Abgabe |
| UC-04 | Allgemeinen Eigenbeleg erstellen | Besucher / Nutzer | Muss | M4 |
| UC-05 | Ankaufbeleg von Privat erstellen | Besucher / Nutzer | Muss | M4 |
| UC-06 | Belegverlauf anzeigen | Nutzer | Muss | M2 |
| UC-07 | Belegdetails anzeigen | Nutzer | Muss | Finale Abgabe |
| UC-08 | Beleg als PDF herunterladen | Besucher / Nutzer | Muss | Finale Abgabe |
| UC-09 | Beleg bearbeiten oder löschen | Nutzer | Kann | Finale Abgabe |
| UC-10 | Verlauf durchsuchen und filtern | Nutzer | Kann | Finale Abgabe |

```mermaid
flowchart LR
    B([Besucher])
    N([Nutzer])
    B --> UC01[UC-01 Registrieren]
    B --> UC02[UC-02 Anmelden und Abmelden]
    B --> UC04[UC-04 Allgemeinen Eigenbeleg erstellen]
    B --> UC05[UC-05 Ankaufbeleg erstellen]
    B --> UC08[UC-08 PDF herunterladen]
    N --> UC04
    N --> UC05
    N --> UC03[UC-03 Firmendaten hinterlegen, Logo optional]
    N --> UC06[UC-06 Belegverlauf anzeigen]
    UC06 --> UC07[UC-07 Belegdetails anzeigen]
    UC07 --> UC08
    N -.-> UC09[UC-09 Bearbeiten/Löschen]
    N -.-> UC10[UC-10 Suchen/Filtern]
```

Routen und Frontend-Pfade in den Use Cases sind Vorschläge. Bereits umgesetzt ist nur `GET /api/receipts` mit fest im Code hinterlegten Beispielbelegen (M1).

## Use Cases im Detail

### UC-01: Registrieren

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Besucher |
| Ziel | Ein eigenes Konto anlegen, damit Belege gespeichert werden, im Verlauf erscheinen und nur dem Ersteller gehören. Der Generator selbst ist auch ohne Konto nutzbar. |
| Meilenstein | Finale Abgabe |
| Vorbedingungen | Besucher ist nicht angemeldet. |
| Nachbedingung | Ein neuer `User` ist gespeichert (Passwort nur als Hash). Der Nutzer ist angemeldet und landet auf den Firmendaten (UC-03). Nach dem Speichern der Firmendaten folgt der leere Belegverlauf (UC-06). |
| Backend-Route | `POST /api/auth/register` |
| Frontend | `/registrieren` |
| Screenshot | Ausgefülltes Registrierungsformular mit E-Mail, Passwort und Passwort-Wiederholung. |

**Hauptablauf**

1. Besucher klickt auf der Startseite oder im Belegformular (Hinweis „Zum Speichern anmelden“) auf „Registrieren“.
2. System zeigt das Formular mit E-Mail, Passwort und Passwort-Wiederholung.
3. Besucher füllt die Felder aus und klickt auf „Konto erstellen“.
4. System prüft die Eingaben, legt das Konto an und meldet den Nutzer an.
5. System leitet zu den Firmendaten (UC-03) weiter, weil diese vor dem ersten gespeicherten Beleg hinterlegt werden müssen. Danach folgt der Belegverlauf (UC-06).

**Alternativabläufe**

- **3a** E-Mail ist ungültig, Passwort hat weniger als 8 Zeichen oder die Wiederholung stimmt nicht: Das System markiert das Feld und zeigt eine verständliche Meldung. Es wird nichts gespeichert.
- **4a** Die E-Mail ist bereits registriert: Das System meldet „Für diese E-Mail gibt es schon ein Konto“ und verlinkt die Anmeldung.

### UC-02: Anmelden und Abmelden

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Besucher (Anmelden), Nutzer (Abmelden) |
| Ziel | Belege speichern und auf die eigenen gespeicherten Belege zugreifen, danach die Sitzung wieder beenden. |
| Meilenstein | Finale Abgabe |
| Vorbedingungen | Ein Konto existiert (UC-01). |
| Nachbedingung | Nach dem Anmelden werden erstellte Belege gespeichert, und Verlauf und Detailansicht zeigen nur Belege mit eigener `ownerId`. Nach dem Abmelden sind Verlauf und Detailansicht wieder gesperrt. Der Beleg-Generator bleibt ohne Anmeldung nutzbar, speichert dann aber nichts. |
| Backend-Route | `POST /api/auth/login`, `POST /api/auth/logout` |
| Frontend | `/anmelden`, Schaltfläche „Abmelden“ in der Navigationsleiste |
| Screenshot | Anmeldeformular und daneben die Navigationsleiste mit angemeldeter E-Mail und „Abmelden“. |

**Hauptablauf**

1. Besucher öffnet „Anmelden“ und gibt E-Mail und Passwort ein.
2. System prüft die Zugangsdaten und startet eine Sitzung.
3. System zeigt den Belegverlauf (UC-06) und in der Navigationsleiste die E-Mail des Nutzers.
4. Nutzer klickt später auf „Abmelden“.
5. System beendet die Sitzung und zeigt die Startseite.

**Alternativabläufe**

- **2a** Zugangsdaten sind falsch: Das System meldet „E-Mail oder Passwort falsch“, ohne zu verraten, welches von beiden.
- **2b** Ein nicht angemeldeter Besucher ruft direkt eine geschützte Seite wie `/belege` (Verlauf, Details) oder `/einstellungen/firma` auf: Das System leitet zur Anmeldung um und danach zurück zur gewünschten Seite. Die Belegformulare (UC-04, UC-05) sind **nicht** geschützt.

### UC-03: Firmendaten hinterlegen (Logo optional)

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Nutzer |
| Ziel | Firmenname und Anschrift einmal speichern, damit sie automatisch auf jedem Beleg-PDF erscheinen, und den Nummernkreis für die fortlaufende Belegnummer festlegen. Das Logo ist optional: Ein Eigenbeleg ersetzt eine fehlende Rechnung oder Quittung des Gegenübers und braucht deshalb kein Logo. |
| Meilenstein | Finale Abgabe |
| Vorbedingungen | Nutzer ist angemeldet (UC-02). |
| Nachbedingung | Ein `CompanyProfile` mit Firmenname und Anschrift ist für den Nutzer gespeichert. Jeder danach gespeicherte Beleg übernimmt Firmenname und Anschrift als Kopie (`issuerName`, `issuerAddress`, siehe UC-04). Bereits gespeicherte Belege und ihre PDFs bleiben unverändert. Das Logo erscheint, falls vorhanden, auf allen PDFs. |
| Backend-Route | `GET /api/company-profile`, `PUT /api/company-profile`, `POST /api/company-profile/logo` |
| Frontend | `/einstellungen/firma` |
| Screenshot | Einstellungsseite mit ausgefüllten Firmendaten und Vorschau des hochgeladenen Logos. |

**Hauptablauf**

1. Nutzer öffnet „Einstellungen → Firmendaten“.
2. System zeigt das Formular, vorausgefüllt mit bisher gespeicherten Werten.
3. Nutzer gibt Firmenname (Pflicht), Inhaber, Anschrift (Pflicht) und optional die Steuernummer ein.
4. Nutzer legt den Nummernkreis fest: Präfix (Standard `EB`) und nächste Nummer im laufenden Jahr (Standard 1). Die Belegnummer hat das Format Präfix-Jahr-Nummer, z. B. `EB-2026-0004`. So kann er an eine bereits bestehende Nummerierung anknüpfen. Am 1. Januar beginnt die Zählung automatisch wieder bei 1, das Präfix bleibt.
5. Nutzer wählt optional eine Logodatei aus. Das System zeigt eine Vorschau.
6. Nutzer klickt auf „Speichern“. Das System speichert die Daten und bestätigt das.

**Alternativabläufe**

- **3a** Firmenname oder Anschrift fehlen: Das System markiert die Pflichtfelder und speichert nicht.
- **4a** Die nächste Nummer ist keine positive ganze Zahl oder bereits vergeben: Das System lehnt sie ab und nennt die nächste freie Nummer.
- **5a** Die Datei ist kein PNG oder JPG oder größer als 1 MB: Das System lehnt sie mit Hinweis auf erlaubte Formate ab. Das bisherige Logo bleibt erhalten.
- **5b** Nutzer entfernt das Logo: Das System löscht es, und folgende PDFs erscheinen ohne Logo. Die Firmendaten bleiben.
- **6a** Nutzer will einen Beleg speichern, bevor Firmendaten existieren: Das Belegformular verweist mit einem Link auf die Firmendaten, der Beleg wird erst danach gespeichert.

### UC-04: Allgemeinen Eigenbeleg erstellen

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Besucher (ohne Speichern), Nutzer (mit Speichern) |
| Ziel | Für eine Ausgabe ohne Originalbeleg (Parkautomat ohne Quittung, Trinkgeld, verlorene Quittung) einen vollständigen Eigenbeleg erstellen. Angemeldete Nutzer speichern ihn zusätzlich im Verlauf. |
| Meilenstein | M4 (Speichern über `POST /api/receipts` in PostgreSQL), Besucher-Modus und Login-Bezug ab finaler Abgabe |
| Vorbedingungen | **Nutzer:** Firmendaten sind hinterlegt (UC-03). **Besucher:** Keine, er gibt die Ausstellerdaten im Formular ein, der Beleg wird nicht gespeichert. In M4 gibt es noch keinen Login und keine Firmendaten, dort werden alle Belege gespeichert. |
| Nachbedingung | **Nutzer:** Ein neuer `Receipt` mit `type` = `ALLGEMEIN` ist in der Datenbank gespeichert und erscheint oben im Belegverlauf (UC-06). **Besucher:** Es wird nichts gespeichert. Der Besucher erhält direkt das PDF (UC-08). |
| Backend-Route | Nutzer: `POST /api/receipts` (JSON, speichert). Besucher: `POST /api/receipts/pdf` als `multipart/form-data` mit dem Teil `receipt` (Belegdaten als JSON) und dem optionalen Teil `logo` (Bilddatei). Erzeugt das PDF und speichert nichts. |
| Frontend | `/belege/neu?typ=allgemein` (ohne Anmeldung erreichbar) |
| Screenshot | Eingabemaske „Neuer Eigenbeleg“ mit ausgefüllten Feldern, Steuerauswahl `REGELSATZ_19` und Schaltfläche „Beleg speichern“ (angemeldet). Zusätzlich dieselbe Maske ohne Anmeldung mit Hinweis „Nicht angemeldet – Beleg wird nicht gespeichert“ und Schaltfläche „PDF erstellen“. |

**Felder der Eingabemaske** (Abbildung auf die Entity `Receipt`)

`issuerName` und `issuerAddress` sind neue Felder der Entity. `Receipt.java` hat sie seit dem 19.10.2026, befüllt werden sie ab der finalen Abgabe. Bei Nutzern speichern sie eine Kopie der Firmendaten zum Zeitpunkt des Speicherns, damit spätere Änderungen am Firmenprofil alte Belege nicht verändern. Bei Besuchern kommen sie aus dem Formular und werden nicht gespeichert.

| Feld in der Maske | Entity-Feld | Regel |
| --- | --- | --- |
| Belegart | `type` | Fest `ALLGEMEIN` (nicht änderbar in dieser Maske) |
| Datum der Zahlung | `paymentDate` | Pflicht, nicht in der Zukunft |
| Ausstellungsdatum | `issueDate` | Pflicht, Standard heute, nicht vor `paymentDate` |
| Zahlungsempfänger | `payeeName` | Pflicht, höchstens 200 Zeichen |
| Anschrift des Empfängers | `payeeAddress` | Optional, aber empfohlen |
| Art der Aufwendung | `description` | Pflicht, höchstens 500 Zeichen |
| Betrag (brutto, in Euro) | `amountCents` | Pflicht, größer als 0. Eingabe „4,50“ wird als 450 Cent gespeichert. |
| Umsatzsteuer | `vatRate` | Pflicht: `REGELSATZ_19` (Vorbelegung), `ERMAESSIGT_7` oder `OHNE_UST` |
| Grund für fehlenden Originalbeleg | `reason` | Pflicht, höchstens 500 Zeichen |
| Zahlungsart | `paymentMethod` | Pflicht: `BAR` (Vorbelegung), `UEBERWEISUNG` oder `KARTE` |
| Name des Ausstellers (nur Besucher) | `issuerName` | Pflicht, höchstens 200 Zeichen. Nutzer sehen das Feld nicht, das System setzt es beim Speichern (siehe unten). |
| Anschrift des Ausstellers (nur Besucher) | `issuerAddress` | Pflicht. Nutzer sehen das Feld nicht, das System setzt es beim Speichern (siehe unten). |
| Belegnummer (nur Besucher) | `number` | Optional, vom Besucher selbst eingetragen, höchstens 30 Zeichen, z. B. `EB-2026-0001`. Bleibt das Feld leer, trägt das PDF keine Nummer. |
| (vom System gesetzt, nur bei Speichern) | `id` | Neue UUID |
| (vom System gesetzt, nur bei Speichern) | `number` | Fortlaufend je Nutzer und Jahr nach dem Nummernkreis aus UC-03, z. B. `EB-2026-0004`. Am 1. Januar beginnt die Zählung neu. In M4 ohne Login fortlaufend über alle Belege. |
| (vom System gesetzt, nur bei Speichern) | `issuerName`, `issuerAddress` | Kopie von Firmenname und Anschrift aus den Firmendaten (UC-03) zum Zeitpunkt des Speicherns. In M4 ohne Firmendaten leer. |
| (vom System gesetzt, nur bei Speichern) | `createdAt` | Aktueller Zeitpunkt |
| (vom System gesetzt, nur bei Speichern) | `ownerId` | ID des angemeldeten Nutzers. In M4 bleibt sie leer (`null`). |

**Hauptablauf (Nutzer, angemeldet)**

1. Nutzer klickt im Belegverlauf auf „Neuer Beleg → Allgemeiner Eigenbeleg“.
2. System zeigt die Eingabemaske mit Vorbelegungen (heutiges Datum, `REGELSATZ_19`, `BAR`).
3. Nutzer füllt die Felder aus. Kurze Hilfetexte erklären jedes Feld, z. B. „Warum gibt es keine Quittung?“.
4. Nutzer klickt auf „Beleg speichern“.
5. System prüft die Eingaben (Bean Validation im Backend, gleiche Regeln im Frontend), vergibt `id`, `number` und `createdAt`, übernimmt `issuerName` und `issuerAddress` aus den Firmendaten und speichert den Beleg.
6. System zeigt die Detailansicht des neuen Belegs (UC-07) mit der Meldung „Beleg EB-2026-0004 gespeichert“.

**Hauptablauf (Besucher, nicht angemeldet)**

1. Besucher klickt auf der Startseite auf „Eigenbeleg erstellen“ und wählt „Allgemeiner Eigenbeleg“.
2. System zeigt dieselbe Eingabemaske mit Vorbelegungen und dem Hinweis „Du bist nicht angemeldet. Der Beleg wird nicht gespeichert und erscheint in keinem Verlauf. Melde dich an, um Belege zu speichern.“ mit Links zu Anmeldung und Registrierung.
3. Besucher füllt die Felder aus, auch Name und Anschrift des Ausstellers, und trägt optional eine eigene Belegnummer ein. Ein Logo kann er optional hinzufügen (PNG oder JPG, höchstens 1 MB). Es wird nicht gespeichert.
4. Besucher klickt auf „PDF erstellen“.
5. System prüft die Eingaben mit denselben Regeln, erzeugt das PDF (UC-08) und speichert nichts.
6. Frontend startet den Download und zeigt den Hinweis „PDF erstellt, nicht gespeichert“ mit dem Angebot, ein Konto anzulegen.

**Alternativabläufe**

- **4a** Pflichtfeld fehlt oder eine Regel ist verletzt (z. B. Betrag 0 oder Zahlungsdatum in der Zukunft): Das Frontend markiert das Feld vor dem Absenden. Lehnt das Backend trotzdem ab (HTTP 400), zeigt das Frontend die Meldungen am jeweiligen Feld an.
- **5a** Das Backend ist nicht erreichbar: Das Frontend zeigt „Speichern fehlgeschlagen, bitte erneut versuchen“ (Besucher: „PDF konnte nicht erstellt werden“). Die Eingaben bleiben erhalten.
- **5b** Das Backend auf Render ist im Ruhezustand und startet erst: Das Frontend zeigt während der Wartezeit einen Ladehinweis statt eines Fehlers.
- **5c** Ein Besucher meldet sich nach dem Ausfüllen an oder registriert sich: Das Frontend behält die eingegebenen Belegdaten. Nach einer Registrierung hinterlegt er zuerst die Firmendaten (UC-03). Danach bietet das Formular „Beleg speichern“ an, und die Ausstellerdaten kommen aus den Firmendaten statt aus dem Formular.

### UC-05: Ankaufbeleg von Privat erstellen

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Besucher (ohne Speichern), Nutzer (mit Speichern, z. B. Gebrauchtwarenhändler) |
| Ziel | Den Ankauf gebrauchter Ware von einer Privatperson ohne Rechnung belegen. |
| Meilenstein | M4 (gleiche Route wie UC-04, nur `type` = `ANKAUF`), sonst finale Abgabe |
| Vorbedingungen | Wie UC-04: **Nutzer:** Firmendaten sind hinterlegt (UC-03). **Besucher:** Keine, er gibt die Ausstellerdaten im Formular ein, der Beleg wird nicht gespeichert. |
| Nachbedingung | **Nutzer:** Ein neuer `Receipt` mit `type` = `ANKAUF` ist gespeichert. **Besucher:** Es wird nichts gespeichert. In beiden Fällen enthält das PDF (UC-08) Unterschriftsfelder für Käufer und Verkäufer. |
| Backend-Route | Nutzer: `POST /api/receipts`. Besucher: `POST /api/receipts/pdf` (wie UC-04). |
| Frontend | `/belege/neu?typ=ankauf` (ohne Anmeldung erreichbar) |
| Screenshot | Eingabemaske „Neuer Ankaufbeleg“ mit Verkäuferdaten, gekauftem Artikel und Steuerauswahl `OHNE_UST`. |

Gleiche Felder wie in UC-04, mit diesen Unterschieden:

| Feld | Unterschied zu UC-04 |
| --- | --- |
| `type` | Fest `ANKAUF` |
| `payeeName` | Beschriftet als „Name des Verkäufers“, Pflicht |
| `payeeAddress` | Beschriftet als „Anschrift des Verkäufers“, **Pflicht** |
| `description` | Beschriftet als „Gekaufter Artikel“, möglichst mit Modell oder Seriennummer |
| `vatRate` | Vorbelegung `OHNE_UST`, da Privatpersonen keine Umsatzsteuer ausweisen |
| `reason` | Vorbelegt mit „Ankauf von Privatperson ohne Rechnung“, änderbar |

**Hauptablauf**

1. Nutzer klickt auf „Neuer Beleg → Ankauf von Privat“. Ein Besucher wählt auf der Startseite „Eigenbeleg erstellen“ und dann „Ankauf von Privat“.
2. System zeigt die Ankauf-Maske mit Vorbelegungen (`OHNE_UST`, `BAR`, Grund vorausgefüllt). Ohne Anmeldung steht darüber der Hinweis aus UC-04, dass der Beleg nicht gespeichert wird.
3. Nutzer bzw. Besucher erfasst Verkäufer, Anschrift, Artikel, Kaufdatum und Kaufpreis.
4. Nutzer klickt auf „Beleg speichern“, Besucher auf „PDF erstellen“.
5. System prüft die Eingaben. Für Nutzer vergibt es die nächste Belegnummer und speichert den Beleg. Für Besucher erzeugt es nur das PDF.
6. Nutzer sieht die Detailansicht (UC-07) mit dem Hinweis, das PDF auszudrucken und vom Verkäufer unterschreiben zu lassen. Der Besucher erhält direkt den PDF-Download mit demselben Hinweis.

**Alternativabläufe**

- **3a** Anschrift des Verkäufers fehlt: Das System verlangt sie, weil ein Ankaufbeleg ohne Anschrift des Verkäufers nicht nachvollziehbar ist.
- **3b** Nutzer ändert die Steuerauswahl auf `REGELSATZ_19`: Das System erlaubt das, zeigt aber den Hinweis „Bei Privatpersonen fällt in der Regel keine Umsatzsteuer an“.
- **5a** Fehler beim Speichern bzw. beim Erzeugen des PDFs: wie UC-04, Alternativablauf 5a.

### UC-06: Belegverlauf anzeigen

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Nutzer |
| Ziel | Alle eigenen gespeicherten Belege auf einen Blick sehen und einen davon zum Öffnen auswählen. Besucher haben keinen Verlauf, weil ihre Belege nicht gespeichert werden. |
| Meilenstein | M2 (Beispieldaten im Frontend, `v-for`), M3 (Daten über `GET /api/receipts`), M4 (Daten aus PostgreSQL) |
| Vorbedingungen | Ab finaler Abgabe: Nutzer ist angemeldet. Ein nicht angemeldeter Besucher wird zur Anmeldung umgeleitet (UC-02, 2b). |
| Nachbedingung | Die Liste zeigt alle Belege des Nutzers, neueste zuerst. Es werden keine Daten verändert. |
| Backend-Route | `GET /api/receipts` |
| Frontend | `/belege`, Komponente `ReceiptList.vue` |
| Screenshot | Belegverlauf mit mehreren Einträgen (Nummer, Datum, Empfänger, Art, Betrag in Euro) und der Schaltfläche „Neuer Beleg“. |

**Hauptablauf**

1. Nutzer öffnet „Meine Belege“. Nach dem Anmelden ist das die Startansicht.
2. Frontend ruft `GET /api/receipts` auf und zeigt währenddessen einen Ladehinweis.
3. System liefert die Belege des Nutzers als JSON.
4. Frontend rendert je Beleg eine Zeile mit `v-for`: `number`, `paymentDate`, `payeeName`, `type` (als „Allgemein“ oder „Ankauf“) und `amountCents` als Euro-Betrag („4,50 €“).
5. Nutzer klickt auf eine Zeile und gelangt zur Detailansicht (UC-07).

**Alternativabläufe**

- **3a** Es gibt noch keine Belege: Das Frontend zeigt „Noch keine Belege – lege deinen ersten Eigenbeleg an“ mit Verweis auf UC-04.
- **3b** Das Backend ist nicht erreichbar: Das Frontend zeigt eine Fehlermeldung mit der Schaltfläche „Erneut laden“.

### UC-07: Belegdetails anzeigen

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Nutzer |
| Ziel | Alle Angaben eines einzelnen gespeicherten Belegs prüfen, bevor er ausgedruckt oder abgelegt wird. |
| Meilenstein | Finale Abgabe |
| Vorbedingungen | Der Beleg existiert und gehört dem angemeldeten Nutzer. |
| Nachbedingung | Alle Felder des Belegs werden angezeigt. Es werden keine Daten verändert. |
| Backend-Route | `GET /api/receipts/{id}` |
| Frontend | `/belege/:id` |
| Screenshot | Detailansicht eines Ankaufbelegs mit allen Feldern und der Schaltfläche „PDF herunterladen“. |

**Hauptablauf**

1. Nutzer wählt im Belegverlauf einen Beleg aus.
2. Frontend ruft `GET /api/receipts/{id}` auf.
3. System liefert den Beleg.
4. Frontend zeigt alle Angaben lesbar an: Steuerauswahl als Text (z. B. „19 % USt enthalten“), Betrag in Euro, Daten im Format TT.MM.JJJJ.
5. Nutzer kann von hier das PDF herunterladen (UC-08) oder zum Verlauf zurückkehren.

**Alternativabläufe**

- **3a** Der Beleg existiert nicht oder gehört einem anderen Nutzer: Das System antwortet mit HTTP 404. Das Frontend zeigt „Beleg nicht gefunden“ und einen Link zum Verlauf. Fremde Belege werden bewusst nicht als „verboten“ gemeldet, damit sich ihre Existenz nicht erraten lässt.

### UC-08: Beleg als PDF herunterladen

| Feld | Inhalt |
| --- | --- |
| Priorität | Muss |
| Akteur | Besucher (direkt nach dem Ausfüllen), Nutzer (aus Detailansicht oder Verlauf) |
| Ziel | Einen einheitlich gestalteten Eigenbeleg als PDF erhalten, ausdrucken, unterschreiben und der Buchhaltung beilegen. |
| Meilenstein | Finale Abgabe |
| Vorbedingungen | **Nutzer:** Der Beleg existiert und gehört dem Nutzer, die Firmendaten (UC-03) sind hinterlegt. **Besucher:** Das Belegformular (UC-04/UC-05) ist gültig ausgefüllt, einschließlich der Ausstellerdaten. Das Logo ist für beide optional. |
| Nachbedingung | Der Browser lädt das PDF herunter (Nutzer: `EB-2026-0004.pdf`, Besucher: z. B. `Eigenbeleg-2026-10-18.pdf`). Für Nutzer kann dasselbe PDF jederzeit erneut erzeugt werden, weil es aus den gespeicherten Daten entsteht, einschließlich der Ausstellerdaten-Kopie. Spätere Änderungen an den Firmendaten ändern es nicht. Für Besucher wird nichts gespeichert. |
| Backend-Route | Nutzer: `GET /api/receipts/{id}/pdf`. Besucher: `POST /api/receipts/pdf` als `multipart/form-data` (Teil `receipt` mit den Belegdaten als JSON, optionaler Teil `logo`). Die Antwort ist das PDF. |
| Frontend | Nutzer: Schaltfläche „PDF herunterladen“ in `/belege/:id` und im Verlauf. Besucher: Schaltfläche „PDF erstellen“ im Belegformular. |
| Screenshot | Geöffnetes PDF mit Firmenkopf, Belegnummer, allen Angaben und Unterschriftszeile, bei hinterlegtem Logo zusätzlich mit Logo. |

**Inhalt des PDFs**

- Kopf: `issuerName` und `issuerAddress` aus dem Beleg (bei Nutzern die beim Speichern übernommene Kopie der Firmendaten, bei Besuchern die Angaben aus dem Formular), dazu das Logo, falls vorhanden. Das Logo ist nur Gestaltung und kommt bei Nutzern aus dem aktuellen Firmenprofil.
- Titel „Eigenbeleg“ oder „Ankaufbeleg“, dazu `number` (bei Besuchern nur, wenn sie eine Nummer eingetragen haben)
- `paymentDate`, `issueDate`, `payeeName`, `payeeAddress`, `description`
- Betrag in Euro, Steuerhinweis aus `vatRate`, `paymentMethod`
- `reason` („Grund für fehlenden Originalbeleg“)
- Unterschriftszeile des Ausstellers, bei `ANKAUF` zusätzlich eine für den Verkäufer

**Hauptablauf (Nutzer)**

1. Nutzer klickt in der Detailansicht auf „PDF herunterladen“.
2. Frontend ruft `GET /api/receipts/{id}/pdf` auf.
3. System erzeugt das PDF serverseitig aus dem gespeicherten Beleg (inklusive `issuerName` und `issuerAddress`) und dem Logo aus dem Firmenprofil.
4. Browser lädt die Datei mit der Belegnummer als Dateinamen herunter.

**Hauptablauf (Besucher)**

1. Besucher klickt im Belegformular auf „PDF erstellen“ (UC-04, UC-05).
2. Frontend sendet die Formulardaten per `POST /api/receipts/pdf` als `multipart/form-data` (Belegdaten als JSON-Teil, Logo als optionaler Datei-Teil).
3. System prüft die Daten und erzeugt das PDF mit den Ausstellerdaten aus dem Formular. Es speichert nichts.
4. Browser lädt die Datei herunter. Das Frontend weist darauf hin, dass der Beleg nicht gespeichert wurde.

**Alternativabläufe**

- **3a** Es gibt kein Logo: Das PDF wird ohne Logo erzeugt, der Firmenkopf bleibt. Das ist kein Fehler.
- **3b** Fehler bei der Erzeugung: Das System antwortet mit HTTP 500. Das Frontend zeigt „PDF konnte nicht erstellt werden“.
- **3c** Besucher sendet ungültige Daten: Das System antwortet mit HTTP 400, das Frontend zeigt die Meldungen am jeweiligen Feld.

### UC-09: Beleg bearbeiten oder löschen

| Feld | Inhalt |
| --- | --- |
| Priorität | Kann (nur bei übriger Zeit) |
| Akteur | Nutzer |
| Ziel | Tippfehler korrigieren oder versehentlich angelegte Belege entfernen. |
| Meilenstein | Finale Abgabe, falls Zeit bleibt |
| Vorbedingungen | Der Beleg existiert und gehört dem Nutzer. |
| Nachbedingung | Der Beleg ist geändert (`number` und `createdAt` bleiben gleich) oder gelöscht. |
| Backend-Route | `PUT /api/receipts/{id}`, `DELETE /api/receipts/{id}` |
| Frontend | „Bearbeiten“ und „Löschen“ in `/belege/:id` |
| Screenshot | Nur falls umgesetzt: Bearbeitungsmaske oder Rückfrage „Beleg wirklich löschen?“. |

**Hauptablauf**

1. Nutzer klickt in der Detailansicht auf „Bearbeiten“.
2. System zeigt die Maske aus UC-04 bzw. UC-05, vorausgefüllt mit den gespeicherten Werten.
3. Nutzer ändert Angaben und speichert. Das System prüft und speichert wie in UC-04.
4. Alternativ klickt der Nutzer auf „Löschen“, bestätigt die Rückfrage, und das System entfernt den Beleg.

### UC-10: Verlauf durchsuchen und filtern

| Feld | Inhalt |
| --- | --- |
| Priorität | Kann (nur bei übriger Zeit) |
| Akteur | Nutzer |
| Ziel | In einem langen Verlauf schnell einen bestimmten Beleg finden. |
| Meilenstein | Finale Abgabe, falls Zeit bleibt |
| Vorbedingungen | Der Nutzer hat mehrere Belege (UC-06). |
| Nachbedingung | Die Liste zeigt nur passende Belege. Es werden keine Daten verändert. |
| Backend-Route | Filterung im Frontend oder `GET /api/receipts?suche=...&typ=...&von=...&bis=...` |
| Frontend | Such- und Filterleiste über `ReceiptList.vue` |
| Screenshot | Nur falls umgesetzt: Verlauf mit aktivem Filter „Ankauf“ und Suchbegriff. |

**Hauptablauf**

1. Nutzer gibt im Verlauf einen Suchbegriff ein (Empfänger, Beschreibung oder Belegnummer).
2. Nutzer wählt optional Belegart und Zeitraum.
3. Die Liste zeigt sofort nur die passenden Belege und die Anzahl der Treffer.

## Zuordnung zu den Abgaben

### M2

**Umfang:** Vue.js-App auf GitHub, mindestens eine eigene Komponente, die eine Liste von Beispiel-Entitäten mit `v-for` rendert.

- Umgesetzt wird **UC-06 (Belegverlauf anzeigen)** in einer Grundversion.
- Eigene Komponente `ReceiptList.vue` rendert ein im Frontend fest hinterlegtes Array von Beispielbelegen mit `v-for` (gleiche Felder wie die Entity `Receipt`, z. B. die drei Beispiele aus dem `ReceiptController`).
- Für jeden Beleg werden Nummer, Datum, Empfänger, Art und Betrag in Euro angezeigt.
- **Abgabe:** Link zum Frontend-Repository auf GitHub.

### M3

**Umfang:** Frontend und Backend auf Render deployed, das Frontend ruft die GET-Route auf.

- **UC-06** lädt die Belege jetzt per `GET /api/receipts` vom Backend statt aus dem Frontend-Array.
- Backend auf Render als Web Service. Render hat keine eigene Java-Laufzeit, daher braucht es ein `Dockerfile` für die Spring-Boot-App.
- Frontend auf Render als Static Site. Die Backend-URL kommt aus einer Umgebungsvariable (z. B. `VITE_API_BASE_URL`).
- Im Backend wird CORS für die Render-Frontend-URL freigegeben, sonst blockiert der Browser den Aufruf.
- Ladehinweis und Fehlermeldung aus UC-06 (Alternativablauf 3b) einbauen. Kostenlose Render-Dienste schlafen nach Inaktivität ein, und der erste Aufruf dauert dann spürbar länger.
- **Abgabe:** Link zur Backend-API auf Render, z. B. `https://...onrender.com/api/receipts`.

### M4

**Umfang:** PostgreSQL-Datenbank in Render erstellt und mit dem Backend verbunden, das Frontend ruft die POST-Route auf und speichert eine neue Entität.

- Umgesetzt wird **UC-04 (Allgemeinen Eigenbeleg erstellen)**, wegen derselben Route idealerweise auch **UC-05 (Ankaufbeleg)**.
- PostgreSQL-Datenbank auf Render anlegen. Zugangsdaten nur als Umgebungsvariablen im Backend-Service, nicht im Repository.
- Backend: Spring Data JPA mit PostgreSQL-Treiber, `Receipt` wird JPA-Entity, `ReceiptRepository`, `POST /api/receipts` mit Validierung. `ownerId` bleibt bis zum Login leer. Der Besucher-Modus ohne Speichern (`POST /api/receipts/pdf`) folgt erst zur finalen Abgabe.
- **UC-06** liest jetzt aus der Datenbank, die fest codierten Beispielbelege entfallen.
- Frontend: Formular-Komponente `ReceiptForm.vue`. Nach dem Speichern erscheint der neue Beleg im Verlauf.
- Kostenlose Render-Datenbanken haben ein Ablaufdatum. Prüfe es im Render-Dashboard, damit die App bei der Bewertung noch läuft.
- **Abgabe:** Link zum Frontend auf Render.

### Finale Abgabe

**Umfang:** Frontend-Tests und Backend-Tests laufen automatisch über GitHub Actions. Die App ist auf der Render-Frontend-URL öffentlich erreichbar und selbsterklärend nutzbar.

- Alle Muss-Use-Cases sind umgesetzt: UC-01, UC-02, UC-03, UC-04, UC-05, UC-06, UC-07, UC-08. UC-09 und UC-10 nur bei übriger Zeit.
- Der Beleg-Generator (UC-04, UC-05, UC-08) ist ohne Konto nutzbar. Belege von Besuchern werden nicht gespeichert, Besucher geben die Ausstellerdaten im Formular ein. Nur angemeldete Nutzer haben einen Verlauf.
- Die fortlaufende Belegnummer ist Pflicht für gespeicherte Belege (automatisch nach dem Nummernkreis aus UC-03). Besucher können eine eigene Nummer eintragen.
- Nach UC-02 sehen Nutzer nur Belege mit eigener `ownerId`.
- **Backend-Tests** (JUnit 5, Spring Boot Test) und **Frontend-Tests** (z. B. Vitest mit Vue Test Utils) laufen in je einem GitHub-Actions-Workflow bei jedem Push und Pull Request (siehe [Testideen](#testideen-je-use-case)).
- „Selbsterklärend“ heißt hier: Leerzustände mit Handlungsaufforderung (UC-06, 3a), Hilfetexte an jedem Belegfeld (UC-04, Schritt 3), ein deutlicher Hinweis für Besucher, dass nichts gespeichert wird, verständliche Fehlermeldungen statt HTTP-Codes und eine Navigationsleiste mit „Neuer Beleg“ für alle sowie „Meine Belege“ und „Firmendaten“ für angemeldete Nutzer.
- **Abgabe:** Screenshot-Dokumentation mit einem Screenshot je Use Case (siehe nächster Abschnitt).

## Screenshot-Plan für die finale Abgabe

Jeder Screenshot wird auf der öffentlichen Render-Frontend-URL mit fiktiven Testdaten aufgenommen.

| Use Case | Was der Screenshot zeigt | Ansicht |
| --- | --- | --- |
| UC-01 | Ausgefülltes Registrierungsformular kurz vor „Konto erstellen“ | `/registrieren` |
| UC-02 | Anmeldeformular, zusätzlich Navigationsleiste mit angemeldeter E-Mail und „Abmelden“ | `/anmelden` |
| UC-03 | Firmendaten-Formular mit Nummernkreis und Logo-Vorschau nach dem Speichern | `/einstellungen/firma` |
| UC-04 | Eingabemaske „Neuer Eigenbeleg“, ausgefüllt, Steuerauswahl sichtbar. Zweiter Screenshot ohne Anmeldung mit dem Hinweis „Beleg wird nicht gespeichert“ und „PDF erstellen“ | `/belege/neu?typ=allgemein` |
| UC-05 | Eingabemaske „Neuer Ankaufbeleg“ mit Verkäuferanschrift und `OHNE_UST` | `/belege/neu?typ=ankauf` |
| UC-06 | Belegverlauf mit mindestens drei Belegen beider Arten | `/belege` |
| UC-07 | Detailansicht eines Belegs mit allen Feldern | `/belege/:id` |
| UC-08 | Heruntergeladenes PDF mit Firmenkopf, Belegnummer, allen Angaben und Unterschriftszeile (Logo nur, falls hinterlegt). Optional zusätzlich ein Besucher-PDF | PDF-Viewer |
| UC-09 | Nur falls umgesetzt: Bearbeitungsmaske oder Lösch-Rückfrage | `/belege/:id` |
| UC-10 | Nur falls umgesetzt: Verlauf mit aktivem Filter | `/belege` |

Tipp: Markiere im Screenshot das entscheidende Element (z. B. die Schaltfläche „Beleg speichern“) mit einem Rahmen und schreibe eine Zeile darunter, welcher Use Case gezeigt wird.

## Testideen je Use Case

| Use Case | Backend-Test | Frontend-Test |
| --- | --- | --- |
| UC-01 | Registrierung legt `User` an, doppelte E-Mail ergibt HTTP 409 | Formular zeigt Fehler bei abweichender Passwort-Wiederholung |
| UC-02 | Login mit falschem Passwort ergibt HTTP 401, `GET /api/receipts` ohne Login ergibt HTTP 401, `POST /api/receipts/pdf` ohne Login ist erlaubt | Router leitet `/belege` ohne Anmeldung auf `/anmelden` um, `/belege/neu` bleibt erreichbar |
| UC-03 | Speichern ohne Firmenname oder Anschrift ergibt HTTP 400, Speichern ohne Logo ist erlaubt, Logo über 1 MB oder kein Bild wird abgelehnt, nächste Belegnummer folgt dem Nummernkreis | Pflichtfelder werden markiert, Logo-Vorschau erscheint nach Dateiauswahl |
| UC-04 | `POST /api/receipts` speichert gültigen Beleg, Betrag 0 ergibt HTTP 400, `POST /api/receipts/pdf` speichert nichts (Anzahl Belege unverändert) und verlangt `issuerName` und `issuerAddress`, zwei gespeicherte Belege erhalten aufeinanderfolgende Nummern, eine Änderung der Firmendaten ändert `issuerName` und `issuerAddress` bereits gespeicherter Belege nicht, der erste Beleg eines neuen Jahres erhält die Nummer 1 | `ReceiptForm.vue` verhindert Absenden ohne Pflichtfelder, „4,50“ wird zu 450 Cent, ohne Anmeldung zeigt das Formular „PDF erstellen“ und den Hinweis statt „Beleg speichern“ |
| UC-05 | Ankauf ohne `payeeAddress` ergibt HTTP 400 | Ankauf-Maske ist mit `OHNE_UST` vorbelegt |
| UC-06 | `GET /api/receipts` liefert nur eigene Belege, neueste zuerst | `ReceiptList.vue` rendert je Beleg eine Zeile und zeigt den Leerzustand |
| UC-07 | Fremder oder unbekannter Beleg ergibt HTTP 404 | Detailansicht formatiert Betrag und Datum deutsch |
| UC-08 | PDF-Route liefert `application/pdf` mit Belegnummer im Dateinamen, PDF ohne Logo wird trotzdem erzeugt, Besucher-PDF trägt die eingegebene Belegnummer bzw. keine, wenn das Feld leer ist, Besucher-Anfrage als `multipart/form-data` funktioniert mit und ohne Logo-Teil | Klick auf „PDF herunterladen“ ruft die richtige URL auf |
