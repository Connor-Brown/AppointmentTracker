package com.mutzy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationDto {
    private Integer id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "LocationDto{" +
                "id=" + id + "," +
                "name=" + name + "," +
                "description=" + description +
                "}";
    }
}
