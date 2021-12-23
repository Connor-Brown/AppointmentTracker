package com.mutzy.service;

import com.mutzy.dao.AppointmentDao;
import com.mutzy.domain.Appointment;
import com.mutzy.dto.AppointmentDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentDao appointmentDao;
    private static final AppointmentComparator comparator = new AppointmentComparator();

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final int MAX_DESCRIPTION_LENGTH = 1024;

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

    /**
     * Attempts to save the given appointment.
     * Can throw a ValidationException if the given AppointmentDto fails validation
     * @param appointmentDto The Appointment object to save
     * @return The created Appointment object
     */
    public Appointment createAppointment(AppointmentDto appointmentDto) throws ValidationException {
        validateAppointmentDto(appointmentDto);
        try {
          Appointment appointment = mapDtoToDomain(appointmentDto);
          return appointmentDao.save(appointment);
        } catch (Exception e) {
            log.error("An unexpected exception occurred", e);
            return null;
        }
    }

    private void validateAppointmentDto(AppointmentDto dto) throws ValidationException {
        if (dto == null) {
            throw new ValidationException("Cannot save a blank appointment");
        }
        validateDescription(dto.getDescription());
        validateDate(dto.getDate());
        validateTime(dto.getTime());
        validatePersonId(dto.getPersonId());
        validateLocationId(dto.getLocationId());
    }

    private void validateDescription(String description) throws ValidationException {
        if (StringUtils.isEmpty(description)) {
            throw new ValidationException("Appointment description cannot be empty");
        } else if (!StringUtils.isAlphanumericSpace(description)) {
            throw new ValidationException("Appointment description can only contain letters and/or numbers");
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Users are entering long descriptions with character count {}. Consider updating the database field size", description.length());
        }
    }

    private void validateDate(String date) throws ValidationException {
        if (StringUtils.isEmpty(date)) {
            throw new ValidationException("Appointment date cannot be empty");
        }
        try {
            // just check if we are able to successfully parse the date
            dateFormat.parse(date);
        } catch (ParseException e) {
            throw new ValidationException("Invalid date format");
        }
    }

    private void validateTime(String time) throws ValidationException {
        if (StringUtils.isEmpty(time)) {
            throw new ValidationException("Appointment date cannot be empty");
        }
        try {
            // just check if we are able to successfully parse the time
            timeFormat.parse(time);
        } catch (ParseException e) {
            throw new ValidationException("Invalid date format");
        }
    }

    private void validatePersonId(Integer personId) throws ValidationException {
        //TODO
    }

    private void validateLocationId(Integer locationId) throws ValidationException {
        //TODO
    }

    public Appointment mapDtoToDomain(AppointmentDto dto) {
        Appointment appointment = new Appointment();
        try {
            log.info("CONNOR: dto {}", dto);
            appointment.setPersonId(dto.getPersonId());
            appointment.setLocationId(dto.getLocationId());
            appointment.setDescription(dto.getDescription().length() > MAX_DESCRIPTION_LENGTH ?
                    dto.getDescription().substring(0, MAX_DESCRIPTION_LENGTH) :
                    dto.getDescription());
            appointment.setDate(dateTimeFormat.parse(String.format("%s %s", dto.getDate(), dto.getTime())));
            return appointment;
        } catch (ParseException e) {
            log.warn("Failed to parse given date", e);
            return appointment;
        }
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
