package com.mutzy.controller;

import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.AppointmentResponseDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import com.mutzy.service.AppointmentService;
import com.mutzy.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ControllerHelper controllerHelper;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, ControllerHelper controllerHelper) {
        this.appointmentService = appointmentService;
        this.controllerHelper = controllerHelper;
    }

    @GetMapping // ideally this would have some sort of pagination rather than returning ALL the data in our database table
    public String getAppointments(Model model) {
        log.info("Received request to get all appointments by date");
        return controllerHelper.redirectToAppointmentsPage(model);
    }

    @Deprecated // a POST request to a /create endpoint is redundant. Work to migrate any traffic to createAppointment() method below
    @PostMapping("/create")
    public String createAppointmentDeprecated(@ModelAttribute AppointmentRequestDto appointment, BindingResult bindingResult, Model model) {
        return createAppointment(appointment, bindingResult, model);
    }

    @PostMapping
    public String createAppointment(@ModelAttribute AppointmentRequestDto appointment, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("Issue with create appointment request {}", bindingResult.getAllErrors());
                return reportAppointmentFormFailureToUser("There was an invalid value on the given appointment", model, appointment);
            }
            AppointmentResponseDto createdAppointment = appointmentService.createAppointment(appointment);
            if (createdAppointment == null) {
                return reportAppointmentFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, appointment);
            }

            // This will re-query the database even though we probably already have the full list of appointments.
            // This is good to do if the user has 2 sessions open at once (unlikely, but possible)
            // but it could cause performance issues at scale (which isn't a big concern for this demo project, but it's good to be aware of)
            return controllerHelper.redirectToAppointmentsPage(model);
        } catch (ValidationException e) {
            log.warn("The given appointment failed validation", e);
            return reportAppointmentFormFailureToUser(e.getMessage(), model, appointment);
        }
    }

    @PostMapping("/{appointmentId}") // This should be a DELETE request
    public String deleteAppointment(@PathVariable(value = "appointmentId") Integer appointmentId, Model model) {
        try {
            appointmentService.deleteAppointment(appointmentId);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return controllerHelper.redirectToAppointmentsPage(model);
    }

    @PostMapping("/person")
    public String createPerson(@ModelAttribute PersonDto personDto, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("Issue with create person request {}", bindingResult.getAllErrors());
                return reportPersonFormFailureToUser("There was an invalid value on the given person", model, personDto);
            }
            Person createdPerson = appointmentService.createPerson(personDto);
            if (createdPerson == null) {
                return reportPersonFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, personDto);
            }
            return controllerHelper.redirectToAppointmentsPage(model);
        } catch (ValidationException e) {
            log.warn("The given person failed validation", e);
            return reportPersonFormFailureToUser(e.getMessage(), model, personDto);
        }
    }

    @PostMapping("/location")
    public String createLocation(@ModelAttribute LocationDto locationDto, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("Issue with create location request {}", bindingResult.getAllErrors());
                return reportLocationFormFailureToUser("There was an invalid value on the given location", model, locationDto);
            }
            Location createdLocation = appointmentService.createLocation(locationDto);
            if (createdLocation == null) {
                return reportLocationFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, locationDto);
            }
            return controllerHelper.redirectToAppointmentsPage(model);
        } catch (ValidationException e) {
            log.warn("The given location failed validation", e);
            return reportLocationFormFailureToUser(e.getMessage(), model, locationDto);
        }
    }

    private String reportAppointmentFormFailureToUser(String errorMessage, Model model, AppointmentRequestDto appointmentRequestDto) {
        controllerHelper.populateAllAppointments(model);
        controllerHelper.ensureRequiredFieldsArePopulated(model);
        model.addAttribute("appointment", appointmentRequestDto);
        model.addAttribute("appointmentValidationError", errorMessage);
        return Constants.APPOINTMENTS_PAGE;
    }

    private String reportPersonFormFailureToUser(String errorMessage, Model model, PersonDto personDto) {
        controllerHelper.populateAllAppointments(model);
        controllerHelper.ensureRequiredFieldsArePopulated(model);
        model.addAttribute("person", personDto);
        model.addAttribute("personValidationError", errorMessage);
        return Constants.APPOINTMENTS_PAGE;
    }

    private String reportLocationFormFailureToUser(String errorMessage, Model model, LocationDto locationDto) {
        controllerHelper.populateAllAppointments(model);
        controllerHelper.ensureRequiredFieldsArePopulated(model);
        model.addAttribute("location", locationDto);
        model.addAttribute("locationValidationError", errorMessage);
        return Constants.APPOINTMENTS_PAGE;
    }
}
