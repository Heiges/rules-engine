package de.heiges.rulesengine.api.controller;

import de.heiges.rulesengine.api.dto.AttributeApiDto;
import de.heiges.rulesengine.api.dto.RulesetApiDto;
import de.heiges.rulesengine.api.dto.SkillApiDto;
import de.heiges.rulesengine.api.dto.ValueRangeApiDto;
import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import de.heiges.rulesengine.coreelements.domain.model.Value;
import de.heiges.rulesengine.coreelements.domain.model.ValueRange;
import de.heiges.rulesengine.persistence.DataDirectory;
import de.heiges.rulesengine.persistence.repository.LoadedRuleset;
import de.heiges.rulesengine.persistence.repository.RulesetRepository;
import de.heiges.rulesengine.persistence.xml.XmlRulesetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/rulesets")
public class RulesetController {

    private final RulesetRepository repository = new XmlRulesetRepository();

    @GetMapping
    public List<String> list() {
        try {
            return DataDirectory.listRulesets().stream()
                    .map(p -> p.getFileName().toString().replaceAll("\\.xml$", ""))
                    .toList();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Regelwerke konnten nicht gelesen werden", e);
        }
    }

    @GetMapping("/{name}")
    public RulesetApiDto load(@PathVariable String name) {
        try {
            LoadedRuleset loaded = repository.load(DataDirectory.rulesetPath(name));
            return toApiDto(loaded);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Regelwerk nicht gefunden: " + name, e);
        }
    }

    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@PathVariable String name, @RequestBody RulesetApiDto dto) {
        try {
            DataDirectory.ensureExists();
            repository.save(toDomain(dto), toAttributeSet(dto), toSkills(dto), DataDirectory.rulesetPath(name));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Regelwerk konnte nicht gespeichert werden", e);
        }
    }

    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable String name) {
        try {
            DataDirectory.ensureExists();
            if (Files.exists(DataDirectory.rulesetPath(name))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Regelwerk existiert bereits: " + name);
            }
            repository.save(new ValueRange(-10, 0, 10), new AttributeSet(), List.of(), DataDirectory.rulesetPath(name));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Regelwerk konnte nicht angelegt werden", e);
        }
    }

    private RulesetApiDto toApiDto(LoadedRuleset loaded) {
        ValueRangeApiDto valueRange = new ValueRangeApiDto(
                loaded.valueRange().min(),
                loaded.valueRange().average(),
                loaded.valueRange().max()
        );
        List<AttributeApiDto> attributes = loaded.attributeSet().getAll().stream()
                .map(a -> new AttributeApiDto(a.getName(), a.getDescription(), a.getValue().amount()))
                .toList();
        List<SkillApiDto> skills = loaded.skills().stream()
                .map(s -> new SkillApiDto(s.getName(), s.getLinkedAttribute().getName(), s.getLevel()))
                .toList();
        return new RulesetApiDto(valueRange, attributes, skills);
    }

    private ValueRange toDomain(RulesetApiDto dto) {
        ValueRangeApiDto vr = dto.valueRange();
        return new ValueRange(vr.min(), vr.average(), vr.max());
    }

    private AttributeSet toAttributeSet(RulesetApiDto dto) {
        AttributeSet set = new AttributeSet();
        for (AttributeApiDto a : dto.attributes()) {
            set.add(new Attribute(a.name(), a.description(), new Value(a.value())));
        }
        return set;
    }

    private List<Skill> toSkills(RulesetApiDto dto) {
        AttributeSet set = toAttributeSet(dto);
        return dto.skills().stream()
                .map(s -> {
                    Attribute attr = set.find(s.linkedAttributeName())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Attribut nicht gefunden: " + s.linkedAttributeName()));
                    return new Skill(s.name(), attr, s.level());
                })
                .toList();
    }
}
