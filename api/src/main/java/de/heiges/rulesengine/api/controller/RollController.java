package de.heiges.rulesengine.api.controller;

import de.heiges.rulesengine.api.dto.RollRequestDto;
import de.heiges.rulesengine.api.dto.RollResultApiDto;
import de.heiges.rulesengine.coremechanics.domain.model.DiceRoll;
import de.heiges.rulesengine.coremechanics.domain.model.DiceRoller;
import de.heiges.rulesengine.coremechanics.domain.model.Pasch;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/roll")
public class RollController {

    private final DiceRoller roller = new DiceRoller(new Random());

    @PostMapping
    public RollResultApiDto roll(@RequestBody RollRequestDto request) {
        DiceRoll result = roller.roll(request.value());
        Optional<Pasch> pasch = result.bestPasch();
        return new RollResultApiDto(
                result.values(),
                result.isSuccess(),
                pasch.map(Pasch::faceValue).orElse(null),
                pasch.map(Pasch::count).orElse(null)
        );
    }
}
