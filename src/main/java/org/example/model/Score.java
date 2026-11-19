
package org.example.model;

import java.math.BigDecimal;

public class Score {
    private int id;
    private int alternativeId;
    private int criterionId;
    private BigDecimal score;

    public Score() {
    }

    public Score(int alternativeId, int criterionId, BigDecimal score) {
        this.alternativeId = alternativeId;
        this.criterionId = criterionId;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlternativeId() {
        return alternativeId;
    }

    public void setAlternativeId(int alternativeId) {
        this.alternativeId = alternativeId;
    }

    public int getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(int criterionId) {
        this.criterionId = criterionId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
