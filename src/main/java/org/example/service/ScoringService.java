
package org.example.service;

import org.example.model.Criterion;
import org.example.model.Score;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoringService {

    public BigDecimal calculateWeightedScore(
            List<Criterion> criteria,
            List<Score> scores) {

        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException(
                    "No criteria available.");
        }

        BigDecimal totalWeight = BigDecimal.ZERO;

        for (Criterion criterion : criteria) {
            totalWeight = totalWeight.add(criterion.getWeight());
        }

        if (totalWeight.compareTo(new BigDecimal("100")) != 0) {
            throw new IllegalArgumentException(
                    "The total criterion weight must equal 100.");
        }

        Map<Integer, BigDecimal> scoreMap = new HashMap<>();

        for (Score score : scores) {
            scoreMap.put(
                    score.getCriterionId(),
                    score.getScore());
        }

        BigDecimal weightedTotal = BigDecimal.ZERO;

        for (Criterion criterion : criteria) {
            BigDecimal value = scoreMap.get(
                    criterion.getId());

            if (value == null) {
                throw new IllegalArgumentException(
                        "A score is missing for: "
                                + criterion.getName());
            }

            if (value.compareTo(BigDecimal.ONE) < 0
                    || value.compareTo(BigDecimal.TEN) > 0) {
                throw new IllegalArgumentException(
                        "Scores must be between 1 and 10.");
            }

            BigDecimal weightedValue = value.multiply(
                            criterion.getWeight())
                    .divide(new BigDecimal("100"),
                            4, RoundingMode.HALF_UP);

            weightedTotal = weightedTotal.add(weightedValue);
        }

        return weightedTotal.setScale(
                2, RoundingMode.HALF_UP);
    }
}
