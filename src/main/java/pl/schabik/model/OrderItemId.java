package pl.schabik.model;

import java.io.Serializable;


public class OrderItemId implements Serializable {

    private Integer id;
    private Order order;

    public OrderItemId(Integer id, Order order) {
        this.id = id;
        this.order = order;
    }

    public OrderItemId() {
    }

    public Integer getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }
}
