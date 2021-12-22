package com.mutzy.controller;

import com.mutzy.domain.Appointment;
import com.mutzy.dto.AppointmentDto;
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
        return redirectToAppointmentsPageWithUpdatedList(model);
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
                return reportFormFailureToUser("An unknown error occurred. Please reach out to support if the issue persists", model, appointment);
            }

            // This will re-query the database even though we probably already have the full list of appointments.
            // This is good to do if the user has 2 sessions open at once (unlikely, but possible)
            // but it could cause performance issues at scale (which isn't a big concern for this demo project, but it's good to be aware of)
            return redirectToAppointmentsPageWithUpdatedList(model);
        } catch (ValidationException e) {
            log.warn("The given appointment failed validation", e);
            return reportFormFailureToUser(e.getMessage(), model, appointment);
        }
    }

    private String redirectToAppointmentsPageWithUpdatedList(Model model) {
        populateAllAppointments(model);
        ensureRequiredFieldsArePopulated(model);
        return APPOINTMENTS_VIEW;
    }

    private void populateAllAppointments(Model model) {
        List<Appointment> appointments = Optional.ofNullable(appointmentService.getAllAppointmentsByDate())
                .orElse(Collections.emptyList());
        log.info("Found {} appointments", appointments.size());
        model.addAttribute(MODEL_KEY_APPOINTMENT_LIST, appointments);
    }

    private void ensureRequiredFieldsArePopulated(Model model) {
        resetFormObjects(model);
        //TODO populate people and locations
    }

    private void resetFormObjects(Model model) {
        model.addAttribute("appointment", new AppointmentDto());
        model.addAttribute("validationError", "");
    }

    private String reportFormFailureToUser(String errorMessage, Model model, AppointmentDto appointmentDto) {
        populateAllAppointments(model);
        model.addAttribute("appointment", appointmentService.mapDtoToDomain(appointmentDto));
        model.addAttribute("validationError", errorMessage);
        return APPOINTMENTS_VIEW;
    }
}
