package com.mutzy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppointmentDto {
    private String description;
    private String date; //TODO CONNOR
    private Integer personId;
    private Integer locationId;

    @Override
    public String toString() {
        return "AppointmentDto{" +
                "description=" + description +"," +
                "date=" + date +"," +
                "personId=" + personId +"," +
                "locationId=" + locationId +"," +
                "}";
    }
}
