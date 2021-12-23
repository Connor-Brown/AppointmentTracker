package com.mutzy.controller;

import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import com.mutzy.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {

    //TODO error mapping

    // if we had a larger number of views, this should be extracted to some constant file
    protected static final String APPOINTMENTS_VIEW = "appointments";
    private static final String MODEL_KEY_APPOINTMENT_LIST = "appointments";

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String getAppointments(Model model) {
        log.info("Received request to get all appointments by date");
        return redirectToAppointmentsPage(model);
    }

    @Deprecated // a POST request to a /create endpoint is redundant. Work to migrate any traffic to createAppointment() method below
    @PostMapping("/create")
    public String createAppointmentDeprecated(@ModelAttribute AppointmentDto appointment, BindingResult bindingResult, Model model) {
        return createAppointment(appointment, bindingResult, model);
    }

    @PostMapping
    public String createAppointment(@ModelAttribute AppointmentDto appointment, BindingResult bindingResult, Model model) {
        try {
            Appointment createdAppointment = appointmentService.createAppointment(appointment);
            if (createdAppointment == null) {
                return reportAppointmentFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, appointment);
            }

            // This will re-query the database even though we probably already have the full list of appointments.
            // This is good to do if the user has 2 sessions open at once (unlikely, but possible)
            // but it could cause performance issues at scale (which isn't a big concern for this demo project, but it's good to be aware of)
            return redirectToAppointmentsPage(model);
        } catch (ValidationException e) {
            log.warn("The given appointment failed validation", e);
            return reportAppointmentFormFailureToUser(e.getMessage(), model, appointment);
        }
    }

    @PostMapping("/person")
    public String createPerson(@ModelAttribute PersonDto personDto, BindingResult bindingResult, Model model) {
        try {
            Person createdPerson = appointmentService.createPerson(personDto);
            if (createdPerson == null) {
                return reportPersonFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, personDto);
            }
            return redirectToAppointmentsPage(model);
        } catch (ValidationException e) {
            log.warn("The given person failed validation", e);
            return reportPersonFormFailureToUser(e.getMessage(), model, personDto);
        }
    }

    @PostMapping("/location")
    public String createLocation(@ModelAttribute LocationDto locationDto, BindingResult bindingResult, Model model) {
        try {
            Location createdLocation = appointmentService.createLocation(locationDto);
            if (createdLocation == null) {
                return reportLocationFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, locationDto);
            }
            return redirectToAppointmentsPage(model);
        } catch (ValidationException e) {
            log.warn("The given location failed validation", e);
            return reportLocationFormFailureToUser(e.getMessage(), model, locationDto);
        }
    }

    private String redirectToAppointmentsPage(Model model) {
        populateAllAppointments(model);
        ensureRequiredFieldsArePopulated(model);
        return APPOINTMENTS_VIEW;
    }

    private void populateAllAppointments(Model model) {
        List<Appointment> appointments = Optional.ofNullable(appointmentService.findAllAppointments())
                .orElse(Collections.emptyList());
        log.info("Found {} appointments", appointments.size());
        model.addAttribute(MODEL_KEY_APPOINTMENT_LIST, appointments);
    }

    private void ensureRequiredFieldsArePopulated(Model model) {
        resetFormObjects(model);
        populateAllPeople(model);
        populateAllLocations(model);
    }

    private void resetFormObjects(Model model) {
        model.addAttribute("appointment", new AppointmentDto());
        model.addAttribute("person", new PersonDto());
        model.addAttribute("location", new LocationDto());
        model.addAttribute("appointmentValidationError", "");
        model.addAttribute("personValidationError", "");
        model.addAttribute("locationValidationError", "");
    }

    private void populateAllPeople(Model model) {
        model.addAttribute("people", appointmentService.findAllPeople());
    }

    private void populateAllLocations(Model model) {
        model.addAttribute("locations", appointmentService.findAllLocations());
    }

    private String reportAppointmentFormFailureToUser(String errorMessage, Model model, AppointmentDto appointmentDto) {
        populateAllAppointments(model);
        ensureRequiredFieldsArePopulated(model);
        model.addAttribute("appointment", appointmentDto);
        model.addAttribute("appointmentValidationError", errorMessage);
        return APPOINTMENTS_VIEW;
    }

    private String reportPersonFormFailureToUser(String errorMessage, Model model, PersonDto personDto) {
        populateAllAppointments(model);
        ensureRequiredFieldsArePopulated(model);
        model.addAttribute("person", personDto);
        model.addAttribute("personValidationError", errorMessage);
        return APPOINTMENTS_VIEW;
    }

    private String reportLocationFormFailureToUser(String errorMessage, Model model, LocationDto locationDto) {
        populateAllAppointments(model);
        ensureRequiredFieldsArePopulated(model);
        model.addAttribute("location", locationDto);
        model.addAttribute("locationValidationError", errorMessage);
        return APPOINTMENTS_VIEW;
    }
}
