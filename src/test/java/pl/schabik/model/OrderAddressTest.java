package pl.schabik.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderAddressTest {

    @Test
    void shouldCreateValidAddress() {
        //given
        var street = "Marianowa";
        var postalCode = "888-88";
        var city = "Paździochowo";
        var houseNo = "11";

        //when
        var address = new OrderAddress(street, postalCode, city, houseNo);

        //then
        assertEquals(street, address.getStreet());
        assertEquals(postalCode, address.getPostalCode());
        assertEquals(city, address.getCity());
        assertEquals(houseNo, address.getHouseNo());
    }
}
