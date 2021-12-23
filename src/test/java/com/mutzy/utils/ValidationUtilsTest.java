package com.mutzy.utils;

import com.mutzy.TestHelper;
import com.mutzy.dao.LocationDao;
import com.mutzy.dao.PersonDao;
import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ValidationException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class ValidationUtilsTest {

    private final PersonDao personDao = Mockito.mock(PersonDao.class);
    private final LocationDao locationDao = Mockito.mock(LocationDao.class);
    private final ValidationUtils validationUtils = new ValidationUtils(personDao, locationDao);

    @BeforeEach
    void setupMocks() {
        // setup mock daos to successfully find something from the database to not interfere with other validation tests
        Mockito.when(personDao.findById(any())).thenReturn(Optional.of(TestHelper.createPerson()));
        Mockito.when(locationDao.findById(any())).thenReturn(Optional.of(TestHelper.createLocation()));
    }

    @Test
    void testValidAppointment() {
        Assertions.assertDoesNotThrow(() -> validationUtils.validateAppointmentDto(TestHelper.createAppointmentDto()));
    }

    @Test
    void testNullAppointmentDto() {
        helpTestAppointmentValidationDto(null);
    }

    @Test
    void testInvalidAppointmentDto_InvalidDescription() { //TODO allow punctuation
        AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        dto.setDescription("");
        helpTestAppointmentValidationDto(dto);

        dto.setDescription(null);
        helpTestAppointmentValidationDto(dto);

        dto.setDescription("Mostly valid %##() String with ^^! non-alphanumeric characters");
        helpTestAppointmentValidationDto(dto);
    }

    @Test
    void testInvalidAppointmentDto_InvalidDate() {
        AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        dto.setDate("");
        helpTestAppointmentValidationDto(dto);

        dto.setDate(null);
        helpTestAppointmentValidationDto(dto);

        dto.setDate("letters in this string");
        helpTestAppointmentValidationDto(dto);

        dto.setDate("2000/50/10");
        helpTestAppointmentValidationDto(dto);
    }

    @Test
    void testInvalidAppointmentDto_InvalidTime() {
        AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        dto.setTime("");
        helpTestAppointmentValidationDto(dto);

        dto.setTime(null);
        helpTestAppointmentValidationDto(dto);

        dto.setTime("letters in this string");
        helpTestAppointmentValidationDto(dto);

        dto.setTime("&$%&&!");
        helpTestAppointmentValidationDto(dto);
    }

    @Test
    void testAppointmentDtoWithNullPersonId_ShouldBeValid() {
        final AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        dto.setPersonId(null);

        Assertions.assertDoesNotThrow(() -> validationUtils.validateAppointmentDto(dto));
        Mockito.verify(personDao, Mockito.never()).findById(any());
    }

    @Test
    void testAppointmentDtoWithNullLocationId_ShouldBeValid() {
        final AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        dto.setLocationId(null);

        Assertions.assertDoesNotThrow(() -> validationUtils.validateAppointmentDto(dto));
        Mockito.verify(locationDao, Mockito.never()).findById(any());
    }

    @Test
    void testValidPersonDto() {
        Assertions.assertDoesNotThrow(() -> validationUtils.validatePersonDto(TestHelper.createPersonDto()));
    }

    @Test
    void testNullPersonDto() {
        helpTestPersonValidationDto(null);
    }

    @Test
    void testInvalidPersonDto_InvalidName() { //TODO allow punctuation
        PersonDto dto = TestHelper.createPersonDto();
        dto.setName("");
        helpTestPersonValidationDto(dto);

        dto.setName(null);
        helpTestPersonValidationDto(dto);

        dto.setName("Mostly valid %##() String with ^^! non-alphanumeric characters");
        helpTestPersonValidationDto(dto);
    }

    @Test
    void testInvalidPersonDto_InvalidAffiliation() { //TODO allow punctuation
        PersonDto dto = TestHelper.createPersonDto();
        dto.setAffiliation("");
        helpTestPersonValidationDto(dto);

        dto.setAffiliation(null);
        helpTestPersonValidationDto(dto);

        dto.setAffiliation("Mostly valid %##() String with ^^! non-alphanumeric characters");
        helpTestPersonValidationDto(dto);
    }

    @Test
    void testValidLocationDto() {
        Assertions.assertDoesNotThrow(() -> validationUtils.validateLocationDto(TestHelper.createLocationDto()));
    }

    @Test
    void testNullLocationDto() {
        helpTestLocationValidationDto(null);
    }

    @Test
    void testInvalidLocationDto_InvalidName() { //TODO allow punctuation
        LocationDto dto = TestHelper.createLocationDto();
        dto.setName("");
        helpTestLocationValidationDto(dto);

        dto.setName(null);
        helpTestLocationValidationDto(dto);

        dto.setName("Mostly valid %##() String with ^^! non-alphanumeric characters");
        helpTestLocationValidationDto(dto);
    }

    @Test
    void testInvalidLocationDto_InvalidAffiliation() { //TODO allow punctuation
        LocationDto dto = TestHelper.createLocationDto();
        dto.setDescription("");
        helpTestLocationValidationDto(dto);

        dto.setDescription(null);
        helpTestLocationValidationDto(dto);

        dto.setDescription("Mostly valid %##() String with ^^! non-alphanumeric characters");
        helpTestLocationValidationDto(dto);
    }

    private void helpTestAppointmentValidationDto(AppointmentRequestDto dto) {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> validationUtils.validateAppointmentDto(dto));
        Assertions.assertNotNull(exception.getMessage());
    }

    private void helpTestPersonValidationDto(PersonDto dto) {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> validationUtils.validatePersonDto(dto));
        Assertions.assertNotNull(exception.getMessage());
    }

    private void helpTestLocationValidationDto(LocationDto dto) {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> validationUtils.validateLocationDto(dto));
        Assertions.assertNotNull(exception.getMessage());
    }

}