package pl.schabik.trackorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Immutable
class TrackingOrderProjection {

    @Id
    private UUID orderId;

    private String status;

    @Column(name = "price")
    private BigDecimal amount;

    public UUID getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
