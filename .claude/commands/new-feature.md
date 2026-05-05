Du begleitest die Entwicklung eines neuen Features. Das Vorgehen ist zweiphasig.

---

## Phase 1 — Freie Beschreibung

Bitte den Nutzer, das Feature offen zu beschreiben:

> "Was soll das Feature leisten? Beschreibe es so, wie du es dem Team erklären würdest — ohne Technologiebegriffe, einfach was der Nutzer damit tun kann."

Lass den Nutzer antworten, ohne vorher Struktur vorzugeben. Fasse das Beschriebene danach in 3–5 Sätzen zusammen und frage, ob du es richtig verstanden hast.

---

## Phase 2 — Schichten-Review (bei Bedarf)

Gehe die betroffenen Schichten durch — aber **nur die, die tatsächlich betroffen sind**. Leite jede Schicht mit einer kurzen Einschätzung ein ("Diese Schicht ist betroffen, weil …" oder "Diese Schicht ist voraussichtlich nicht betroffen.") und stelle dann gezielte Fragen.

### Domain (`coreElements`)
- Entstehen neue Klassen oder Records? Welche fachlichen Schlüssel (Name, ID)?
- Ändern sich bestehende Domänenklassen? Welche Invarianten müssen gelten?
- Referenz: @.claude/conventions/coding.md, @.claude/blueprints/layers/domain.md

### Persistence (`persistence`)
- Müssen neue Objekte gespeichert werden? In welchem Format (XML)?
- Ändern sich bestehende DTOs oder XML-Strukturen?
- Referenz: @.claude/blueprints/layers/persistence-local-xml.md, @.claude/appspecs/persistence/domain.md

### API (`api`)
- Welche neuen Endpunkte sind nötig (CRUD + domänenspezifisch)?
- Werden neue DTOs gebraucht oder reichen bestehende?
- Referenz: @.claude/appspecs/api/domain.md

### Frontend (`frontend`)
- Welche Views sind neu oder werden geändert?
- Welche Kacheln erscheinen auf HomeView oder PlayerView?
- Gibt es neue Navigationsschritte? Breadcrumb-Label nötig?
- Rolleneinschränkung (nur Spielleiter, nur Spieler, beide)?
- Referenz: @.claude/conventions/patterns.md, @.claude/appspecs/navigation/tile-navigation.md

---

## Abschluss — Specs schreiben

Wenn du dir sicher bist, dass du das Feature vollständig verstanden hast:

1. Lege Specs in `.claude/appspecs/<domäne>/` an oder aktualisiere bestehende — je nach betroffener Schicht `domain.md`, `persistence.md`, `view.md`.
2. Prüfe, ob neue **generische** Muster entstehen, die eine Blueprint in `.claude/blueprints/` rechtfertigen.
3. Trage neue Specs in `.claude/appspecs/README.md` unter "Bestehende Specs" ein.
4. Fasse ab, welche Artefakte du angelegt oder geändert hast, und welche Implementierungsschritte als nächstes folgen würden.

$ARGUMENTS
