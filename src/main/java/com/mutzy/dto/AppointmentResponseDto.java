package com.mutzy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class AppointmentResponseDto {
    private Integer id;
    private String description;
    private Date date;
    private PersonDto person;
    private LocationDto location;

    @Override
    public String toString() {
        return "AppointmentResponseDto{" +
                "description=" + description + "," +
                "date=" + date + "," +
                "person=" + person + "," +
                "location=" + location +
                "}";
    }
}
