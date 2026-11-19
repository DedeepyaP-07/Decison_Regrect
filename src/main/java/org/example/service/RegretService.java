
package org.example.service;

import org.example.model.RegretRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RegretService {

    public RegretRecord calculateRegret(
            int decisionId,
            int chosenAlternativeId,
            BigDecimal bestScore,
            BigDecimal chosenScore) {

        if (bestScore == null || chosenScore == null) {
            throw new IllegalArgumentException(
                    "Both scores are required.");
        }

        if (chosenScore.compareTo(bestScore) > 0) {
            throw new IllegalArgumentException(
                    "Chosen score cannot exceed the best score.");
        }

        BigDecimal regretValue = bestScore
                .subtract(chosenScore)
                .setScale(2, RoundingMode.HALF_UP);

        return new RegretRecord(
                decisionId,
                chosenAlternativeId,
                bestScore.setScale(2, RoundingMode.HALF_UP),
                chosenScore.setScale(2, RoundingMode.HALF_UP),
                regretValue);
    }
}
