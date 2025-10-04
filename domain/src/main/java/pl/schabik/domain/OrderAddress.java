package pl.schabik.domain;

import java.util.UUID;

public class OrderAddress {

    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;
    private final String houseNo;

    public OrderAddress(String street, String postalCode, String city, String houseNo) {
        this(UUID.randomUUID(), street, postalCode, city, houseNo);
    }

    public OrderAddress(UUID id, String street, String postalCode, String city, String houseNo) {
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
