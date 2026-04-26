package de.heiges.rulesengine.coremechanics.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceRoller {

    private static final int MIN_DICE = 2;
    private static final int FACES = 6;

    private final Random random;

    public DiceRoller(Random random) {
        this.random = random;
    }

    public DiceRoll roll(int value) {
        int diceCount = Math.max(value, MIN_DICE);
        List<Integer> rolled = new ArrayList<>(diceCount);
        for (int i = 0; i < diceCount; i++) {
            rolled.add(random.nextInt(FACES) + 1);
        }
        return new DiceRoll(rolled);
    }
}
