package com.maria.ecommerce.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="state")
@Data
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;


    //più stati appartengono ad un paese
    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;

}
