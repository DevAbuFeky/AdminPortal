package com.adminportal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Data
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    private String tittle;
    private String author;
    private String publisher;
    private String publicationDate;
    private String language;
    private int numberOfPages;
    private String format;
    private int isbn;
    private double shippingWeight;
    private double listPrice;
    private double ourPrice;
    private boolean active = true;

    @Column(columnDefinition = "text")
    private String description;
    private int inStockNumber;

    @Column(length = 255 )
    private String logo;


    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookToCartItem> bookToCartItemList;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Transient
    public String getLogoImagePath(){
        if (logo == null || id == null)
            return "/image/logo-default.png";
        return "/image/" + this.id + "/" + this.logo;
    }

}
