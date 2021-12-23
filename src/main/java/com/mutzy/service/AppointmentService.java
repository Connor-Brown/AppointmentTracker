package com.mutzy.service;

import com.mutzy.dao.AppointmentDao;
import com.mutzy.dao.LocationDao;
import com.mutzy.dao.PersonDao;
import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.AppointmentResponseDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import com.mutzy.utils.ObjectMapper;
import com.mutzy.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.compare.ObjectToStringComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentDao appointmentDao;
    private final PersonDao personDao;
    private final LocationDao locationDao;
    private final ValidationUtils validationUtils;

    private static final AppointmentComparator appointmentComparator = new AppointmentComparator();
    private static final PersonComparator personComparator = new PersonComparator();
    private static final LocationComparator locationComparator = new LocationComparator();

    @Autowired
    public AppointmentService(AppointmentDao appointmentDao, PersonDao personDao, LocationDao locationDao, ValidationUtils validationUtils) {
        this.appointmentDao = appointmentDao;
        this.personDao = personDao;
        this.locationDao = locationDao;
        this.validationUtils = validationUtils;
    }

    public List<AppointmentResponseDto> findAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        Iterable<Appointment> results = appointmentDao.findAll();
        results.forEach(appointments::add);
        appointments.sort(appointmentComparator);
        return appointments.stream().map(this::mapAppointmentToResponseDto).collect(Collectors.toList());
    }

    public List<Person> findAllPeople() {
        List<Person> people = new ArrayList<>();
        Iterable<Person> results = personDao.findAll();
        results.forEach(people::add);
        people.sort(personComparator);
        return people;
    }

    public List<Location> findAllLocations() {
        List<Location> locations = new ArrayList<>();
        Iterable<Location> results = locationDao.findAll();
        results.forEach(locations::add);
        locations.sort(locationComparator);
        return locations;
    }

    /**
     * Attempts to save the given appointment.
     * Can throw a ValidationException if the given AppointmentDto fails validation
     * @param appointmentRequestDto The Appointment object to save
     * @return The created Appointment object
     */
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) throws ValidationException {
        validationUtils.validateAppointmentDto(appointmentRequestDto);
        try {
            Appointment appointment = ObjectMapper.getInstance().mapDtoToDomain(appointmentRequestDto);
            Appointment savedAppointment = appointmentDao.save(appointment);
            return mapAppointmentToResponseDto(savedAppointment);
        } catch (Exception e) {
            log.error("An unexpected exception occurred creating an appointment", e);
            return null;
        }
    }

    private AppointmentResponseDto mapAppointmentToResponseDto(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setDate(appointment.getDate());
        dto.setDescription(appointment.getDescription());
        if (appointment.getPersonId() != null) {
            Optional<Person> person = personDao.findById(appointment.getPersonId());
            if (person.isPresent()) {
                dto.setPerson(ObjectMapper.getInstance().mapDomainToDto(person.get()));
            } else {
                log.error("Cannot find person with id {}", appointment.getPersonId());
            }
        }
        if (appointment.getLocationId() != null) {
            Optional<Location> location = locationDao.findById(appointment.getLocationId());
            if (location.isPresent()) {
                dto.setLocation(ObjectMapper.getInstance().mapDomainToDto(location.get()));
            } else {
                log.error("Cannot find location with id {}", appointment.getLocationId());
            }
        }
        return dto;
    }

    public Person createPerson(PersonDto personDto) throws ValidationException {
        validationUtils.validatePersonDto(personDto);
        try {
            Person person = ObjectMapper.getInstance().mapDtoToDomain(personDto);
            return personDao.save(person);
        } catch (Exception e) {
            log.error("An unexpected exception occurred creating a person", e);
            return null;
        }
    }

    public Location createLocation(LocationDto locationDto) throws ValidationException {
        validationUtils.validateLocationDto(locationDto);
        try {
            Location location = ObjectMapper.getInstance().mapDtoToDomain(locationDto);
            return locationDao.save(location);
        } catch (Exception e) {
            log.error("An unexpected exception occurred creating a location", e);
            return null;
        }
    }

    private static class AppointmentComparator implements Comparator<Appointment> {
        @Override
        public int compare(Appointment a1, Appointment a2) {
            int comparedDates = a1.getDate().compareTo(a2.getDate());
            return comparedDates == 0 ?
                    a1.getId().compareTo(a2.getId()) : // use the object ids to break any ties caused by the same date
                    comparedDates;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof AppointmentComparator;
        }
    }

    private static class PersonComparator implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            ObjectToStringComparator comparator = new ObjectToStringComparator();
            int nameComparison = comparator.compare(p1.getName(), p2.getName());
            return nameComparison == 0 ?
                    p1.getId().compareTo(p2.getId()) : // use the object ids to break any ties caused by the same name
                    nameComparison;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PersonComparator;
        }
    }

    private static class LocationComparator implements Comparator<Location> {
        @Override
        public int compare(Location l1, Location l2) {
            ObjectToStringComparator comparator = new ObjectToStringComparator();
            int nameComparison = comparator.compare(l1.getName(), l2.getName());
            return nameComparison == 0 ?
                    l1.getId().compareTo(l2.getId()) : // use the object ids to break any ties caused by the same name
                    nameComparison;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof LocationComparator;
        }
    }

}
