# Datenschutzkonzept (DSGVO): Eigenbeleg Generator

Dieses Dokument beschreibt, welche personenbezogenen Daten der Eigenbeleg Generator verarbeitet, auf welcher Rechtsgrundlage das geschieht, wie lange die Daten gespeichert werden und mit welchen Maßnahmen sie geschützt sind. Es baut auf den Use Cases in [`use-cases.md`](use-cases.md) auf.

> **Hinweis:** Hochschulprojekt (HTW Berlin, Team 31) mit ausschließlich fiktiven Testdaten. Dieses Dokument ist keine Rechtsberatung. Vor einem echten Betrieb mit echten Personen müssen die [offenen Punkte](#18-offene-punkte-vor-einem-echtbetrieb) geklärt und das Konzept fachkundig geprüft werden.

## Inhalt

1. [Rechtsrahmen](#1-rechtsrahmen)
2. [Begriffe](#2-begriffe)
3. [Rollen: Wer ist verantwortlich?](#3-rollen-wer-ist-verantwortlich)
4. [Welche Daten verarbeitet werden](#4-welche-daten-verarbeitet-werden)
5. [Grundsätze nach Art. 5 DSGVO](#5-grundsätze-nach-art-5-dsgvo)
6. [Rechtsgrundlagen nach Art. 6 DSGVO](#6-rechtsgrundlagen-nach-art-6-dsgvo)
7. [Verarbeitungsverzeichnis (Art. 30 DSGVO)](#7-verarbeitungsverzeichnis-art-30-dsgvo)
8. [Informationspflichten (Art. 12–14 DSGVO)](#8-informationspflichten-art-1214-dsgvo)
9. [Rechte der betroffenen Personen (Art. 15–21, 77 DSGVO)](#9-rechte-der-betroffenen-personen-art-1521-77-dsgvo)
10. [Technische und organisatorische Maßnahmen (Art. 25, 32 DSGVO)](#10-technische-und-organisatorische-maßnahmen-art-25-32-dsgvo)
11. [Hosting, Auftragsverarbeitung und Drittland (Art. 28, 44–46 DSGVO)](#11-hosting-auftragsverarbeitung-und-drittland-art-28-4446-dsgvo)
12. [Cookies und Endgerätezugriff (§ 25 TDDDG)](#12-cookies-und-endgerätezugriff--25-tdddg)
13. [Impressum (§ 5 DDG)](#13-impressum--5-ddg)
14. [Aufbewahrung von Belegen (§ 147 AO, § 257 HGB)](#14-aufbewahrung-von-belegen--147-ao--257-hgb)
15. [Datenpannen (Art. 33, 34 DSGVO)](#15-datenpannen-art-33-34-dsgvo)
16. [Datenschutz-Folgenabschätzung (Art. 35 DSGVO)](#16-datenschutz-folgenabschätzung-art-35-dsgvo)
17. [Zuordnung zu den Use Cases](#17-zuordnung-zu-den-use-cases)
18. [Offene Punkte vor einem Echtbetrieb](#18-offene-punkte-vor-einem-echtbetrieb)
19. [Quellen](#19-quellen)

## 1. Rechtsrahmen

| Gesetz | Wofür es hier gilt |
| --- | --- |
| **DSGVO** (Verordnung (EU) 2016/679) | Grundregeln für jede Verarbeitung personenbezogener Daten: Grundsätze, Rechtsgrundlagen, Betroffenenrechte, Sicherheit, Auftragsverarbeitung, Drittlandübermittlung |
| **BDSG** (Bundesdatenschutzgesetz) | Ergänzt die DSGVO in Deutschland, z. B. zu Datenschutzbeauftragten (§ 38 BDSG) und Bußgeldverfahren. Für dieses Projekt ohne eigene Sonderregeln. |
| **TDDDG** (Telekommunikation-Digitale-Dienste-Datenschutz-Gesetz, bis Mai 2024 „TTDSG“) | § 25 TDDDG regelt, wann Informationen auf dem Endgerät gespeichert oder ausgelesen werden dürfen (Cookies, Local Storage) |
| **DDG** (Digitale-Dienste-Gesetz, ersetzt seit Mai 2024 das TMG) | § 5 DDG regelt die Impressumspflicht |
| **AO** (Abgabenordnung) | § 147 AO: Aufbewahrungspflicht für Buchungsbelege. Sie trifft die Nutzer, nicht den Betreiber der App. |
| **HGB** (Handelsgesetzbuch) | § 257 HGB: handelsrechtliche Aufbewahrungspflicht für Kaufleute, inhaltlich parallel zu § 147 AO |
| **GoBD** (BMF-Schreiben, keine Rechtsnorm, aber Verwaltungsauffassung) | Anforderungen an elektronische Buchführung, z. B. Nachvollziehbarkeit und Unveränderbarkeit von Belegen |

## 2. Begriffe

Die Begriffe stammen aus Art. 4 DSGVO:

| Begriff | Fundstelle | Bedeutung im Projekt |
| --- | --- | --- |
| Personenbezogene Daten | Art. 4 Nr. 1 DSGVO | Alle Angaben zu einer identifizierbaren natürlichen Person, z. B. E-Mail, Name und Anschrift eines privaten Verkäufers, IP-Adresse. Daten einer GmbH (z. B. „Parkhaus Mitte GmbH“) sind keine personenbezogenen Daten, der Name eines Einzelunternehmers dagegen schon. |
| Verarbeitung | Art. 4 Nr. 2 DSGVO | Jeder Umgang mit den Daten: erheben, speichern, anzeigen, als PDF ausgeben, löschen |
| Verantwortlicher | Art. 4 Nr. 7 DSGVO | Wer über Zweck und Mittel entscheidet (siehe Abschnitt 3) |
| Auftragsverarbeiter | Art. 4 Nr. 8 DSGVO | Wer Daten im Auftrag eines Verantwortlichen verarbeitet, z. B. ein Hosting-Anbieter |

## 3. Rollen: Wer ist verantwortlich?

| Daten | Verantwortlicher | Begründung |
| --- | --- | --- |
| Konto, Sitzung, Firmenprofil, Server-Logs | **Betreiber** (Team 31) | Der Betreiber entscheidet, welche Kontodaten nötig sind und wie die App arbeitet. |
| Inhalt gespeicherter Belege, insbesondere Daten von Zahlungsempfängern und privaten Verkäufern (`payeeName`, `payeeAddress`) | **Nutzer** (das Unternehmen, das den Beleg ausstellt) | Der Nutzer erfasst die Daten für seine eigene Buchführung. Der Betreiber speichert sie nur in seinem Auftrag und ist insoweit **Auftragsverarbeiter** nach Art. 28 DSGVO. |
| Gast-Modus (Beleg ohne Konto, UC-03, UC-04, UC-07) | **Gast** für den Belegsinhalt, **Betreiber** für die technische Verarbeitung der Anfrage | Die Daten werden nur für die Dauer einer Anfrage im Arbeitsspeicher verarbeitet und nicht gespeichert. |
| Hosting | **Render Services, Inc.** als (Unter-)Auftragsverarbeiter | Siehe Abschnitt 11 |

Folgen für einen Echtbetrieb:

- Mit jedem Nutzer bräuchte es einen **Auftragsverarbeitungsvertrag** (AVV) nach Art. 28 Abs. 3 DSGVO, z. B. als Teil der Nutzungsbedingungen beim Registrieren (UC-01).
- Render darf nach Art. 28 Abs. 2 und 4 DSGVO als Unterauftragsverarbeiter nur eingesetzt werden, wenn die Nutzer darüber informiert sind und Render vertraglich dieselben Pflichten übernimmt.

## 4. Welche Daten verarbeitet werden

| Daten | Beispiel | Betroffene Person | Use Case | Pflicht? | Gespeichert? |
| --- | --- | --- | --- | --- | --- |
| E-Mail | `erika@example.org` | Nutzer | UC-01 | Pflicht | Ja, bis zur Löschung des Kontos |
| Passwort-Hash | BCrypt-Hash, nie das Passwort im Klartext | Nutzer | UC-01 | Pflicht | Ja, bis zur Löschung des Kontos |
| Sitzungscookie | zufällige Sitzungs-ID | Nutzer | UC-01 | technisch nötig | Im Browser bis Abmeldung oder Ablauf |
| Firmenname, Inhaber | „Erikas Ankauf, Inh. Erika Musterfrau“ | Nutzer (bei Einzelunternehmern personenbezogen) | UC-02 | Firmenname Pflicht, Inhaber optional | Ja |
| Firmenanschrift (Anschrift des Ausstellers) | „Lindenallee 22, 10249 Berlin“ | Nutzer | UC-02 | Pflicht | Ja |
| Steuernummer | „27/123/45678“ | Nutzer | UC-02 | optional | Ja, wenn angegeben |
| Logo | PNG/JPG, höchstens 1 MB | Nutzer (meist nicht personenbezogen, außer es zeigt z. B. ein Foto) | UC-02 | optional | Ja, wenn hochgeladen |
| Zahlungsempfänger bzw. Verkäufer: `payeeName`, `payeeAddress` | „Max Mustermann, Beispielweg 5“ | **Dritte**: private Verkäufer, Einzelunternehmer | UC-03, UC-04, UC-05, UC-06, UC-07 | Name Pflicht, Anschrift bei Ankauf Pflicht | Bei Nutzern ja, bei Gästen nein |
| Weitere Belegdaten: `description`, `amountCents`, `paymentDate`, `issueDate`, `reason`, `paymentMethod`, `number`, `ownerId` | „Gebrauchtes Notebook, 180 €“ | Nutzer und Dritte (über den Bezug zum Beleg) | UC-03 bis UC-07 | siehe Use Cases | Bei Nutzern ja, bei Gästen nein |
| Ausstellerdaten am Beleg: `issuerName`, `issuerAddress` | „Max Händler, Hauptstr. 1“ | Nutzer bzw. Gast | UC-03, UC-04, UC-07 | Pflicht | Bei Nutzern ja, als Kopie der Firmendaten am gespeicherten Beleg. Bei Gästen **nein**, nur für die Dauer der Anfrage. |
| Optionales Logo und eigene Belegnummer im Gast-Modus | Bilddatei, „EB-2026-0001“ | Gast | UC-03, UC-04, UC-07 | optional | **Nein**, nur für die Dauer der Anfrage |
| IP-Adresse, Zeitpunkt, aufgerufene URL, Browserkennung | `203.0.113.7` | alle Gast und Nutzer | alle | technisch nötig | In Server-Logs des Hosters (siehe V6) |

**Besondere Kategorien (Art. 9 DSGVO)** wie Gesundheitsdaten verarbeitet die App nicht gezielt. In die Freitextfelder `description` und `reason` könnten Nutzer sie aber eintragen, z. B. „Zuzahlung Apotheke“. Das Formular sollte deshalb einen kurzen Hinweis zeigen, keine Gesundheits- oder vergleichbar sensiblen Angaben einzutragen.

## 5. Grundsätze nach Art. 5 DSGVO

| Grundsatz | Fundstelle | Umsetzung |
| --- | --- | --- |
| Rechtmäßigkeit, Treu und Glauben, Transparenz | Art. 5 Abs. 1 lit. a DSGVO | Rechtsgrundlagen aus Abschnitt 6, Datenschutzerklärung in der App (Abschnitt 8) |
| Zweckbindung | Art. 5 Abs. 1 lit. b DSGVO | Daten werden nur zum Erstellen, Speichern und Ausgeben von Belegen genutzt. Es gibt keine Werbung, kein Tracking und keine Weitergabe. |
| Datenminimierung | Art. 5 Abs. 1 lit. c DSGVO | Konto braucht nur E-Mail und Passwort. Logo und Steuernummer sind optional. Der Gast-Modus speichert gar nichts. |
| Richtigkeit | Art. 5 Abs. 1 lit. d DSGVO | Firmendaten sind änderbar (UC-02), Belege optional ebenfalls (UC-08). Gespeicherte Belege behalten die Ausstellerdaten als Kopie (`issuerName`, `issuerAddress`, UC-03), damit eine spätere Adressänderung alte Belege nicht verfälscht. |
| Speicherbegrenzung | Art. 5 Abs. 1 lit. e DSGVO | Löschfristen im Verarbeitungsverzeichnis (Abschnitt 7) |
| Integrität und Vertraulichkeit | Art. 5 Abs. 1 lit. f DSGVO | Maßnahmen aus Abschnitt 10 |
| Rechenschaftspflicht | Art. 5 Abs. 2 DSGVO | Dieses Dokument und das Verarbeitungsverzeichnis |

## 6. Rechtsgrundlagen nach Art. 6 DSGVO

Jede Verarbeitung braucht eine der Rechtsgrundlagen aus Art. 6 Abs. 1 DSGVO. Die App kommt **ohne Einwilligung** (lit. a) aus. Das ist bewusst so, weil eine Einwilligung jederzeit widerrufen werden kann (Art. 7 Abs. 3 DSGVO) und dann die Grundlage für den Dienst wegfiele.

| Rechtsgrundlage | Wofür |
| --- | --- |
| **Art. 6 Abs. 1 lit. b DSGVO** (Vertrag bzw. vorvertragliche Maßnahmen) | Konto anlegen und führen, Anmeldung, Firmenprofil, Belege speichern und als PDF ausgeben. Bei Gästen: Erzeugen des PDFs auf deren Anfrage. Die Nutzung der App ist der Vertrag, die Verarbeitung ist dafür erforderlich. |
| **Art. 6 Abs. 1 lit. c DSGVO** (rechtliche Verpflichtung) | Gilt für den **Nutzer** als Verantwortlichen für die Belegsinhalte: Er muss Buchungsbelege aufzeichnen und aufbewahren (§ 147 AO, § 257 HGB). Daraus folgt die Befugnis, Name und Anschrift von Zahlungsempfängern und Verkäufern zu erfassen. Hinzu kommt lit. b gegenüber dem Verkäufer (Kaufvertrag). |
| **Art. 6 Abs. 1 lit. f DSGVO** (berechtigtes Interesse) | Server-Logs mit IP-Adresse, um Angriffe zu erkennen und den Betrieb zu sichern. Abwägung: Das Interesse an einem sicheren Dienst überwiegt, weil die Logs nur kurz gespeichert, nicht ausgewertet und nicht weitergegeben werden. Betroffene können nach Art. 21 DSGVO widersprechen. |

## 7. Verarbeitungsverzeichnis (Art. 30 DSGVO)

Nach Art. 30 Abs. 5 DSGVO sind kleine Organisationen unter 250 Beschäftigten teilweise ausgenommen. Die Ausnahme greift aber nicht, wenn die Verarbeitung nicht nur gelegentlich erfolgt. Bei einer dauerhaft betriebenen App ist das der Fall, deshalb gibt es hier ein Verzeichnis.

| Nr. | Verarbeitung | Datenkategorien | Betroffene | Rechtsgrundlage | Empfänger | Löschfrist | Use Case |
| --- | --- | --- | --- | --- | --- | --- | --- |
| V1 | Benutzerkonto | E-Mail, Passwort-Hash | Nutzer | Art. 6 Abs. 1 lit. b DSGVO | Render (Hosting) | Löschung sofort mit dem Konto | UC-01 |
| V2 | Sitzung | Sitzungs-ID im Cookie | Nutzer | Art. 6 Abs. 1 lit. b DSGVO, § 25 Abs. 2 Nr. 2 TDDDG | Render | gelöscht beim Abmelden, sonst nach 30 Minuten Inaktivität (Spring-Boot-Standard) | UC-01 |
| V3 | Firmenprofil | Firmenname, Inhaber, Firmenanschrift, Steuernummer, Logo, Nummernkreis | Nutzer | Art. 6 Abs. 1 lit. b DSGVO | Render | Löschung mit dem Konto, Logo sofort beim Entfernen (UC-02, 5b) | UC-02 |
| V4 | Gespeicherte Belege | alle Belegfelder inkl. `payeeName`, `payeeAddress` und der Ausstellerdaten-Kopie `issuerName`, `issuerAddress` | Nutzer, Zahlungsempfänger, private Verkäufer | Betreiber: Art. 6 Abs. 1 lit. b DSGVO (Auftrag des Nutzers); Nutzer: Art. 6 Abs. 1 lit. c DSGVO i. V. m. § 147 AO, § 257 HGB | Render | Löschung durch den Nutzer (UC-09) oder mit dem Konto. Die 8-jährige Aufbewahrung ist Pflicht des Nutzers, nicht des Betreibers (Abschnitt 14). | UC-03 bis UC-09 |
| V5 | Gast-PDF | Belegfelder, `issuerName`, `issuerAddress`, optionales Logo | Gast, Zahlungsempfänger, Verkäufer | Art. 6 Abs. 1 lit. b DSGVO | Render (nur Durchleitung) | nicht gespeichert: nur im Arbeitsspeicher während der Anfrage, danach sofort verworfen | UC-03, UC-04, UC-07 |
| V6 | Server-Logs | IP-Adresse, Zeitpunkt, URL, Statuscode, Browserkennung | alle | Art. 6 Abs. 1 lit. f DSGVO | Render | nach der Aufbewahrungsdauer von Render, Ziel höchstens 30 Tage (prüfen, siehe offene Punkte) | alle |

Weitere Pflichtangaben nach Art. 30 Abs. 1 DSGVO:

- **Verantwortlicher und Kontakt:** Team 31 (Hüseyin Surhay Yilmaz, Oguzhan Cabar), HTW Berlin. Eine Kontakt-E-Mail für Datenschutzanfragen muss vor dem Echtbetrieb festgelegt werden.
- **Drittlandübermittlung:** möglich über Render, siehe Abschnitt 11.
- **Technische und organisatorische Maßnahmen:** siehe Abschnitt 10.

## 8. Informationspflichten (Art. 12–14 DSGVO)

**Datenschutzerklärung in der App (Art. 13 DSGVO).** Die Daten werden direkt bei Nutzern und Gästen erhoben. Deshalb braucht die App eine Seite `/datenschutz`, die auf jeder Seite im Fußbereich verlinkt ist und beim Registrieren (UC-01) sowie im Belegformular (UC-03, UC-04) erreichbar ist. Inhalt nach Art. 13 Abs. 1 und 2 DSGVO:

- Name und Kontaktdaten des Verantwortlichen
- Zwecke und Rechtsgrundlagen (Abschnitt 6)
- berechtigtes Interesse bei den Server-Logs
- Empfänger: Render als Hosting-Anbieter
- Drittlandübermittlung und die Garantie, auf die sie gestützt ist (Abschnitt 11)
- Speicherdauer (Abschnitt 7)
- die Rechte aus Art. 15 bis 21 DSGVO und das Beschwerderecht nach Art. 77 DSGVO
- ob die Angabe der Daten erforderlich ist: E-Mail und Passwort für ein Konto ja, für den Gast-Modus nicht

Nach Art. 12 Abs. 1 DSGVO muss die Erklärung präzise, verständlich und in klarer Sprache sein.

**Daten von Verkäufern und Zahlungsempfängern.** Diese Pflicht trifft den **Nutzer** als Verantwortlichen, nicht die App:

- Beim Ankauf (UC-04) erhebt der Nutzer die Daten direkt beim Verkäufer. Dann gilt Art. 13 DSGVO. Die App kann helfen, indem das Ankauf-PDF optional einen kurzen Datenschutzhinweis für den Verkäufer enthält.
- Stammen die Daten nicht von der Person selbst, gilt Art. 14 DSGVO. Die Information kann nach Art. 14 Abs. 5 lit. c DSGVO entfallen, soweit die Erfassung durch Rechtsvorschriften wie § 147 AO ausdrücklich geregelt ist. Ob das im Einzelfall reicht, muss der Nutzer selbst beurteilen.

## 9. Rechte der betroffenen Personen (Art. 15–21, 77 DSGVO)

Anfragen müssen nach Art. 12 Abs. 3 DSGVO **innerhalb eines Monats** beantwortet werden.

| Recht | Fundstelle | Umsetzung in der App |
| --- | --- | --- |
| Auskunft | Art. 15 DSGVO | Nutzer sehen ihre Kontodaten, Firmendaten (UC-02) und alle Belege (UC-05, UC-06). Für eine vollständige Kopie nach Art. 15 Abs. 3 DSGVO braucht es einen Export (siehe offene Punkte). |
| Berichtigung | Art. 16 DSGVO | Firmendaten ändern (UC-02). Belege ändern (UC-08, „Kann“). Ohne UC-08 per Anfrage an den Betreiber. |
| Löschung | Art. 17 DSGVO | Beleg löschen (UC-09). Konto löschen mit allen Daten fehlt noch in den Use Cases (siehe offene Punkte). |
| Einschränkung | Art. 18 DSGVO | Per Anfrage: Daten werden gesperrt statt gelöscht, z. B. solange die Richtigkeit bestritten ist. |
| Datenübertragbarkeit | Art. 20 DSGVO | Export der eigenen Belege und Firmendaten als JSON oder CSV (siehe offene Punkte). Gilt, weil die Verarbeitung auf einem Vertrag beruht (Art. 20 Abs. 1 lit. a DSGVO). |
| Widerspruch | Art. 21 DSGVO | Gegen die Server-Logs (lit. f). Der Betreiber muss dann prüfen, ob zwingende Gründe für die Verarbeitung überwiegen. |
| Beschwerde bei der Aufsichtsbehörde | Art. 77 DSGVO | Zuständig in Berlin: die Berliner Beauftragte für Datenschutz und Informationsfreiheit. Wird in der Datenschutzerklärung genannt. |

Für **Dritte** (Verkäufer, Zahlungsempfänger) ist der Nutzer der Ansprechpartner. Als Auftragsverarbeiter unterstützt der Betreiber ihn dabei (Art. 28 Abs. 3 lit. e DSGVO).

## 10. Technische und organisatorische Maßnahmen (Art. 25, 32 DSGVO)

Art. 25 DSGVO verlangt Datenschutz durch Technikgestaltung und datenschutzfreundliche Voreinstellungen. Art. 32 DSGVO verlangt ein dem Risiko angemessenes Schutzniveau.

| Bereich | Maßnahme |
| --- | --- |
| Transport | Nur HTTPS. Render stellt TLS-Zertifikate für `*.onrender.com` bereit. |
| Passwörter | Hash mit BCrypt (`BCryptPasswordEncoder` aus Spring Security), mindestens 8 Zeichen (UC-01). Das Klartext-Passwort wird nie gespeichert oder geloggt. |
| Sitzung | Cookie mit `HttpOnly`, `Secure` und `SameSite=Lax`. CSRF-Schutz von Spring Security aktiv lassen, weil die Anmeldung über ein Cookie läuft. |
| Zugriffskontrolle | Jede Abfrage filtert nach `ownerId`. Fremde Belege ergeben HTTP 404 (UC-06, 3a), damit sich ihre Existenz nicht erraten lässt. |
| Geheimnisse | Datenbank-Zugang nur als Umgebungsvariable auf Render, nie im Repository (siehe M4 in den Use Cases). |
| CORS | Backend erlaubt Anfragen nur von der eigenen Frontend-URL. |
| Eingaben | Bean Validation im Backend mit Längenbegrenzungen. Logo nur als PNG oder JPG, höchstens 1 MB, Dateityp am Dateiinhalt prüfen und nicht nur an der Endung. |
| Logs | Keine Request-Bodies und keine Belegdaten ins Log schreiben, nur technische Angaben. |
| Datenminimierung (Art. 25 Abs. 2 DSGVO) | Gast-Modus ohne Speicherung als Standard, optionale Felder bleiben leer, keine Analyse- oder Tracking-Werkzeuge. |
| Keine externen Dienste im Browser | Schriften, Skripte und Bilder werden selbst ausgeliefert. Das Einbinden von Google Fonts direkt von Google-Servern wurde vom LG München I (Urteil vom 20.01.2022, Az. 3 O 17493/20) als unzulässige Übermittlung der IP-Adresse gewertet. |
| Verfügbarkeit | Regelmäßige Datenbank-Sicherungen. Ob der gewählte Render-Tarif Sicherungen enthält, ist zu prüfen. |

## 11. Hosting, Auftragsverarbeitung und Drittland (Art. 28, 44–46 DSGVO)

**Render als Auftragsverarbeiter (Art. 28 DSGVO).** Frontend, Backend und Datenbank laufen bei Render Services, Inc. (USA). Render stellt einen Auftragsverarbeitungsvertrag (Data Processing Addendum) bereit. Er nennt die Unterauftragsverarbeiter und verweist auf die Liste unter `render.com/trust`. Für einen Echtbetrieb muss der DPA abgeschlossen bzw. akzeptiert werden.

**Übermittlung in ein Drittland (Art. 44 ff. DSGVO).** Render ist ein US-Unternehmen. Eine Übermittlung personenbezogener Daten in die USA ist nur zulässig, wenn eine der folgenden Grundlagen vorliegt:

1. **Angemessenheitsbeschluss (Art. 45 DSGVO):** Der Durchführungsbeschluss (EU) 2023/1795 vom 10.07.2023 zum EU-US Data Privacy Framework erlaubt Übermittlungen an US-Unternehmen, die nach dem Framework zertifiziert sind. Ob Render zertifiziert ist, muss auf der offiziellen Liste geprüft werden (siehe offene Punkte).
2. **Geeignete Garantien (Art. 46 DSGVO):** Andernfalls Standardvertragsklauseln nach Art. 46 Abs. 2 lit. c DSGVO (Durchführungsbeschluss (EU) 2021/914) als Teil des DPA.

**Region.** Die Dienste sollten in einer Region in der EU angelegt werden, sofern Render sie anbietet (z. B. Frankfurt). Das senkt das Risiko. Eine Übermittlung bleibt aber möglich, weil der Anbieter ein US-Unternehmen ist und Support- oder Unterauftragsverarbeiter außerhalb der EU sitzen können.

**GitHub** speichert nur den Quellcode und darin keine Nutzerdaten. Es ist deshalb kein Auftragsverarbeiter für die App.

## 12. Cookies und Endgerätezugriff (§ 25 TDDDG)

- § 25 Abs. 1 TDDDG verlangt eine Einwilligung, bevor Informationen auf dem Endgerät gespeichert oder ausgelesen werden.
- **Ausnahme nach § 25 Abs. 2 Nr. 2 TDDDG:** Keine Einwilligung ist nötig, wenn die Speicherung unbedingt erforderlich ist, damit ein vom Nutzer ausdrücklich gewünschter Dienst bereitgestellt werden kann.
- Das Sitzungscookie für die Anmeldung (UC-01) fällt unter diese Ausnahme. Ebenso eine Zwischenspeicherung des Belegformulars, die nur dazu dient, Eingaben nach dem Anmelden zu behalten (UC-03, 5c).
- Weil die App keine Analyse-, Werbe- oder Tracking-Cookies nutzt, braucht sie **kein Cookie-Banner**. Über das Sitzungscookie informiert die Datenschutzerklärung.

## 13. Impressum (§ 5 DDG)

- § 5 DDG verpflichtet Anbieter **geschäftsmäßiger, in der Regel gegen Entgelt angebotener** digitaler Dienste zu einem Impressum.
- Ein nicht kommerzielles Hochschulprojekt fällt voraussichtlich nicht darunter.
- Trotzdem empfohlen: eine Seite „Über das Projekt“ mit Namen des Teams, Hochschule und Kontakt-E-Mail. Den Kontakt verlangt Art. 13 Abs. 1 lit. a DSGVO ohnehin.

## 14. Aufbewahrung von Belegen (§ 147 AO, § 257 HGB)

- Eigenbelege sind **Buchungsbelege**. Nach § 147 Abs. 1 Nr. 4 und Abs. 3 AO sowie § 257 Abs. 1 Nr. 4 und Abs. 4 HGB sind sie **8 Jahre** aufzubewahren. Bis Ende 2024 waren es 10 Jahre. Verkürzt hat die Frist das Vierte Bürokratieentlastungsgesetz (BGBl. 2024 I Nr. 323), mit einer Übergangsregel in Art. 97 § 19a EGAO.
- Diese Pflicht trifft den **Nutzer**. Der Betreiber ist nicht verpflichtet, Belege 8 Jahre vorzuhalten.
- Löschwunsch und Aufbewahrungspflicht: Will ein Nutzer sein Konto löschen, gilt Art. 17 DSGVO. Die Ausnahme nach Art. 17 Abs. 3 lit. b DSGVO (rechtliche Pflicht zur Aufbewahrung) greift für den Betreiber nicht, weil die Pflicht beim Nutzer liegt. Die App muss deshalb vor dem Löschen deutlich darauf hinweisen und einen Export aller PDFs anbieten.
- **GoBD:** Nach dem Grundsatz der Unveränderbarkeit muss erkennbar bleiben, was an einem gespeicherten Beleg geändert wurde. Wird UC-08 (bearbeiten) umgesetzt, sollte eine Änderung protokolliert oder als Korrekturbeleg angelegt werden, statt den Beleg still zu überschreiben.

## 15. Datenpannen (Art. 33, 34 DSGVO)

Wird z. B. die Datenbank-URL mit Passwort versehentlich auf GitHub veröffentlicht:

1. Sofort den Zugang sperren bzw. das Passwort auf Render ändern und den Commit-Verlauf bereinigen.
2. Prüfen, welche Daten betroffen sind und ob ein Risiko für Personen besteht.
3. Besteht ein Risiko, wird die Aufsichtsbehörde **innerhalb von 72 Stunden** informiert (Art. 33 Abs. 1 DSGVO).
4. Bei voraussichtlich **hohem Risiko** werden auch die betroffenen Personen benachrichtigt (Art. 34 DSGVO).
5. Jede Panne wird dokumentiert, auch wenn keine Meldung nötig war (Art. 33 Abs. 5 DSGVO).

Als Auftragsverarbeiter meldet der Betreiber Pannen bei Belegsinhalten unverzüglich an den betroffenen Nutzer (Art. 33 Abs. 2 DSGVO).

## 16. Datenschutz-Folgenabschätzung (Art. 35 DSGVO)

Eine Folgenabschätzung ist nur bei voraussichtlich **hohem Risiko** nötig, insbesondere in den Fällen des Art. 35 Abs. 3 DSGVO. Keiner davon liegt vor:

- keine automatisierte Bewertung von Personen mit Rechtswirkung (lit. a)
- keine umfangreiche Verarbeitung besonderer Kategorien nach Art. 9 DSGVO (lit. b)
- keine systematische Überwachung öffentlicher Bereiche (lit. c)

Die Datenmenge ist klein, und Belegdaten werden nicht ausgewertet. Eine Folgenabschätzung ist daher nicht erforderlich. Diese Einschätzung ist zu wiederholen, wenn neue Funktionen dazukommen (z. B. eine Auswertung von Ankäufen je Verkäufer).

**Bußgelder (Art. 83 DSGVO):** Verstöße können mit bis zu 20 Mio. € oder 4 % des weltweiten Jahresumsatzes geahndet werden, je nachdem, welcher Betrag höher ist. Für ein Hochschulprojekt mit fiktiven Daten ist das nicht praktisch relevant, zeigt aber, warum die Maßnahmen oben im Echtbetrieb ernst zu nehmen sind.

## 17. Zuordnung zu den Use Cases

| Use Case | Personenbezogene Daten | Rechtsgrundlage | Verzeichnis |
| --- | --- | --- | --- |
| UC-01 Registrieren, Anmelden und Abmelden | E-Mail, Passwort-Hash, Sitzungscookie | Art. 6 Abs. 1 lit. b DSGVO, § 25 Abs. 2 Nr. 2 TDDDG | V1, V2 |
| UC-02 Firmendaten hinterlegen | Firmenname, Inhaber, Anschrift, Steuernummer, Logo | Art. 6 Abs. 1 lit. b DSGVO | V3 |
| UC-03 Allgemeinen Eigenbeleg erstellen | Belegdaten, `issuerName`, `issuerAddress` (bei Nutzern Kopie der Firmendaten, bei Gästen aus dem Formular) | Art. 6 Abs. 1 lit. b DSGVO, für den Nutzer zusätzlich lit. c | V4 (Nutzer), V5 (Gast) |
| UC-04 Ankaufbeleg erstellen | Name und Anschrift privater Verkäufer | Art. 6 Abs. 1 lit. b und lit. c DSGVO | V4, V5 |
| UC-05 Belegverlauf anzeigen | eigene Belege | Art. 6 Abs. 1 lit. b DSGVO | V4 |
| UC-06 Belegdetails anzeigen | eigene Belege | Art. 6 Abs. 1 lit. b DSGVO | V4 |
| UC-07 PDF herunterladen | Belegdaten im PDF | Art. 6 Abs. 1 lit. b DSGVO | V4, V5 |
| UC-08 Beleg bearbeiten | eigene Belege | Art. 6 Abs. 1 lit. b DSGVO, Art. 16 DSGVO | V4 |
| UC-09 Beleg löschen | eigene Belege | Art. 6 Abs. 1 lit. b DSGVO, Art. 17 DSGVO | V4 |
| UC-10 Verlauf durchsuchen | eigene Belege | Art. 6 Abs. 1 lit. b DSGVO | V4 |
| alle | IP-Adresse in Server-Logs | Art. 6 Abs. 1 lit. f DSGVO | V6 |

## 18. Offene Punkte vor einem Echtbetrieb

- [ ] **Render und Data Privacy Framework:** Auf der offiziellen Liste prüfen, ob Render Services, Inc. zertifiziert ist. Bisher gibt es dafür nur einen Hinweis aus Renders Community-Forum. Sonst auf die Standardvertragsklauseln im DPA stützen.
- [ ] **Region:** Beim Anlegen der Render-Dienste eine EU-Region wählen und dokumentieren.
- [ ] **Log-Aufbewahrung bei Render:** Tatsächliche Dauer im gewählten Tarif prüfen und V6 anpassen.
- [ ] **Konto löschen und Daten exportieren:** In den Use Cases fehlen bisher „Konto löschen“ (Art. 17 DSGVO) und „Daten exportieren“ (Art. 15 Abs. 3, Art. 20 DSGVO). Beide als eigene Use Cases ergänzen, Export auch wegen der Aufbewahrungspflicht der Nutzer (Abschnitt 14).
- [ ] **Datenschutzerklärung:** Seite `/datenschutz` mit den Inhalten aus Abschnitt 8 erstellen und verlinken.
- [ ] **Kontakt-E-Mail** für Datenschutzanfragen festlegen.
- [ ] **AVV mit Nutzern:** Muster für den Auftragsverarbeitungsvertrag (Art. 28 Abs. 3 DSGVO) in die Nutzungsbedingungen aufnehmen.
- [ ] **Ausstellerdaten-Kopie umsetzen:** In UC-03 ist festgelegt, dass gespeicherte Belege `issuerName` und `issuerAddress` als Kopie der Firmendaten behalten (Art. 5 Abs. 1 lit. d DSGVO, GoBD). Die zwei Felder gibt es in `Receipt.java` bereits. Offen ist das Befüllen beim Speichern (Projektplan, F-16).
- [ ] **Änderungen protokollieren**, falls UC-08 umgesetzt wird (GoBD, Abschnitt 14).
- [ ] **Hinweis an Freitextfeldern**, keine sensiblen Daten im Sinne von Art. 9 DSGVO einzutragen.
- [ ] **Datenbank-Sicherungen** im gewählten Render-Tarif prüfen.

## 19. Quellen

- DSGVO, Verordnung (EU) 2016/679: <https://eur-lex.europa.eu/eli/reg/2016/679/oj>
- BDSG, TDDDG, DDG, AO, HGB, EGAO: amtliche Fassungen auf <https://www.gesetze-im-internet.de>
- Durchführungsbeschluss (EU) 2023/1795 (EU-US Data Privacy Framework): <https://eur-lex.europa.eu/eli/dec_impl/2023/1795/oj>
- Durchführungsbeschluss (EU) 2021/914 (Standardvertragsklauseln): <https://eur-lex.europa.eu/eli/dec_impl/2021/914/oj>
- Liste zertifizierter Unternehmen im Data Privacy Framework: <https://www.dataprivacyframework.gov/list>
- Render Data Processing Addendum: <https://render.com/dpa>
- Viertes Bürokratieentlastungsgesetz, verkürzte Aufbewahrungsfristen: <https://www.haufe.de/steuern/gesetzgebung-politik/viertes-buerokratieentlastungsgesetz_168_613390.html>
