package com.mutzy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "appointments")
@Getter @Setter
public class Appointment {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;
    private Date date;
    private Integer personId;
    private Integer locationId;

}
