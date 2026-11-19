
package org.example.model;

import java.math.BigDecimal;

public class Criterion {
    private int id;
    private int decisionId;
    private String name;
    private BigDecimal weight;

    public Criterion() {
    }

    public Criterion(int decisionId, String name, BigDecimal weight) {
        this.decisionId = decisionId;
        this.name = name;
        this.weight = weight;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
