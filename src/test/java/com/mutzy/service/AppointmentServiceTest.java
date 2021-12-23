package com.mutzy.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.mutzy.TestHelper;
import com.mutzy.dao.AppointmentDao;
import com.mutzy.dao.LocationDao;
import com.mutzy.dao.PersonDao;
import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import com.mutzy.utils.Constants;
import com.mutzy.utils.ValidationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AppointmentServiceTest {

    private final AppointmentDao mockAppointmentDao = Mockito.mock(AppointmentDao.class);
    private final PersonDao mockPersonDao = Mockito.mock(PersonDao.class);
    private final LocationDao mockLocationDao = Mockito.mock(LocationDao.class);
    private final ValidationUtils mockValidationUtils = Mockito.mock(ValidationUtils.class);
    private final AppointmentService service = new AppointmentService(mockAppointmentDao, mockPersonDao, mockLocationDao, mockValidationUtils);

    @Test
    void testFindAllAppointments_WhenNoAppointmentsStored() {
        Mockito.when(mockAppointmentDao.findAll()).thenReturn(Collections::emptyIterator);
        List<Appointment> appointments = service.findAllAppointments();
        Assertions.assertNotNull(appointments);
        Assertions.assertTrue(appointments.isEmpty());
    }

    @Test
    void testFindAllAppointments_WithMultipleElements_ShouldReturnSortedList() throws ParseException {
        Appointment june2020 = TestHelper.createAppointment(Constants.DATE_FORMAT.parse("2020-06-01"));
        Appointment may2019 = TestHelper.createAppointment(Constants.DATE_FORMAT.parse("2019-05-13"));
        Appointment january2030 = TestHelper.createAppointment(Constants.DATE_FORMAT.parse("2030-01-27"));

        Mockito.when(mockAppointmentDao.findAll()).thenReturn(() -> new ArrayIterator<>(new Appointment[]{june2020, may2019, january2030}));
        List<Appointment> appointments = service.findAllAppointments();

        Assertions.assertNotNull(appointments);
        Assertions.assertEquals(3, appointments.size());
        Assertions.assertEquals(may2019, appointments.get(0));
        Assertions.assertEquals(june2020, appointments.get(1));
        Assertions.assertEquals(january2030, appointments.get(2));
    }

    @Test
    void testCreateAppointment_WithValidDto() throws ValidationException {
        AppointmentDto dto = TestHelper.createAppointmentDto();
        Appointment expectedAppointment = TestHelper.createAppointment();

        Mockito.when(mockAppointmentDao.save(any())).thenReturn(expectedAppointment);
        Appointment appointment = service.createAppointment(dto);
        Assertions.assertEquals(expectedAppointment, appointment);
    }

    @Test
    void testCreateAppointment_WithInvalidDto() throws ValidationException {
        AppointmentDto dto = TestHelper.createAppointmentDto();
        Mockito.doThrow(new ValidationException("some message")).when(mockValidationUtils).validateAppointmentDto(dto);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> service.createAppointment(dto));
        Assertions.assertNotNull(exception.getMessage());
        Mockito.verify(mockAppointmentDao, Mockito.never()).save(any());
    }

    @Test
    void testFindAllPeople_WhenNoPeopleStored() {
        Mockito.when(mockPersonDao.findAll()).thenReturn(Collections::emptyIterator);
        List<Person> people = service.findAllPeople();
        Assertions.assertNotNull(people);
        Assertions.assertTrue(people.isEmpty());
    }

    @Test
    void testFindAllPeople_WithMultipleElements_ShouldReturnSortedList() {
        Person person1 = TestHelper.createPerson();
        person1.setName("DEF");
        Person person2 = TestHelper.createPerson();
        person2.setName("ABC");
        Person person3 = TestHelper.createPerson();
        person3.setName("GHI");

        Mockito.when(mockPersonDao.findAll()).thenReturn(() -> new ArrayIterator<>(new Person[]{person1, person2, person3}));
        List<Person> people = service.findAllPeople();

        Assertions.assertNotNull(people);
        Assertions.assertEquals(3, people.size());
        Assertions.assertEquals(person2, people.get(0));
        Assertions.assertEquals(person1, people.get(1));
        Assertions.assertEquals(person3, people.get(2));
    }

    @Test
    void testCreatePerson_WithValidDto() throws ValidationException {
        PersonDto dto = TestHelper.createPersonDto();
        Person expectedPerson = TestHelper.createPerson();

        Mockito.when(mockPersonDao.save(any())).thenReturn(expectedPerson);
        Person person = service.createPerson(dto);
        Assertions.assertEquals(expectedPerson, person);
    }

    @Test
    void testCreatePerson_WithInvalidDto() throws ValidationException {
        PersonDto dto = TestHelper.createPersonDto();
        Mockito.doThrow(new ValidationException("some message")).when(mockValidationUtils).validatePersonDto(dto);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> service.createPerson(dto));
        Assertions.assertNotNull(exception.getMessage());
        Mockito.verify(mockPersonDao, Mockito.never()).save(any());
    }

    @Test
    void testFindAllLocations_WhenNoLocationStored() {
        Mockito.when(mockLocationDao.findAll()).thenReturn(Collections::emptyIterator);
        List<Location> locations = service.findAllLocations();
        Assertions.assertNotNull(locations);
        Assertions.assertTrue(locations.isEmpty());
    }

    @Test
    void testFindAllLocations_WithMultipleElements_ShouldReturnSortedList() {
        Location location1 = TestHelper.createLocation();
        location1.setName("DEF");
        Location location2 = TestHelper.createLocation();
        location2.setName("ABC");
        Location location3 = TestHelper.createLocation();
        location3.setName("GHI");

        Mockito.when(mockLocationDao.findAll()).thenReturn(() -> new ArrayIterator<>(new Location[]{location1, location2, location3}));
        List<Location> locations = service.findAllLocations();

        Assertions.assertNotNull(locations);
        Assertions.assertEquals(3, locations.size());
        Assertions.assertEquals(location2, locations.get(0));
        Assertions.assertEquals(location1, locations.get(1));
        Assertions.assertEquals(location3, locations.get(2));
    }

    @Test
    void testCreateLocation_WithValidDto() throws ValidationException {
        LocationDto dto = TestHelper.createLocationDto();
        Location expectedLocation = TestHelper.createLocation();

        Mockito.when(mockLocationDao.save(any())).thenReturn(expectedLocation);
        Location location = service.createLocation(dto);
        Assertions.assertEquals(expectedLocation, location);
    }

    @Test
    void testCreateLocation_WithInvalidDto() throws ValidationException {
        LocationDto dto = TestHelper.createLocationDto();
        Mockito.doThrow(new ValidationException("some message")).when(mockValidationUtils).validateLocationDto(dto);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> service.createLocation(dto));
        Assertions.assertNotNull(exception.getMessage());
        Mockito.verify(mockLocationDao, Mockito.never()).save(any());
    }

}