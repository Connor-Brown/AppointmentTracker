package com.mutzy.controller;

import com.mutzy.domain.Appointment;
import com.mutzy.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {

    // if we had a larger number of views, this should be extracted to some constant file
    protected static final String APPOINTMENTS_VIEW = "appointments";

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String getApplications(Model model) {
        log.info("Received request to get all appointments by date");
        List<Appointment> appointments = Optional.ofNullable(appointmentService.getAllAppointmentsByDate())
                .orElse(Collections.emptyList());
        log.info("Found {} appointments", appointments.size());
        model.addAttribute("appointments", appointments);
        return APPOINTMENTS_VIEW;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createApplicationDeprecated() {
        return createApplication();
    }

    @PostMapping
    public ResponseEntity<String> createApplication() {
        // create an application after validating inputs
        return null;
    }

}
