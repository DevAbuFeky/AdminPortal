package com.adminportal.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserBilling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userBillingName;
    private String userBillingStreet1;
    private String userBiliingStreet2;
    private String userBillingCity;
    private String userBillingState;
    private String userBillingCountry;
    private String userBillingZipcode;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPayment userPayment;
}
