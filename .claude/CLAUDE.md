# Projektspezifische Claude-Konfiguration

Ergänzende Hinweise für Claude, die nicht in die Root-CLAUDE.md gehören.

## Verzeichniskonventionen

<!-- Wo liegt was, Namenskonventionen für Pakete/Module -->


## Verbotene Aktionen

<!-- Was Claude in diesem Projekt nie tun soll, z.B. bestimmte Dateien nicht ändern -->
Beim Löschen von Dateien oder Verzeichnissen immer eine Bestätigung einholen!

## Testrichtlinien

<!-- Wie Tests geschrieben und ausgeführt werden -->

## Feature-Dokumentation

Nach jeder Feature-Implementierung (neu oder geändert) eine Feature-Spec anlegen oder aktualisieren:

- Datei: `.claude/features/<feature-name>.md`
- Pflichtabschnitte: **Ziel**, **Anforderungen**, **Entscheidungen**, **Implementierung** (Tabelle mit Artefakt-Pfaden), **Rekonstruktion** (Prompt + Kontext)
- Außerdem: `_projekt-struktur.md` im Abschnitt `## Bestehende Features` aktualisieren

Das gilt für alle Implementierungs-Tasks — nicht nur für `/new-core-element` oder `/new-rule`.
Vorlage und Prozess: `/document-feature <name>`
