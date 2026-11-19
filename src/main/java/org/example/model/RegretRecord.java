
package org.example.model;

import java.math.BigDecimal;

public class RegretRecord {

    private int id;
    private int decisionId;
    private int chosenAlternativeId;
    private BigDecimal bestScore;
    private BigDecimal chosenScore;
    private BigDecimal regretValue;

    public RegretRecord() {
    }

    public RegretRecord(
            int decisionId,
            int chosenAlternativeId,
            BigDecimal bestScore,
            BigDecimal chosenScore,
            BigDecimal regretValue) {

        this.decisionId = decisionId;
        this.chosenAlternativeId = chosenAlternativeId;
        this.bestScore = bestScore;
        this.chosenScore = chosenScore;
        this.regretValue = regretValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(int decisionId) {
        this.decisionId = decisionId;
    }

    public int getChosenAlternativeId() {
        return chosenAlternativeId;
    }

    public void setChosenAlternativeId(int chosenAlternativeId) {
        this.chosenAlternativeId = chosenAlternativeId;
    }

    public BigDecimal getBestScore() {
        return bestScore;
    }

    public void setBestScore(BigDecimal bestScore) {
        this.bestScore = bestScore;
    }

    public BigDecimal getChosenScore() {
        return chosenScore;
    }

    public void setChosenScore(BigDecimal chosenScore) {
        this.chosenScore = chosenScore;
    }

    public BigDecimal getRegretValue() {
        return regretValue;
    }

    public void setRegretValue(BigDecimal regretValue) {
        this.regretValue = regretValue;
    }
}
