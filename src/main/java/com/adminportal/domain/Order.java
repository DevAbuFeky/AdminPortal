package com.adminportal.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date orderDate;
    private Date shippingDate;
    private String shippingMethod;
    private String orderStatus;
    private BigDecimal orderTotal;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItem> cartItemList;

    @OneToOne(cascade = CascadeType.ALL)
    private ShippingAddress shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private BillingAddress billingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;

    @ManyToOne
    private User user;
}
