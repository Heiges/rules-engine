Dokumentiere das Feature "$ARGUMENTS" in `.claude/features/`.

Lies zunächst die relevanten Quelldateien, um die Dokumentation aus dem tatsächlichen Code abzuleiten — nicht aus Annahmen.

## Was zu tun ist

1. `.claude/features/$ARGUMENTS.md` anlegen oder aktualisieren mit diesen Pflichtabschnitten:

   - **Ziel**: warum dieses Feature existiert, welchen Mehrwert es liefert
   - **Anforderungen**: alle Invarianten, Verhaltensregeln und Randbedingungen
   - **Entscheidungen**: warum so implementiert und nicht anders (Alternativen, Trade-offs)
   - **Implementierung**: Tabelle mit allen Artefakt-Pfaden (Klassen, Tests, DTOs, Views, Komponenten …)
   - **Rekonstruktion**: minimaler Prompt + notwendiger Kontext, um das Feature aus dem Nichts wiederherzustellen

2. `.claude/features/_projekt-struktur.md` — im Abschnitt `## Bestehende Features` einen Link auf die neue Spec ergänzen (falls noch nicht vorhanden).

## Vorlage

```markdown
# <Feature-Name>

## Ziel

<Warum existiert dieses Feature?>

## Anforderungen

- <Invariante oder Verhaltensregel>

## Entscheidungen

- <Warum so und nicht anders>

## Implementierung

| Artefakt | Pfad |
|---|---|
| <Typ> | `<Pfad>` |

## Rekonstruktion

\`\`\`
<Minimaler Prompt zum Wiederherstellen>
\`\`\`

Kontext: <Was Claude wissen muss, damit der Prompt korrekt ausgeführt wird>
```

## Hinweise

- Dokumentation aus dem Code ableiten, nicht erfinden
- Entscheidungen nur eintragen, wenn der Grund nicht offensichtlich ist
- Pfade exakt wie im Dateisystem (keine Platzhalter)
