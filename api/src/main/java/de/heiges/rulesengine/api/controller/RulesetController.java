package de.heiges.rulesengine.api.controller;

import de.heiges.rulesengine.api.dto.AttributeApiDto;
import de.heiges.rulesengine.api.dto.AttributeGroupApiDto;
import de.heiges.rulesengine.api.dto.CheatApiDto;
import de.heiges.rulesengine.api.dto.RulesetApiDto;
import de.heiges.rulesengine.api.dto.SkillApiDto;
import de.heiges.rulesengine.api.dto.SkillDomainApiDto;
import de.heiges.rulesengine.api.dto.ValueRangeApiDto;
import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeGroup;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Cheat;
import de.heiges.rulesengine.coreelements.domain.model.SkillDomain;
import de.heiges.rulesengine.coreelements.domain.model.SkillVerb;
import de.heiges.rulesengine.coreelements.domain.model.Value;
import de.heiges.rulesengine.coreelements.domain.model.ValueRange;
import de.heiges.rulesengine.persistence.DataDirectory;
import de.heiges.rulesengine.persistence.repository.LoadedRuleset;
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

    private final XmlRulesetRepository repository = new XmlRulesetRepository();

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
            repository.save(toDomain(dto), toAttributeSet(dto), toSkillVerbs(dto), toSkillDomains(dto), toCheats(dto), DataDirectory.rulesetPath(name));
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
            repository.save(new ValueRange(-10, 0, 10), new AttributeSet(), List.of(), List.of(), List.of(), DataDirectory.rulesetPath(name));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Regelwerk konnte nicht angelegt werden", e);
        }
    }

    @PostMapping(value = "/import", consumes = "text/xml", produces = "application/json")
    public RulesetApiDto importFromXml(@RequestBody String xml) {
        try {
            return toApiDto(repository.fromXml(xml));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültiges XML: " + e.getMessage(), e);
        }
    }

    @PostMapping(value = "/export", consumes = "application/json", produces = "text/xml;charset=UTF-8")
    public String exportToXml(@RequestBody RulesetApiDto dto) {
        try {
            return repository.toXml(toDomain(dto), toAttributeSet(dto), toSkillVerbs(dto), toSkillDomains(dto), toCheats(dto));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "XML-Serialisierung fehlgeschlagen", e);
        }
    }

    private RulesetApiDto toApiDto(LoadedRuleset loaded) {
        ValueRangeApiDto valueRange = new ValueRangeApiDto(
                loaded.valueRange().min(),
                loaded.valueRange().average(),
                loaded.valueRange().max()
        );
        List<AttributeGroupApiDto> attributeGroups = loaded.attributeSet().getGroups().stream()
                .map(g -> new AttributeGroupApiDto(
                        g.getName(),
                        g.getAll().stream()
                                .map(a -> new AttributeApiDto(a.getName(), a.getDescription(), a.getValue().amount()))
                                .toList()
                ))
                .toList();
        List<SkillApiDto> skills = loaded.skills().stream()
                .map(s -> new SkillApiDto(s.getName(), s.getDescription()))
                .toList();
        List<SkillDomainApiDto> skillDomains = loaded.skillDomains().stream()
                .map(d -> new SkillDomainApiDto(d.getName(), d.getDescription()))
                .toList();
        List<CheatApiDto> cheats = loaded.cheats().stream()
                .map(c -> new CheatApiDto(c.getName(), c.getDescription()))
                .toList();
        return new RulesetApiDto(valueRange, attributeGroups, skills, skillDomains, cheats);
    }

    private ValueRange toDomain(RulesetApiDto dto) {
        ValueRangeApiDto vr = dto.valueRange();
        return new ValueRange(vr.min(), vr.average(), vr.max());
    }

    private AttributeSet toAttributeSet(RulesetApiDto dto) {
        AttributeSet set = new AttributeSet();
        for (AttributeGroupApiDto g : dto.attributeGroups()) {
            AttributeGroup group = new AttributeGroup(g.group());
            for (AttributeApiDto a : g.attributes()) {
                group.add(new Attribute(a.name(), a.description(), new Value(a.value())));
            }
            set.addGroup(group);
        }
        return set;
    }

    private List<SkillVerb> toSkillVerbs(RulesetApiDto dto) {
        return dto.skills().stream()
                .map(s -> new SkillVerb(s.name(), s.description()))
                .toList();
    }

    private List<SkillDomain> toSkillDomains(RulesetApiDto dto) {
        if (dto.skillDomains() == null) return List.of();
        return dto.skillDomains().stream()
                .map(d -> new SkillDomain(d.name(), d.description()))
                .toList();
    }

    private List<Cheat> toCheats(RulesetApiDto dto) {
        if (dto.cheats() == null) return List.of();
        return dto.cheats().stream()
                .map(c -> new Cheat(c.name(), c.description()))
                .toList();
    }
}
