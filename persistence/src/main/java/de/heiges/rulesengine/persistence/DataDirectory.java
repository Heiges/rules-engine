package de.heiges.rulesengine.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class DataDirectory {

    private static final Path BASE = Path.of(System.getProperty("user.home"), ".rules-engine", "data");

    public static Path base() {
        return BASE;
    }

    public static Path rulesetPath(String name) {
        return BASE.resolve(name + ".xml");
    }

    public static void ensureExists() throws IOException {
        Files.createDirectories(BASE);
    }

    public static List<Path> listRulesets() throws IOException {
        if (!Files.exists(BASE)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.list(BASE)) {
            return stream
                    .filter(p -> p.getFileName().toString().endsWith(".xml"))
                    .sorted()
                    .toList();
        }
    }
}
