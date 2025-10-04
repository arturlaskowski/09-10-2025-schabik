package pl.schabik.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Table(name = "order_address")
@Entity
public class JpaOrderAddressEntity {

    @Id
    private UUID id;

    private String street;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String city;

    @NotBlank
    private String houseNo;

    protected JpaOrderAddressEntity() {
    }

    public JpaOrderAddressEntity(UUID id, String street, String postalCode, String city, String houseNo) {
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.houseNo = houseNo;
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getHouseNo() {
        return houseNo;
    }
}
