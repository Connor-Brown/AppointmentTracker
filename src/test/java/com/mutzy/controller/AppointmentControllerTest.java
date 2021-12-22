package com.mutzy.controller;

import com.mutzy.domain.Appointment;
import com.mutzy.service.AppointmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

class AppointmentControllerTest {

    private final AppointmentService mockAppointmentService = Mockito.mock(AppointmentService.class);
    private final AppointmentController controller = new AppointmentController(mockAppointmentService);
    private Model model = new ExtendedModelMap();

    @AfterEach
    void reset() {
        // clear out any residual data in the model
        model = new ExtendedModelMap();
    }

    // unit tests

    @Test
    void testGetAppointments_WhenServiceReturnsNull() {
        Mockito.when(mockAppointmentService.getAllAppointmentsByDate()).thenReturn(null);
        String view = controller.getApplications(model);

        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertNotNull(modelAppointments);
        Assertions.assertTrue(((List<?>) modelAppointments).isEmpty());
    }

    @Test
    void testGetAppointments_ShouldShowAllReturnedAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());

        Mockito.when(mockAppointmentService.getAllAppointmentsByDate()).thenReturn(appointments);
        String view = controller.getApplications(model);

        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertNotNull(modelAppointments);
        Assertions.assertEquals(appointments, modelAppointments);
    }

    // integration tests
}