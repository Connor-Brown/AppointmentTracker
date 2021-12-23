package com.mutzy.utils;

import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class ObjectMapper {

    private static ObjectMapper instance;
    private ObjectMapper() {}

    public static ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper();
        }
        return instance;
    }

    public Appointment mapDtoToDomain(AppointmentDto dto) {
        Appointment appointment = new Appointment();
        try {
            appointment.setPersonId(dto.getPersonId());
            appointment.setLocationId(dto.getLocationId());
            appointment.setDescription(dto.getDescription().length() > Constants.MAX_APPOINTMENT_DESCRIPTION_LENGTH ?
                    dto.getDescription().substring(0, Constants.MAX_APPOINTMENT_DESCRIPTION_LENGTH) :
                    dto.getDescription());
            appointment.setDate(Constants.DATE_TIME_FORMAT.parse(String.format("%s %s", dto.getDate(), dto.getTime())));
            return appointment;
        } catch (ParseException e) {
            log.warn("Failed to parse given date", e);
            return appointment;
        }
    }

    public Person mapDtoToDomain(PersonDto dto) {
        Person person = new Person();
        person.setName(dto.getName().length() > Constants.MAX_PERSON_NAME_LENGTH ?
                dto.getName().substring(0, Constants.MAX_PERSON_NAME_LENGTH) :
                dto.getName());
        person.setAffiliation(dto.getAffiliation().length() > Constants.MAX_PERSON_AFFILIATION_LENGTH ?
                dto.getAffiliation().substring(0, Constants.MAX_PERSON_AFFILIATION_LENGTH) :
                dto.getAffiliation());
        return person;
    }

    public Location mapDtoToDomain(LocationDto dto) {
        Location location = new Location();
        location.setName(dto.getName().length() > Constants.MAX_LOCATION_NAME_LENGTH ?
                dto.getName().substring(0, Constants.MAX_LOCATION_NAME_LENGTH) :
                dto.getName());
        location.setDescription(dto.getDescription().length() > Constants.MAX_LOCATION_DESCRIPTION_LENGTH ?
                dto.getDescription().substring(0, Constants.MAX_LOCATION_DESCRIPTION_LENGTH) :
                dto.getDescription());
        return location;
    }

}
