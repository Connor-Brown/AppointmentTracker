package com.mutzy.controller;

import com.mutzy.TestHelper;
import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import com.mutzy.service.AppointmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AppointmentControllerTest {

    private final AppointmentService mockAppointmentService = Mockito.mock(AppointmentService.class);
    private final AppointmentController controller = new AppointmentController(mockAppointmentService);
    private Model model = new ExtendedModelMap();

    @BeforeEach
    void setUpCommonMocks() {
        List<Appointment> appointments = Collections.singletonList(TestHelper.createAppointment());
        Mockito.when(mockAppointmentService.findAllAppointments()).thenReturn(appointments);

        List<Person> people = Collections.singletonList(TestHelper.createPerson());
        Mockito.when(mockAppointmentService.findAllPeople()).thenReturn(people);

        List<Location> locations = Collections.singletonList(TestHelper.createLocation());
        Mockito.when(mockAppointmentService.findAllLocations()).thenReturn(locations);
    }

    @AfterEach
    void reset() {
        // clear out any residual data in the model
        model = new ExtendedModelMap();
    }

    @Test
    void testGetAppointments_WhenServiceReturnsNull() {
        Mockito.when(mockAppointmentService.findAllAppointments()).thenReturn(null);
        String view = controller.getAppointments(model);

        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertTrue(((List<?>) modelAppointments).isEmpty());
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);

        checkRequiredFieldsOnModel();
        checkPeopleArePopulated();
        checkLocationsArePopulated();
    }

    @Test
    void testGetAppointments_ShouldShowAllReturnedAppointments() {
        List<Appointment> appointments = TestHelper.createAppointmentList(2);
        Mockito.when(mockAppointmentService.findAllAppointments()).thenReturn(appointments);
        String view = controller.getAppointments(model);

        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(appointments, modelAppointments);

        checkRequiredFieldsOnModel();
        checkPeopleArePopulated();
        checkLocationsArePopulated();
    }

    @Test
    void testCreateAppointment_WhenRequestIsValid_ShouldCreateAppointmentAndUpdateModel() {
        int initialSize = 3;
        List<Appointment> initialAppointments = TestHelper.createAppointmentList(initialSize);
        Appointment expectedAppointment = TestHelper.createAppointment();
        List<Appointment> allAppointments = TestHelper.createAppointmentList(initialSize);
        allAppointments.add(expectedAppointment);

        model.addAttribute("appointments", initialAppointments);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenReturn(expectedAppointment);
        Mockito.when(mockAppointmentService.findAllAppointments()).thenReturn(allAppointments);
        String view = controller.createAppointment(TestHelper.createAppointmentDto(), null, model);

        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(initialSize + 1, ((List<?>) modelAppointments).size());
        Assertions.assertTrue(((List<?>) modelAppointments).contains(expectedAppointment));

        checkRequiredFieldsOnModel();
        checkPeopleArePopulated();
        checkLocationsArePopulated();
    }

    @Test
    void testCreateAppointment_WhenExtraValidationFails_ShouldDisplayErrorToUser() {
        List<Appointment> initialAppointments = TestHelper.createAppointmentList(2);
        AppointmentDto dto = TestHelper.createAppointmentDto();
        ValidationException exception = new ValidationException("some field failed validation");

        model.addAttribute("appointments", initialAppointments);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenThrow(exception);
        Mockito.when(mockAppointmentService.findAllAppointments()).thenReturn(initialAppointments);

        String view = controller.createAppointment(dto, null, model);
        checkAppointmentValidationFields(view, initialAppointments, dto);
    }

    @Test
    void testCreateAppointment_WhenUnknownErrorOccurs_ShouldDisplayErrorToUser() {
        List<Appointment> initialAppointments = TestHelper.createAppointmentList(2);
        AppointmentDto dto = TestHelper.createAppointmentDto();

        model.addAttribute("appointments", initialAppointments);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenReturn(null);
        Mockito.when(mockAppointmentService.findAllAppointments()).thenReturn(initialAppointments);

        String view = controller.createAppointment(dto, null, model);
        checkAppointmentValidationFields(view, initialAppointments, dto);
    }

    @Test
    void testCreatePerson_WhenRequestIsValid_ShouldCreatePersonAndUpdateModel() {
        int initialSize = 3;
        List<Person> initialPeople = TestHelper.createPersonList(initialSize);
        Person expectedPerson = TestHelper.createPerson();
        List<Person> allPeople = TestHelper.createPersonList(initialSize);
        allPeople.add(expectedPerson);

        model.addAttribute("people", initialPeople);
        Mockito.when(mockAppointmentService.createPerson(any())).thenReturn(expectedPerson);
        Mockito.when(mockAppointmentService.findAllPeople()).thenReturn(allPeople);
        String view = controller.createPerson(TestHelper.createPersonDto(), null, model);

        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelPeople = model.getAttribute("people");
        Assertions.assertTrue(modelPeople instanceof List);
        Assertions.assertEquals(initialSize + 1, ((List<?>) modelPeople).size());
        Assertions.assertTrue(((List<?>) modelPeople).contains(expectedPerson));

        checkRequiredFieldsOnModel();
        checkAppointmentsArePopulated();
        checkLocationsArePopulated();
    }

    @Test
    void testCreatePerson_WhenExtraValidationFails_ShouldDisplayErrorToUser() {
        List<Person> initialPeople = TestHelper.createPersonList(2);
        PersonDto dto = TestHelper.createPersonDto();
        ValidationException exception = new ValidationException("some field failed validation");

        model.addAttribute("people", initialPeople);
        Mockito.when(mockAppointmentService.createPerson(any())).thenThrow(exception);
        Mockito.when(mockAppointmentService.findAllPeople()).thenReturn(initialPeople);

        String view = controller.createPerson(dto, null, model);
        checkPeopleValidationFields(view, initialPeople, dto);
    }

    @Test
    void testCreatePerson_WhenUnknownErrorOccurs_ShouldDisplayErrorToUser() {
        List<Person> initialPeople = TestHelper.createPersonList(2);
        PersonDto dto = TestHelper.createPersonDto();

        model.addAttribute("people", initialPeople);
        Mockito.when(mockAppointmentService.createAppointment(any())).thenReturn(null);
        Mockito.when(mockAppointmentService.findAllPeople()).thenReturn(initialPeople);

        String view = controller.createPerson(dto, null, model);
        checkPeopleValidationFields(view, initialPeople, dto);
    }

    @Test
    void testCreateLocation_WhenRequestIsValid_ShouldCreatePersonAndUpdateModel() {
        int initialSize = 3;
        List<Location> initialLocations = TestHelper.createLocationList(initialSize);
        Location expectedLocation = TestHelper.createLocation();
        List<Location> allLocations = TestHelper.createLocationList(initialSize);
        allLocations.add(expectedLocation);

        model.addAttribute("locations", initialLocations);
        Mockito.when(mockAppointmentService.createLocation(any())).thenReturn(expectedLocation);
        Mockito.when(mockAppointmentService.findAllLocations()).thenReturn(allLocations);
        String view = controller.createLocation(TestHelper.createLocationDto(), null, model);

        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelLocations = model.getAttribute("locations");
        Assertions.assertTrue(modelLocations instanceof List);
        Assertions.assertEquals(initialSize + 1, ((List<?>) modelLocations).size());
        Assertions.assertTrue(((List<?>) modelLocations).contains(expectedLocation));

        checkRequiredFieldsOnModel();
        checkAppointmentsArePopulated();
        checkPeopleArePopulated();
    }

    @Test
    void testCreateLocation_WhenExtraValidationFails_ShouldDisplayErrorToUser() {
        List<Location> initialLocations = TestHelper.createLocationList(2);
        LocationDto dto = TestHelper.createLocationDto();
        ValidationException exception = new ValidationException("some field failed validation");

        model.addAttribute("locations", initialLocations);
        Mockito.when(mockAppointmentService.createLocation(any())).thenThrow(exception);
        Mockito.when(mockAppointmentService.findAllLocations()).thenReturn(initialLocations);

        String view = controller.createLocation(dto, null, model);
        checkLocationValidationFields(view, initialLocations, dto);
    }

    @Test
    void testCreateLocation_WhenUnknownErrorOccurs_ShouldDisplayErrorToUser() {
        List<Location> initialLocations = TestHelper.createLocationList(2);
        LocationDto dto = TestHelper.createLocationDto();

        model.addAttribute("locations", initialLocations);
        Mockito.when(mockAppointmentService.createLocation(any())).thenReturn(null);
        Mockito.when(mockAppointmentService.findAllLocations()).thenReturn(initialLocations);

        String view = controller.createLocation(dto, null, model);
        checkLocationValidationFields(view, initialLocations, dto);
    }

    private void checkAppointmentValidationFields(String view, List<Appointment> initialAppointments, AppointmentDto dto) {
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelAppointments = model.getAttribute("appointments");
        Assertions.assertTrue(modelAppointments instanceof List);
        Assertions.assertEquals(initialAppointments, modelAppointments); // The list of all appointments should not have changed
        Assertions.assertNotNull(model.getAttribute("appointmentValidationError"));
        Assertions.assertEquals(dto, model.getAttribute("appointment"));
    }

    private void checkPeopleValidationFields(String view, List<Person> initialPeople, PersonDto dto) {
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelPeople = model.getAttribute("people");
        Assertions.assertTrue(modelPeople instanceof List);
        Assertions.assertEquals(initialPeople, modelPeople); // The list of all people should not have changed
        Assertions.assertNotNull(model.getAttribute("personValidationError"));
        Assertions.assertEquals(dto, model.getAttribute("person"));
    }

    private void checkLocationValidationFields(String view, List<Location> initialLocation, LocationDto dto) {
        Assertions.assertEquals(AppointmentController.APPOINTMENTS_VIEW, view);
        Object modelLocations = model.getAttribute("locations");
        Assertions.assertTrue(modelLocations instanceof List);
        Assertions.assertEquals(initialLocation, modelLocations); // The list of all locations should not have changed
        Assertions.assertNotNull(model.getAttribute("locationValidationError"));
        Assertions.assertEquals(dto, model.getAttribute("location"));
    }

    private void checkRequiredFieldsOnModel() {
        Assertions.assertNotNull(model.getAttribute("appointment"));
        Assertions.assertNotNull(model.getAttribute("person"));
        Assertions.assertNotNull(model.getAttribute("location"));

        // ensure there are no validation errors
        Assertions.assertEquals("", model.getAttribute("appointmentValidationError"));
        Assertions.assertEquals("", model.getAttribute("personValidationError"));
        Assertions.assertEquals("", model.getAttribute("locationValidationError"));
    }

    private void checkAppointmentsArePopulated() {
        Object appointments = model.getAttribute("appointments");
        Assertions.assertTrue(appointments instanceof List);
        Assertions.assertFalse(((List<?>) appointments).isEmpty());
    }

    private void checkPeopleArePopulated() {
        Object people = model.getAttribute("people");
        Assertions.assertTrue(people instanceof List);
        Assertions.assertFalse(((List<?>) people).isEmpty());
    }

    private void checkLocationsArePopulated() {
        Object locations = model.getAttribute("locations");
        Assertions.assertTrue(locations instanceof List);
        Assertions.assertFalse(((List<?>) locations).isEmpty());
    }
}