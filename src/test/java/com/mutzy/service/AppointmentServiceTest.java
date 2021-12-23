package com.mutzy.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.mutzy.TestHelper;
import com.mutzy.dao.AppointmentDao;
import com.mutzy.domain.Appointment;
import com.mutzy.dto.AppointmentDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ValidationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AppointmentServiceTest {

    private final AppointmentDao mockDao = Mockito.mock(AppointmentDao.class);
    private final AppointmentService service = new AppointmentService(mockDao);

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void testGetAllAppointmentsByDate_WhenNoAppointmentsStored() {
        Mockito.when(mockDao.findAll()).thenReturn(Collections::emptyIterator);
        List<Appointment> appointments = service.getAllAppointmentsByDate();
        Assertions.assertNotNull(appointments);
        Assertions.assertTrue(appointments.isEmpty());
    }

    @Test
    void testGetAllAppointmentsByDate_WithMultipleElements_ShouldReturnSortedList() throws ParseException {
        Appointment june2020 = TestHelper.createAppointment(dateFormat.parse("2020-06-01"));
        Appointment may2019 = TestHelper.createAppointment(dateFormat.parse("2019-05-13"));
        Appointment january2030 = TestHelper.createAppointment(dateFormat.parse("2030-01-27"));

        Mockito.when(mockDao.findAll()).thenReturn(() -> new ArrayIterator<>(new Appointment[]{june2020, may2019, january2030}));
        List<Appointment> appointments = service.getAllAppointmentsByDate();

        Assertions.assertNotNull(appointments);
        Assertions.assertEquals(3, appointments.size());
        Assertions.assertEquals(may2019, appointments.get(0));
        Assertions.assertEquals(june2020, appointments.get(1));
        Assertions.assertEquals(january2030, appointments.get(2));
    }

    @Test
    void testCreateAppointment_WithValidDto() throws ValidationException {
        AppointmentDto dto = TestHelper.createAppointmentDto();
        Appointment expectedAppointment = TestHelper.createAppointment();

        Mockito.when(mockDao.save(any())).thenReturn(expectedAppointment);
        Appointment appointment = service.createAppointment(dto);
        Assertions.assertEquals(expectedAppointment, appointment);
    }

    @Test
    void testInvalidDto() {
        helpTestCreateAppointmentValidationError(null);
    }

    @Test
    void testInvalidDto_InvalidDescription() {
        AppointmentDto dto = TestHelper.createAppointmentDto();
        dto.setDescription("");
        helpTestCreateAppointmentValidationError(dto);

        dto.setDescription(null);
        helpTestCreateAppointmentValidationError(dto);

        dto.setDescription("Mostly valid %##() String with ^^! non-alphanumeric characters");
        helpTestCreateAppointmentValidationError(dto);
    }

    //TODO test description input with greater than 255 characters

    @Test
    void testInvalidDto_InvalidDate() {
        AppointmentDto dto = TestHelper.createAppointmentDto();
        dto.setDate("");
        helpTestCreateAppointmentValidationError(dto);

        dto.setDate(null);
        helpTestCreateAppointmentValidationError(dto);

        dto.setDate("letters in this string");
        helpTestCreateAppointmentValidationError(dto);

        dto.setDate("2000/50/10");
        helpTestCreateAppointmentValidationError(dto);
    }

    @Test
    void testInvalidDto_InvalidTime() {
        AppointmentDto dto = TestHelper.createAppointmentDto();
        dto.setTime("");
        helpTestCreateAppointmentValidationError(dto);

        dto.setTime(null);
        helpTestCreateAppointmentValidationError(dto);

        dto.setTime("letters in this string");
        helpTestCreateAppointmentValidationError(dto);

        dto.setTime("&$%&&!");
        helpTestCreateAppointmentValidationError(dto);
    }

    private void helpTestCreateAppointmentValidationError(AppointmentDto dto) {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> service.createAppointment(dto));
        Assertions.assertNotNull(exception.getMessage());
    }

}