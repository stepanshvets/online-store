package com.dev.server.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy="category")
    private List<Product> products;
}
