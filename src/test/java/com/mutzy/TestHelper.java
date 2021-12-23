package com.mutzy;

import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.AppointmentResponseDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestHelper {

    private static final Random random = new Random();
    private static final long START_INSTANT = Instant.now().minus(100, ChronoUnit.DAYS).toEpochMilli();
    private static final long END_INSTANT = Instant.now().plus(100, ChronoUnit.DAYS).toEpochMilli();

    public static Appointment createAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(random.nextInt(Integer.MAX_VALUE));
        appointment.setDescription(RandomStringUtils.randomAlphanumeric(10));
        appointment.setLocationId(random.nextInt(Integer.MAX_VALUE));
        appointment.setPersonId(random.nextInt(Integer.MAX_VALUE));
        appointment.setDate(Date.from(Instant.ofEpochSecond(random.longs(START_INSTANT, END_INSTANT).findFirst().getAsLong())));
        return appointment;
    }

    public static AppointmentResponseDto createAppointmentResponse() {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setDescription(RandomStringUtils.randomAlphanumeric(10));
        dto.setDate(Date.from(Instant.ofEpochSecond(random.longs(START_INSTANT, END_INSTANT).findFirst().getAsLong())));
        dto.setLocation(createLocationDto());
        dto.setPerson(createPersonDto());
        return dto;
    }

    public static Appointment createAppointment(Date date) {
        Appointment appointment = createAppointment();
        appointment.setDate(date);
        return appointment;
    }

    public static AppointmentRequestDto createAppointmentDto() {
        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setDate("2022-12-20");
        dto.setTime("12:30");
        dto.setDescription(RandomStringUtils.randomAlphanumeric(10));
        dto.setLocationId(1); // this is valid if we have a location in the database with id 1
        dto.setPersonId(1); // this is valid if we have a person in the database with id 1
        return dto;
    }

    public static PersonDto createPersonDto() {
        PersonDto personDto = new PersonDto();
        personDto.setId(random.nextInt(Integer.MAX_VALUE));
        personDto.setName(RandomStringUtils.randomAlphanumeric(10));
        personDto.setAffiliation(RandomStringUtils.randomAlphanumeric(10));
        return personDto;
    }

    public static Person createPerson() {
        Person person = new Person();
        person.setId(random.nextInt(Integer.MAX_VALUE));
        person.setName(RandomStringUtils.randomAlphanumeric(10));
        person.setAffiliation(RandomStringUtils.randomAlphanumeric(10));
        return person;
    }

    public static LocationDto createLocationDto() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(random.nextInt(Integer.MAX_VALUE));
        locationDto.setName(RandomStringUtils.randomAlphanumeric(10));
        locationDto.setDescription(RandomStringUtils.randomAlphanumeric(10));
        return locationDto;
    }

    public static Location createLocation() {
        Location location = new Location();
        location.setId(random.nextInt(Integer.MAX_VALUE));
        location.setName(RandomStringUtils.randomAlphanumeric(10));
        location.setDescription(RandomStringUtils.randomAlphanumeric(10));
        return location;
    }

    public static List<Appointment> createAppointmentList(int count) {
        List<Appointment> appointments = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            appointments.add(createAppointment());
        }
        return appointments;
    }

    public static List<AppointmentResponseDto> createAppointmentResponseList(int count) {
        List<AppointmentResponseDto> appointments = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            appointments.add(createAppointmentResponse());
        }
        return appointments;
    }

    public static List<Person> createPersonList(int count) {
        List<Person> people = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            people.add(createPerson());
        }
        return people;
    }

    public static List<Location> createLocationList(int count) {
        List<Location> locations = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            locations.add(createLocation());
        }
        return locations;
    }

}
