Führe ein Code-Review der geänderten Dateien durch.

Prüfe auf:
- Einhaltung der Architekturvorgaben (@architecture.md)
- Einhaltung der Designrichtlinien (@design.md)
- Korrekte Verwendung des Toolstacks (@toolstack.md)
- Testabdeckung (JUnit 5, min. Validierungen + Gleichheitsprüfung)
- Sicherheitsprobleme (OWASP Top 10)
- Offensichtliche Bugs

## Java / coreElements spezifisch

- Paket-Konvention: `de.heiges.rulesengine.coreelements.domain.model`
- Java 17: moderne Syntax bevorzugen (Records, Pattern Matching, etc. wo sinnvoll)
- Kein negativer oder Leer-String-Wert ohne Validierung
- Gleichheit (`equals`/`hashCode`) über fachlichen Schlüssel (Name), nicht Technologie-ID
- Tests in `coreElements/src/test/...` spiegeln die Struktur von `src/main/...`
- `pom.xml` bleibt bei Java 17 und JUnit 5.x

Ausgabe als priorisierte Liste: Blocker → Warnung → Verbesserungsvorschlag.

$ARGUMENTS
