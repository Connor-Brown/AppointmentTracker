package com.mutzy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "people")
@Getter @Setter
public class Person {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String affiliation;
}
