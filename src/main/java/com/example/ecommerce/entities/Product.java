package com.example.ecommerce.entities;

import java.util.List;

import com.example.ecommerce.entities.converters.StringListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    private int stocks;

    private double price;
    @Column(columnDefinition = "longtext")
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    private String uuid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "uuid", referencedColumnName = "uuid")
    private List<DocumentMapping> docs;
    @ManyToOne
    @JoinColumn(name = "seller_id",nullable = false,referencedColumnName = "id")
    private Seller seller;


    
}
