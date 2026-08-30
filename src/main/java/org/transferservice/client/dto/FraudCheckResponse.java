package org.transferservice.client.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "fraudCheckId",
        "riskLevel",
        "decision",
        "reason"
})
public class FraudCheckResponse {

    private Long fraudCheckId;
    private FraudDecision decision;
    private FraudRiskLevel riskLevel;
    private String reason;

    public FraudCheckResponse(){}

    public FraudCheckResponse(Long fraudCheckId, FraudDecision decision, FraudRiskLevel riskLevel, String reason) {
        this.fraudCheckId = fraudCheckId;
        this.decision = decision;
        this.riskLevel = riskLevel;
        this.reason = reason;
    }

    public Long getFraudCheckId() {
        return fraudCheckId;
    }

    public void setFraudCheckId(Long fraudCheckId) {
        this.fraudCheckId = fraudCheckId;
    }

    public FraudDecision getDecision() {
        return decision;
    }

    public void setDecision(FraudDecision decision) {
        this.decision = decision;
    }

    public FraudRiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(FraudRiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
