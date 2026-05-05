# Coding-Konventionen

## Java

- Java 21
- Keine Wildcardimporte
- Records für reine Datenhaltung (`Value`, `ValueRange`)
- Domänenklassen ohne Framework-Annotationen (kein JAXB, kein Spring)
- Keine Kommentare außer wenn das *Warum* nicht offensichtlich ist

## Was zu vermeiden ist

- JAXB- oder Spring-Annotationen im Domänenmodell (`coreElements`)
- Direkte `fetch`-Aufrufe in Views — immer über `api.ts`
- Geteilten Zustand in lokalen View-States statt im `RulesetContext`
