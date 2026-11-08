# Deployment auf Render (M3)

Backend und Frontend laufen auf [Render](https://render.com) als zwei getrennte Dienste:

| Dienst | Render-Typ | Repository | Ergebnis |
| --- | --- | --- | --- |
| Backend | Web Service (Docker) | <https://github.com/oguzhankc/Eigenbeleg-Generator> | `https://<backend>.onrender.com/api/receipts` |
| Frontend | Static Site | <https://github.com/Surhay276/eigenbeleg-generator-frontend> | `https://<frontend>.onrender.com` |

Render holt den Code von GitHub. Deshalb müssen alle Änderungen vor dem Deployment committet und gepusht sein.

## Reihenfolge

1. Backend anlegen, weil das Frontend dessen Adresse braucht
2. Frontend mit der Backend-Adresse anlegen
3. Frontend-Adresse im Backend eintragen (CORS)

## 1. Backend als Web Service

1. Auf render.com anmelden, **New → Web Service**.
2. Repository wählen. Beide Repositories sind öffentlich und können auch über „Public Git Repository“ mit ihrer URL eingebunden werden.
3. Einstellungen:

   | Feld | Wert |
   | --- | --- |
   | Language / Runtime | Docker |
   | Branch | `main` |
   | Region | Frankfurt (EU Central), wegen der DSGVO (`dsgvo.md`, Abschnitt 11) |
   | Instance Type | Free |

4. **Create Web Service**. Render baut das Image aus dem `Dockerfile`. Der erste Build dauert einige Minuten.
5. Sobald der Status „Live“ ist, im Browser `https://<backend>.onrender.com/api/receipts` öffnen. Dort muss die JSON-Liste mit 3 Belegen erscheinen.

Die Variable `PORT` setzt Render selbst. Das Backend liest sie über `server.port=${PORT:8080}`.

## 2. Frontend als Static Site

1. **New → Static Site**, Frontend-Repository wählen.
2. Einstellungen:

   | Feld | Wert |
   | --- | --- |
   | Branch | `main` |
   | Build Command | `npm ci && npm run build` |
   | Publish Directory | `dist` |

3. Unter **Environment** zwei Variablen anlegen:

   | Variable | Wert | Warum |
   | --- | --- | --- |
   | `VITE_API_BASE_URL` | `https://<backend>.onrender.com` | Adresse des Backends. Vite baut sie beim Build fest ein. |
   | `NODE_VERSION` | `24` | `package.json` verlangt Node 22.18 oder neuer |

4. Unter **Redirects/Rewrites** eine Regel anlegen: Source `/*`, Destination `/index.html`, Action **Rewrite**. So funktionieren auch direkte Aufrufe von Unterseiten des Vue Routers.
5. **Create Static Site** und warten, bis der Status „Live“ ist.

Wird `VITE_API_BASE_URL` später geändert, muss das Frontend neu gebaut werden: **Manual Deploy → Deploy latest commit**.

## 3. Frontend-Adresse im Backend eintragen

1. Im Backend-Dienst unter **Environment** die Variable `FRONTEND_URL` anlegen.
2. Wert: die Frontend-Adresse genau so, wie sie im Browser steht, mit `https://` und **ohne** `/` am Ende, z. B. `https://eigenbeleg-frontend.onrender.com`.
3. Speichern. Render startet das Backend neu.

Ohne diesen Schritt blockiert der Browser die Anfrage des Frontends. In der Browser-Konsole erscheint dann ein CORS-Fehler.

## 4. Prüfen

| Prüfung | Erwartung |
| --- | --- |
| `https://<backend>.onrender.com/api/receipts` | JSON mit 3 Belegen |
| `https://<frontend>.onrender.com` | Seite „Meine Belege“ mit 3 Belegen, nicht mit 5 Beispielbelegen |
| Browser-Entwicklerwerkzeuge (F12) → Netzwerk | Aufruf von `…/api/receipts` mit Status 200 |
| Browser-Konsole | keine CORS-Fehler |

**Abgabe M3:** der Link `https://<backend>.onrender.com/api/receipts`.

## Gut zu wissen

- **Kaltstart:** Dienste im kostenlosen Tarif schlafen nach etwa 15 Minuten ohne Aufruf ein. Der erste Aufruf danach dauert deutlich länger. Das Frontend zeigt solange „Belege werden geladen …“. Vor der Abgabe den Link einmal aufrufen.
- **Arbeitsspeicher:** Der kostenlose Tarif hat 512 MB. Das `Dockerfile` begrenzt Java auf 75 % davon (`-XX:MaxRAMPercentage=75`).
- **Lokal testen wie auf Render:**

  ```bash
  docker build -t eigenbeleg-backend .
  docker run --rm -e PORT=9090 -e FRONTEND_URL=http://localhost:5173 -p 9090:9090 eigenbeleg-backend
  ```

  Danach liefert `http://localhost:9090/api/receipts` die Belege.
