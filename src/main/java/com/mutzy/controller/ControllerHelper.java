package com.mutzy.controller;

import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.AppointmentResponseDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import com.mutzy.service.AppointmentService;
import com.mutzy.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ControllerHelper {

    private static final String MODEL_KEY_APPOINTMENT_LIST = "appointments";

    private final AppointmentService appointmentService;

    @Autowired
    public ControllerHelper(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    protected String redirectToAppointmentsPage(Model model) {
        populateAllAppointments(model);
        ensureRequiredFieldsArePopulated(model);
        return Constants.APPOINTMENTS_PAGE;
    }

    protected void populateAllAppointments(Model model) {
        List<AppointmentResponseDto> appointments = Optional.ofNullable(appointmentService.findAllAppointments())
                .orElse(Collections.emptyList());
        model.addAttribute(MODEL_KEY_APPOINTMENT_LIST, appointments);
    }

    protected void ensureRequiredFieldsArePopulated(Model model) {
        resetFormObjects(model);
        populateAllPeople(model);
        populateAllLocations(model);
    }

    private void resetFormObjects(Model model) {
        model.addAttribute("appointment", new AppointmentRequestDto());
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

}
