package com.mutzy.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.mutzy.TestHelper;
import com.mutzy.dao.AppointmentDao;
import com.mutzy.domain.Appointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

class AppointmentServiceTest {

    private final AppointmentDao mockDao = Mockito.mock(AppointmentDao.class);
    private final AppointmentService service = new AppointmentService(mockDao);

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testGetAllAppointmentsByDate_WhenNoAppointmentsStored() {
        Mockito.when(mockDao.findAll()).thenReturn(Collections::emptyIterator);
        List<Appointment> appointments = service.getAllAppointmentsByDate();
        Assertions.assertNotNull(appointments);
        Assertions.assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetAllAppointmentsByDate_WithMultipleElements_ShouldReturnSortedList() throws ParseException {
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

}