package com.mutzy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppointmentRequestDto {
    private String description;
    private String date;
    private String time;
    private Integer personId;
    private Integer locationId;

    @Override
    public String toString() {
        return "AppointmentRequestDto{" +
                "description=" + description + "," +
                "date=" + date + "," +
                "time=" + time + "," +
                "personId=" + personId + "," +
                "locationId=" + locationId +
                "}";
    }
}
