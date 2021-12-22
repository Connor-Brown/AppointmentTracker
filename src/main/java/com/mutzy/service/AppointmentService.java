package com.mutzy.service;

import com.mutzy.dao.AppointmentDao;
import com.mutzy.domain.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentDao appointmentDao;
    private static final AppointmentComparator comparator = new AppointmentComparator();

    @Autowired
    public AppointmentService(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public List<Appointment> getAllAppointmentsByDate() {
        List<Appointment> appointments = new ArrayList<>();
        Iterable<Appointment> results = appointmentDao.findAll();
        results.forEach(appointments::add);
        appointments.sort(comparator);
        return appointments;
    }

    private static class AppointmentComparator implements Comparator<Appointment> {

        @Override
        public int compare(Appointment a1, Appointment a2) {
            int comparedDates = a1.getDate().compareTo(a2.getDate());
            if (comparedDates == 0) {
                // use the object ids to break any ties caused by the same date
                return a1.getId().compareTo(a2.getId());
            }
            return comparedDates;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof AppointmentComparator;
        }
    }

}
