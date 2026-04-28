---
name: Spielwelt anlegen
description: Kachel und Placeholder-View für das Anlegen einer neuen Spielwelt
type: project
---

# Spielwelt anlegen

## Ziel

Einstiegspunkt für das Anlegen einer neuen Spielwelt. Die Kachel ist auf der Startseite immer sichtbar und navigiert zu einer dedizierten View.

## Anforderungen

- Kachel „Spielwelt anlegen" in der HomeView, immer sichtbar (keine Abhängigkeit von geladenem Regelwerk)
- Navigation zu `/create-world`
- Placeholder-View mit Zurück-Button

## Entscheidungen

- **Immer sichtbar**: Eine Spielwelt ist kein Derivat von Referenzregeln — der Einstieg soll unabhängig vom geladenen Regelwerk möglich sein.
- **Placeholder zuerst**: Die View ist intentional leer; das Feature wird in einem separaten Schritt weiter ausgebaut.
- **Kein eigenes CSS**: nutzt `.detail-view` und `.back-button` aus `DetailView.css`.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Kachel-Eintrag (HomeView) | `frontend/src/views/HomeView.tsx` |
| View-Komponente | `frontend/src/views/SpielweltView.tsx` |
| Route-Registrierung | `frontend/src/App.tsx` |

## Rekonstruktion

```
Neue Kachel "Spielwelt anlegen" (id: create-world) in HomeView — immer sichtbar.
Route /create-world → SpielweltView.
SpielweltView: Placeholder mit Zurück-Button, nutzt DetailView.css.
```
