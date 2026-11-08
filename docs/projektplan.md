# Projektplan: Eigenbeleg Generator

Dieser Plan listet alle Funktionen des Eigenbeleg Generators, ordnet sie den Abgaben M1 bis zur finalen Abgabe zu und beschreibt die Arbeitspakete in Backend und Frontend. Grundlage sind [`use-cases.md`](use-cases.md) und [`dsgvo.md`](dsgvo.md).

Stand: 19.10.2026. Die Abgabetermine stehen im Kurs und sind hier nicht eingetragen.

## Inhalt

1. [Stand auf einen Blick](#1-stand-auf-einen-blick)
2. [Funktionsliste](#2-funktionsliste)
3. [Arbeitspakete je Meilenstein](#3-arbeitspakete-je-meilenstein)
4. [Datenmodell](#4-datenmodell)
5. [API-Übersicht](#5-api-übersicht)
6. [Frontend-Seiten](#6-frontend-seiten)
7. [Tests](#7-tests)
8. [Reihenfolge und Abhängigkeiten](#8-reihenfolge-und-abhängigkeiten)
9. [Risiken](#9-risiken)
10. [Definition of Done](#10-definition-of-done)

## 1. Stand auf einen Blick

| Meilenstein | Inhalt | Status |
| --- | --- | --- |
| M1 | Spring-Boot-App, Entity `Receipt`, `GET /api/receipts` mit Beispieldaten | Erledigt |
| M2 | Vue-App mit eigener Komponente und `v-for`-Liste auf GitHub | In Arbeit: App lokal fertig, Push auf GitHub fehlt |
| M3 | Frontend und Backend auf Render, Frontend ruft die GET-Route auf | Offen |
| M4 | PostgreSQL auf Render, Frontend speichert über die POST-Route | Offen |
| Finale Abgabe | alle Muss-Use-Cases, Tests in GitHub Actions, Screenshot-Doku | Offen |

## 2. Funktionsliste

Spalte „Bezug“: Use Case aus `use-cases.md`, DSGVO-Pflicht aus `dsgvo.md` oder rein technische Voraussetzung („Technik“).

| ID | Funktion | Bezug | Backend | Frontend | Meilenstein | Abhängig von | Status |
| --- | --- | --- | --- | --- | --- | --- | --- |
| F-01 | Beispielbelege abrufen | UC-05 | `ReceiptController`, `GET /api/receipts` | – | M1 | – | Erledigt |
| F-02 | Entity `Receipt` mit Eingaberegeln und Aussteller-Feldern | UC-03, UC-04 | `Receipt.java` mit Bean Validation, `issuerName`, `issuerAddress`, `ReceiptValidationTest` | `src/types/receipt.ts` | M1 (Felder), M4 (Regeln aktiv) | F-01 | Erledigt |
| F-03 | Belegliste mit `v-for` | UC-05 | – | `ReceiptList.vue`, Beispieldaten, Tests | M2 | F-02 | Erledigt (lokal) |
| F-04 | Frontend-Repository auf GitHub | Technik | – | leeres Repo anlegen, committen, pushen | M2 | F-03 | Offen |
| F-05 | Backend auf Render | Technik | `Dockerfile`, `server.port=${PORT:8080}` | – | M3 | F-01 | Offen |
| F-06 | Frontend auf Render und CORS | Technik, UC-05 | CORS für die Frontend-URL | Static Site, `VITE_API_BASE_URL` | M3 | F-04, F-05 | Offen |
| F-07 | Verlauf lädt vom Backend | UC-05 | – | `fetch` auf `GET /api/receipts`, Lade- und Fehlerzustand | M3 | F-06 | Offen |
| F-08 | Datenbank anbinden | Technik, UC-05 | PostgreSQL auf Render, Spring Data JPA, `ReceiptRepository` | – | M4 | F-05 | Offen |
| F-09 | Beleg speichern | UC-03, UC-04 | `POST /api/receipts` mit `@Valid`, Fehler als Feldmeldungen | – | M4 | F-02, F-08 | Offen |
| F-10 | Belegformular | UC-03, UC-04 | – | `ReceiptForm.vue` für beide Belegarten, Euro-Eingabe in Cent | M4 | F-09 | Offen |
| F-11 | Fortlaufende Belegnummer | UC-02, UC-03 | M4: fortlaufend über alle Belege. Final: je Nutzer und Jahr nach Nummernkreis | – | M4, Finale | F-08 | Offen |
| F-12 | Registrieren | UC-01 | Spring Security, Entity `User`, BCrypt, `POST /api/auth/register` | `/registrieren` | Finale | F-08 | Offen |
| F-13 | Anmelden und Abmelden | UC-01 | `POST /api/auth/login`, `POST /api/auth/logout`, Sitzungscookie | `/anmelden`, Router-Guard, Navigationsleiste | Finale | F-12 | Offen |
| F-14 | Nur eigene Belege sehen | UC-01, UC-05, UC-06 | Filter nach `ownerId`, fremde Belege ergeben 404 | – | Finale | F-13 | Offen |
| F-15 | Firmendaten, Nummernkreis und Logo | UC-02 | Entity `CompanyProfile`, `GET /api/company-profile`, `PUT /api/company-profile`, `POST /api/company-profile/logo`, Logo entfernen | `/einstellungen/firma` | Finale | F-13 | Offen |
| F-16 | Ausstellerdaten-Kopie beim Speichern | UC-02, UC-03 | `issuerName`, `issuerAddress` aus `CompanyProfile` übernehmen | – | Finale | F-09, F-15 | Offen |
| F-17 | Belegdetails | UC-06 | `GET /api/receipts/{id}` | `/belege/:id` | Finale | F-14 | Offen |
| F-18 | PDF für gespeicherte Belege | UC-07 | `GET /api/receipts/{id}/pdf` mit PDF-Bibliothek (z. B. OpenPDF) | Schaltfläche „PDF herunterladen“ | Finale | F-16, F-17 | Offen |
| F-19 | Gast-Modus ohne Speichern | UC-03, UC-04, UC-07 | `POST /api/receipts/pdf` als `multipart/form-data` | Formular ohne Anmeldung mit Hinweis und „PDF erstellen“ | Finale | F-10, F-18 | Offen |
| F-20 | Datenschutzerklärung | DSGVO Art. 13 | – | Seite `/datenschutz`, Link im Fußbereich | Finale | – | Offen |
| F-21 | Konto löschen | DSGVO Art. 17 | `DELETE /api/account` löscht Konto, Firmendaten und Belege | Bestätigungsdialog mit Export-Hinweis | Finale | F-13, F-22 | Offen |
| F-22 | Daten exportieren | DSGVO Art. 15, 20 | `GET /api/account/export` (JSON) | Schaltfläche in den Einstellungen | Finale | F-14, F-15 | Offen |
| F-23 | Tests automatisch in GitHub Actions | Technik | Workflow mit `./gradlew test` | Workflow mit `npm ci` und `vitest run` | Finale | F-04 | Offen |
| F-24 | Selbsterklärende Oberfläche | UC-03, UC-05 | verständliche Fehlermeldungen | Hilfetexte, Leerzustände, Hinweis „nicht gespeichert“ | Finale | F-10, F-19 | Offen |
| F-25 | Screenshot-Dokumentation | Technik, UC-01 bis UC-07 | – | ein Screenshot je Use Case laut Screenshot-Plan | Finale | alle Muss-Funktionen | Offen |
| F-26 | Beleg bearbeiten (Kann) | UC-08 | `PUT /api/receipts/{id}`, Änderungen protokollieren (GoBD) | „Bearbeiten“ in `/belege/:id` | Finale, nur bei Zeit | F-17 | Offen |
| F-27 | Beleg löschen (Kann) | UC-09 | `DELETE /api/receipts/{id}` | „Löschen“ mit Rückfrage in `/belege/:id` | Finale, nur bei Zeit | F-17 | Offen |
| F-28 | Verlauf durchsuchen und filtern (Kann) | UC-10 | optional `GET /api/receipts` mit Filterparametern | Such- und Filterleiste | Finale, nur bei Zeit | F-14 | Offen |

## 3. Arbeitspakete je Meilenstein

### M1

**Abgabe:** erledigt.

- [x] Spring-Boot-App mit Java 25 und Gradle
- [x] Entity `Receipt` mit Enums `ReceiptType`, `VatRate`, `PaymentMethod`
- [x] `GET /api/receipts` mit drei Beispielbelegen
- [x] Nachgezogen am 19.10.2026: `issuerName`, `issuerAddress` und Eingaberegeln in `Receipt.java`, dazu 11 neue Backend-Tests

### M2

**Abgabe:** Link zum Frontend-Repository.

- [x] Vue-3-App mit TypeScript und Vite im Ordner `eigenbeleg-generator-frontend`
- [x] Eigene Komponente `ReceiptList.vue` rendert die Beispielbelege mit `v-for`
- [x] 6 Frontend-Tests und Produktions-Build grün
- [ ] ZIP-Datei und Temp-Datei `ziGtn9gw` aus dem Frontend-Ordner entfernen, damit sie nicht mit hochgeladen werden
- [ ] Leeres Repository auf GitHub anlegen
- [ ] Commit und Push auf `main`
- [ ] Link zum Repository abgeben

### M3

**Abgabe:** Link zur Backend-API auf Render, z. B. `…onrender.com/api/receipts`.

- [ ] `Dockerfile` für das Backend: Bauen mit Gradle und JDK 25, Ausführen mit einem schlanken JRE-25-Image. Render hat keine eigene Java-Laufzeit.
- [ ] `application.properties`: `server.port=${PORT:8080}`, weil Render den Port über die Umgebungsvariable `PORT` vorgibt
- [ ] Backend als Web Service auf Render anlegen, EU-Region wählen (siehe `dsgvo.md`, Abschnitt 11)
- [ ] CORS im Backend: nur die Frontend-URL erlauben, am besten aus einer Umgebungsvariable wie `FRONTEND_URL`
- [ ] Frontend als Static Site auf Render: Build-Befehl `npm ci && npm run build`, Ausgabeordner `dist`, Umgebungsvariable `VITE_API_BASE_URL`
- [ ] `App.vue` lädt die Belege per `GET /api/receipts` statt aus `sampleReceipts.ts`, mit Ladehinweis und Fehlermeldung „Erneut laden“ (UC-05, 3b)
- [ ] Kaltstart testen: Render-Dienste im kostenlosen Tarif schlafen ein, der erste Aufruf dauert länger

### M4

**Abgabe:** Link zum Frontend auf Render.

- [ ] PostgreSQL-Datenbank auf Render anlegen und Ablaufdatum des kostenlosen Tarifs notieren
- [ ] Abhängigkeiten `spring-boot-starter-data-jpa` und PostgreSQL-Treiber in `build.gradle`
- [ ] Zugangsdaten nur als Umgebungsvariablen. Achtung: Render zeigt eine URL der Form `postgresql://…`, Spring braucht `jdbc:postgresql://host:port/datenbank` mit Benutzer und Passwort getrennt.
- [ ] `Receipt` wird JPA-Entity: `@Entity`, `@Id`, Enums mit `@Enumerated(EnumType.STRING)`
- [ ] `ReceiptRepository extends JpaRepository<Receipt, UUID>`
- [ ] `POST /api/receipts` mit `@Valid @RequestBody Receipt`: System setzt `id`, `number`, `createdAt`. Antwort 201 mit dem gespeicherten Beleg.
- [ ] Fehlerbehandlung: `@RestControllerAdvice` wandelt Validierungsfehler in HTTP 400 mit Feld → Meldung um. Die deutschen Meldungen stehen schon in `Receipt.java`.
- [ ] Fortlaufende Belegnummer über alle Belege (F-11, M4-Variante)
- [ ] `GET /api/receipts` liest aus der Datenbank, die Beispielbelege im Controller entfallen
- [ ] Frontend: `ReceiptForm.vue` für allgemeinen und Ankaufbeleg, „4,50“ wird zu 450 Cent, Feldmeldungen anzeigen, nach dem Speichern erscheint der Beleg im Verlauf

### Finale Abgabe

**Abgabe:** Screenshot-Dokumentation mit einem Screenshot je Use Case.

1. **Konten:** F-12 Registrieren, F-13 Anmelden und Abmelden, F-14 nur eigene Belege
2. **Firmendaten:** F-15 mit Nummernkreis und optionalem Logo, F-16 Ausstellerdaten-Kopie, F-11 Nummer je Nutzer und Jahr
3. **Belege ansehen und ausgeben:** F-17 Details, F-18 PDF, F-19 Gast-Modus
4. **Datenschutz:** F-20 Datenschutzerklärung, F-21 Konto löschen, F-22 Daten exportieren
5. **Qualität:** F-23 GitHub Actions für Backend und Frontend, F-24 selbsterklärende Oberfläche
6. **Abschluss:** F-25 Screenshot-Dokumentation auf der öffentlichen Render-URL
7. **Nur bei Zeit:** F-26 bearbeiten, F-27 löschen, F-28 suchen und filtern

## 4. Datenmodell

**`Receipt`** (vorhanden, 16 Felder)

| Feld | Typ | Herkunft |
| --- | --- | --- |
| `id` | `UUID` | System |
| `number` | `String` (höchstens 30) | System, bei Gästen optional selbst eingetragen |
| `type` | `ReceiptType` | Eingabe, Pflicht |
| `issueDate`, `paymentDate` | `LocalDate` | Eingabe, Pflicht, Zahlung nicht in der Zukunft, Ausstellung nicht vor Zahlung |
| `payeeName` | `String` (höchstens 200) | Eingabe, Pflicht |
| `payeeAddress` | `String` (höchstens 500) | Eingabe, Pflicht bei `ANKAUF` |
| `description`, `reason` | `String` (höchstens 500) | Eingabe, Pflicht |
| `amountCents` | `Long` | Eingabe, Pflicht, größer als 0 |
| `vatRate` | `VatRate` | Eingabe, Pflicht |
| `paymentMethod` | `PaymentMethod` | Eingabe, Pflicht |
| `issuerName` (höchstens 200), `issuerAddress` (höchstens 500) | `String` | System: Kopie aus `CompanyProfile` beim Speichern |
| `createdAt` | `Instant` | System |
| `ownerId` | `UUID` | System, ab Login |

**`User`** (geplant, F-12): `id`, `email` (eindeutig), `passwordHash`, `createdAt`.

**`CompanyProfile`** (geplant, F-15): `id`, `ownerId`, `companyName`, `ownerName` (optional), `address`, `taxNumber` (optional), `logo` (optional, Bilddaten oder Dateiverweis), `numberPrefix` (Standard `EB`), `nextNumber`, `numberYear`.

## 5. API-Übersicht

| Methode und Pfad | Zweck | Use Case | Zugriff | Meilenstein |
| --- | --- | --- | --- | --- |
| `GET /api/receipts` | Belegverlauf | UC-05 | M1–M4 öffentlich, final nur angemeldet | M1 |
| `POST /api/receipts` | Beleg speichern | UC-03, UC-04 | M4 öffentlich, final nur angemeldet | M4 |
| `GET /api/receipts/{id}` | Belegdetails | UC-06 | angemeldet, nur eigene | Finale |
| `GET /api/receipts/{id}/pdf` | PDF eines gespeicherten Belegs | UC-07 | angemeldet, nur eigene | Finale |
| `POST /api/receipts/pdf` | Gast-PDF ohne Speichern, `multipart/form-data` | UC-03, UC-04, UC-07 | öffentlich | Finale |
| `PUT /api/receipts/{id}` | Beleg bearbeiten (Kann) | UC-08 | angemeldet, nur eigene | Finale |
| `DELETE /api/receipts/{id}` | Beleg löschen (Kann) | UC-09 | angemeldet, nur eigene | Finale |
| `POST /api/auth/register` | Registrieren | UC-01 | öffentlich | Finale |
| `POST /api/auth/login` | Anmelden | UC-01 | öffentlich | Finale |
| `POST /api/auth/logout` | Abmelden | UC-01 | angemeldet | Finale |
| `GET /api/company-profile` | Firmendaten lesen | UC-02 | angemeldet | Finale |
| `PUT /api/company-profile` | Firmendaten und Nummernkreis speichern | UC-02 | angemeldet | Finale |
| `POST /api/company-profile/logo` | Logo hochladen | UC-02 | angemeldet | Finale |
| `DELETE /api/company-profile/logo` | Logo entfernen (UC-02, 5b), neu im Plan | UC-02 | angemeldet | Finale |
| `DELETE /api/account` | Konto löschen, neu aus der DSGVO | DSGVO Art. 17 | angemeldet | Finale |
| `GET /api/account/export` | Daten exportieren, neu aus der DSGVO | DSGVO Art. 15, 20 | angemeldet | Finale |

## 6. Frontend-Seiten

| Pfad | Inhalt | Use Case | Zugriff | Meilenstein |
| --- | --- | --- | --- | --- |
| `/` | Startseite mit „Als Gast fortfahren“, Anmelden, Registrieren | UC-01, UC-03 | öffentlich | Finale (bis M4 zeigt `/` den Verlauf) |
| `/belege` | Belegverlauf (`ReceiptList.vue`) | UC-05 | angemeldet | M2 |
| `/belege/neu?typ=allgemein` und `?typ=ankauf` | Belegformular (`ReceiptForm.vue`) | UC-03, UC-04 | öffentlich | M4 |
| `/belege/:id` | Belegdetails mit PDF-Download | UC-06, UC-07 | angemeldet | Finale |
| `/registrieren` | Registrierung | UC-01 | öffentlich | Finale |
| `/anmelden` | Anmeldung | UC-01 | öffentlich | Finale |
| `/einstellungen/firma` | Firmendaten, Nummernkreis, Logo, Export, Konto löschen | UC-02, DSGVO | angemeldet | Finale |
| `/datenschutz` | Datenschutzerklärung | DSGVO Art. 13 | öffentlich | Finale |

Für mehrere Seiten braucht das Frontend Vue Router. Er wird spätestens mit M4 ergänzt, weil dann Verlauf und Formular getrennte Seiten sind. Für die Static Site auf Render ist eine Rewrite-Regel `/*` → `/index.html` nötig, damit direkte Aufrufe wie `/belege/neu` funktionieren.

## 7. Tests

| Bereich | Vorhanden | Geplant |
| --- | --- | --- |
| Backend | 12 Tests: Kontext lädt, 9 Validierungsregeln (`ReceiptValidationTest`), 2 JSON-Tests für `GET /api/receipts` (`ReceiptControllerTest`) | je Funktion die Testideen aus `use-cases.md`, z. B. `POST /api/receipts` speichert, 404 bei fremden Belegen, PDF-Inhalt |
| Frontend | 6 Tests: `ReceiptList.vue` (Zeilen, Formatierung, Sortierung, Summe, Leerzustand) und `App.vue` | `ReceiptForm.vue`, Router-Guard, Lade- und Fehlerzustände |
| Automatisch | – | F-23: GitHub Actions bei jedem Push und Pull Request, in beiden Repositories |

## 8. Reihenfolge und Abhängigkeiten

Der kritische Pfad bis zur finalen Abgabe:

```text
F-03 Belegliste ─► F-04 GitHub ─► F-05/F-06 Render ─► F-07 Laden vom Backend
                                        │
                                        └─► F-08 Datenbank ─► F-09 Speichern ─► F-10 Formular
                                                   │
                                                   └─► F-12 Registrieren ─► F-13 Anmelden ─► F-14 eigene Belege
                                                                                   │
                                                                                   ├─► F-15 Firmendaten ─► F-16 Kopie ─► F-18 PDF ─► F-19 Gast
                                                                                   └─► F-17 Details
```

Parallel zum kritischen Pfad möglich: F-20 Datenschutzerklärung (jederzeit), F-23 GitHub Actions (sobald F-04 erledigt ist) und F-24 Oberfläche (laufend).

Aufteilen im Team: Ab M3 bietet sich eine Trennung nach Backend und Frontend an, weil die Schnittstelle in Abschnitt 5 festgelegt ist.

## 9. Risiken

| Risiko | Auswirkung | Gegenmaßnahme |
| --- | --- | --- |
| Kostenlose Render-Datenbank läuft ab | App funktioniert bei der Bewertung nicht | Ablaufdatum notieren, rechtzeitig neu anlegen |
| Kaltstart der Render-Dienste | erster Aufruf dauert spürbar länger, wirkt wie ein Fehler | Ladehinweis (UC-03 5b, UC-05), vor Screenshots einmal aufrufen |
| JDK 25 im Docker-Image | Build schlägt fehl, wenn das Image kein Java 25 hat | Image mit JDK bzw. JRE 25 wählen und lokal mit `docker build` testen |
| Datenbank-URL im falschen Format | Backend startet auf Render nicht | URL in `jdbc:postgresql://…` umwandeln (Abschnitt 3, M4) |
| Zugangsdaten landen auf GitHub | Datenpanne (`dsgvo.md`, Abschnitt 15) | nur Umgebungsvariablen, vor jedem Push `git status` prüfen |
| Spring Security sperrt bestehende Routen | Frontend erhält 401 oder 403 | Freigaben laut Abschnitt 5 von Anfang an konfigurieren und testen |
| PDF-Erzeugung dauert länger als geplant | finale Abgabe gefährdet | F-18 früh beginnen, einfache Gestaltung zuerst |

## 10. Definition of Done

Eine Funktion gilt als erledigt, wenn:

1. sie die Abläufe des zugehörigen Use Cases inklusive Alternativabläufen erfüllt,
2. Backend- und Frontend-Tests dafür vorhanden und grün sind,
3. sie auf Render läuft (ab M3),
4. keine personenbezogenen Daten ins Log oder auf GitHub gelangen,
5. die Oberfläche ohne Erklärung verständlich ist,
6. der Status in diesem Plan aktualisiert ist.
