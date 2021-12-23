package com.mutzy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonDto {
    private Integer id;
    private String name;
    private String affiliation;

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id + "," +
                "name=" + name + "," +
                "affiliation=" + affiliation +
                "}";
    }
}
