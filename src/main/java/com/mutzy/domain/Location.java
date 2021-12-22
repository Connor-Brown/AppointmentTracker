package com.mutzy.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "locations")
public class Location {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
}
