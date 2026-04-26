# coreMechanics-Modul

## Ziel

Eigenständiges Maven-Modul als Heimat für die Kernmechaniken des Regelwerks (Würfelwürfe, Proben, Modifikatoren o. ä.). Trennt spielmechanische Logik sauber von den reinen Datenbausteinen in `coreElements`.

## Anforderungen

- Kein Rückwärts-Abhängigkeit: `coreElements` darf `coreMechanics` **nicht** kennen.
- Domänenklassen bleiben annotation-frei (keine JAXB-, Jakarta- oder Spring-Annotationen).
- Paket-Wurzel: `de.heiges.rulesengine.coremechanics`
- Domain-Klassen liegen unter `…coremechanics.domain.model`

## Entscheidungen

- **`core-elements` als Compile-Abhängigkeit** — Mechaniken operieren auf den Domain-Objekten (Attribute, Skill, Value), müssen diese also kennen.
- **Eigenständiges Modul statt Unterpaket in `coreElements`** — hält spielmechanische Logik von reinen Datenbausteinen getrennt und ermöglicht unabhängigen Build.
- **Kein Root-POM** — das Projekt nutzt keinen Multi-Module-Build; jedes Modul wird eigenständig gebaut (analog zu `coreElements` und `persistence`).

## Implementierung

| Artefakt | Pfad |
|---|---|
| Maven POM | `coreMechanics/pom.xml` |
| Main-Sources (leer) | `coreMechanics/src/main/java/de/heiges/rulesengine/coremechanics/domain/model/` |
| Test-Sources (leer) | `coreMechanics/src/test/java/de/heiges/rulesengine/coremechanics/domain/model/` |

## Rekonstruktion

```
Erstelle ein neues Maven-Modul coreMechanics analog zu coreElements.
groupId=de.heiges.rulesengine, artifactId=core-mechanics, version=0.1.0-SNAPSHOT.
Abhängigkeiten: core-elements (compile) + junit-jupiter 5.10.2 (test).
Paketstruktur: de.heiges.rulesengine.coremechanics.domain.model
Kein Root-POM vorhanden — Modul ist eigenständig.
```

Kontext: Toolstack-Datei `.claude/toolstack.md` lesen (Java 17, Maven, Surefire 3.2.5). `coreElements/pom.xml` als Vorlage verwenden.
