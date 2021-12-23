package com.mutzy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "locations")
@Getter @Setter
public class Location {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
}
