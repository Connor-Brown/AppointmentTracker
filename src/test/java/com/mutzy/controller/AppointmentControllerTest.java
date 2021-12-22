package com.mutzy.controller;

import com.mutzy.TestHelper;
import com.mutzy.domain.Appointment;
import com.mutzy.service.AppointmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AppointmentControllerTest {

    private final AppointmentService mockAppointmentService = Mockito.mock(AppointmentService.class);
    private final AppointmentController controller = new AppointmentController(mockAppointmentService);
    private Model model = new ExtendedModelMap();

    @AfterEach
    void reset() {
        // clear out any residual data in the model
        model = new ExtendedModelMap();
    }

    @Test
    void testGetAppointments_WhenServiceReturnsNull() {
        Mockito.when(mockAppointmentService.getAllAppointmentsByDate()).thenReturn(null);
        String view = controller.getAppointments(model);

        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertTrue(((List<?>) modelAppointments).isEmpty());
    }

    @Test
    void testGetAppointments_ShouldShowAllReturnedAppointments() {
        List<Appointment> appointments = genericAppointments(2);
        Mockito.when(mockAppointmentService.getAllAppointmentsByDate()).thenReturn(appointments);
        String view = controller.getAppointments(model);

        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(appointments, modelAppointments);
    }

    @Test
    void testCreateAppointment_WhenRequestIsValid_ShouldCreateAppointmentAndUpdateModel() throws Exception {
        int initialSize = 3;
        List<Appointment> initialAppointments = genericAppointments(initialSize);
        Appointment expectedAppointment = TestHelper.createAppointment();
        List<Appointment> allAppointments = genericAppointments(initialSize);
        allAppointments.add(expectedAppointment);

        model.addAttribute("appointments", initialAppointments);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenReturn(expectedAppointment);
        Mockito.when(mockAppointmentService.getAllAppointmentsByDate()).thenReturn(allAppointments);
        String view = controller.createAppointment(TestHelper.createAppointmentDto(), null, model);

        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(initialSize + 1, ((List<?>) modelAppointments).size());
        Assertions.assertTrue(((List<?>) modelAppointments).contains(expectedAppointment));
    }

    @Test
    void testCreateAppointment_WhenExtraValidationFails_ShouldDisplayErrorToUser() throws Exception {
        List<Appointment> initialAppointments = genericAppointments(2);
        ValidationException exception = new ValidationException("some field failed validation");

        model.addAttribute("appointments", initialAppointments);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenThrow(exception);

        String view = controller.createAppointment(TestHelper.createAppointmentDto(), null, model);
        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(initialAppointments, modelAppointments); // The list of all appointments should not have changed
        Assertions.assertEquals(exception.getMessage(), model.getAttribute("form.error"));
    }

    @Test
    void testCreateAppointment_WhenUnknownErrorOccurs_ShouldDisplayErrorToUser() throws Exception {
        List<Appointment> initialAppointments = genericAppointments(2);

        model.addAttribute("appointments", initialAppointments);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenReturn(null);

        String view = controller.createAppointment(TestHelper.createAppointmentDto(), null, model);
        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(initialAppointments, modelAppointments); // The list of all appointments should not have changed
        Assertions.assertNotNull(model.getAttribute("form.error"));
    }

    private List<Appointment> genericAppointments(int count) {
        List<Appointment> appointments = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            appointments.add(new Appointment());
        }
        return appointments;
    }
}