# Attribute — View-Spezifikation

## Views

| View | Route | Datei |
|------|-------|-------|
| `AttributeView` | `/tile/attributes`, `/world/attributes` | [frontend/…/AttributeView.tsx](../../../frontend/src/views/AttributeView.tsx) |
| `AttributeDetailView` | `/tile/attributes/:index`, `/world/attributes/:index` | [frontend/…/AttributeDetailView.tsx](../../../frontend/src/views/AttributeDetailView.tsx) |

## AttributeView

Listenansicht aller Attribute. Wird für zwei Kontexte verwendet (Regelwerk-Bearbeitung und Spielwelt), gesteuert über Props.

### Props

| Prop | Typ | Bedeutung |
|------|-----|-----------|
| `allowGrouping` | `boolean` | Zeigt Gruppen-Spalte und „Gruppieren"-Aktion an |
| `detailBasePath` | `string` | Basispfad für Detail-Navigation (`{detailBasePath}/neu`, `{detailBasePath}/{index}`) |

### Funktionen

| Funktion | Beschreibung |
|----------|-------------|
| Sortieren | Spalte „Name" klickbar: `null → asc → desc → null` |
| Einzelauswahl | Checkbox pro Zeile |
| Alle auswählen | Checkbox im Header |
| Löschen (einzeln) | Sofort ohne Bestätigung, persistiert direkt |
| Löschen (Mehrfach) | Alle ausgewählten, persistiert direkt |
| Gruppieren | Nur wenn `allowGrouping=true`; Gruppenname via `window.prompt`; setzt `groupName` auf ausgewählten Attributen |
| Neues Attribut | Navigiert zu `{detailBasePath}/neu` |
| Anzeigen / Bearbeiten | Navigiert zu `{detailBasePath}/{originalIndex}` |

### Hinweise

- Sortierung ist rein visuell — die gespeicherte Reihenfolge bleibt erhalten (sortiert wird über `originalIndex`).
- Gruppe `Allgemein` wird in der Tabelle als `–` dargestellt.

## AttributeDetailView

Formular zum Anlegen und Bearbeiten eines einzelnen Attributs.

### Props

| Prop | Typ | Bedeutung |
|------|-----|-----------|
| `listPath` | `string` | Zielpfad nach Speichern und für „Abbrechen" |

### Felder

| Feld | Label | Pflicht | Validierung |
|------|-------|---------|------------|
| `name` | Name | ja | Nicht leer; kein Duplikat in der Liste |
| `description` | Beschreibung | nein | — |
| `value` | Wert | nein | `parseInt`; ungültig → `0` |

### Verhalten

- `index === 'neu'` → Neuanlage; sonst Bearbeitung des Eintrags an Position `index`.
- Duplikatprüfung erfolgt im Frontend vor dem Speichern.
- Nach erfolgreichem Speichern: navigiert zu `listPath`.
- „Abbrechen" navigiert ebenfalls zu `listPath`.
- Enter im Name-Feld löst Speichern aus.

### Persistenz

Identisch zu `AttributeView` — fileHandle → XML-Export; sonst `saveRuleset` via API.

## Referenzen

- [attribute/domain.md](domain.md) — Domänenklassen `Attribute`, `AttributeGroup`, `AttributeSet`
