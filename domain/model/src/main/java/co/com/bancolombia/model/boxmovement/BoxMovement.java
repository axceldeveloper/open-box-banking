package co.com.bancolombia.model.boxmovement;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public class BoxMovement {
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String type; // INCOME o EXPENSE
    private BigDecimal amount;
    private String currency;
    private String description;

    public BoxMovement(String movementId, String boxId, LocalDateTime date, String type, BigDecimal amount, String currency, String description) {
        this.movementId = movementId;
        this.boxId = boxId;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }
}
