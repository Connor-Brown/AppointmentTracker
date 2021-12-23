package com.mutzy;

import com.mutzy.domain.Appointment;
import com.mutzy.dto.AppointmentDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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

    public static Appointment createAppointment(Date date) {
        Appointment appointment = createAppointment();
        appointment.setDate(date);
        return appointment;
    }

    public static AppointmentDto createAppointmentDto() {
        AppointmentDto dto = new AppointmentDto();
        dto.setDate("2022-12-20T13:40:20");
        dto.setTime("12:30");
        dto.setDescription(RandomStringUtils.randomAlphanumeric(10));
        dto.setLocationId(1); // this is valid if we have a location in the database with id 1
        dto.setPersonId(1); // this is valid if we have a person in the database with id 1
        return dto;
    }

}
