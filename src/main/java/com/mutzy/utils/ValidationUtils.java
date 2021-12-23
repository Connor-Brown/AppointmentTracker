package com.mutzy.utils;

import com.mutzy.dao.LocationDao;
import com.mutzy.dao.PersonDao;
import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.text.ParseException;

@Service
@Slf4j
public class ValidationUtils {

    private final PersonDao personDao;
    private final LocationDao locationDao;

    private static final String APPOINTMENT_DESCRIPTION_FIELD = "Appointment description";
    private static final String PERSON_NAME_FIELD = "Person name";
    private static final String PERSON_AFFILIATION_FIELD = "Person affiliation";
    private static final String LOCATION_NAME_FIELD = "Location name";
    private static final String LOCATION_DESCRIPTION_FIELD = "Location description";

    @Autowired
    public ValidationUtils(PersonDao personDao, LocationDao locationDao) {
        this.personDao = personDao;
        this.locationDao = locationDao;
    }

    public void validatePersonDto(PersonDto dto) throws ValidationException {
        if (dto == null) {
            throw new ValidationException("Cannot save a blank person");
        }
        validateAlphaNumericPunctuation(dto.getName(), PERSON_NAME_FIELD);
        checkDtoFieldSize(dto.getName(), Constants.MAX_PERSON_NAME_LENGTH, PERSON_NAME_FIELD);
        validateAlphaNumericPunctuation(dto.getAffiliation(), PERSON_AFFILIATION_FIELD);
        checkDtoFieldSize(dto.getAffiliation(), Constants.MAX_PERSON_AFFILIATION_LENGTH, PERSON_AFFILIATION_FIELD);
    }

    public void validateLocationDto(LocationDto dto) throws ValidationException {
        if (dto == null) {
            throw new ValidationException("Cannot save a blank location");
        }
        validateAlphaNumericPunctuation(dto.getName(), LOCATION_NAME_FIELD);
        checkDtoFieldSize(dto.getName(), Constants.MAX_LOCATION_NAME_LENGTH, LOCATION_NAME_FIELD);
        validateAlphaNumericPunctuation(dto.getDescription(), LOCATION_DESCRIPTION_FIELD);
        checkDtoFieldSize(dto.getDescription(), Constants.MAX_LOCATION_DESCRIPTION_LENGTH, LOCATION_DESCRIPTION_FIELD);
    }

    public void validateAppointmentDto(AppointmentRequestDto dto) throws ValidationException {
        if (dto == null) {
            throw new ValidationException("Cannot save a blank appointment");
        }
        validateAlphaNumericPunctuation(dto.getDescription(), "description");
        checkDtoFieldSize(dto.getDescription(), Constants.MAX_APPOINTMENT_DESCRIPTION_LENGTH, APPOINTMENT_DESCRIPTION_FIELD);
        validateDate(dto.getDate());
        validateTime(dto.getTime());
        validatePersonId(dto.getPersonId());
        validateLocationId(dto.getLocationId());
    }

    private void validateDate(String date) throws ValidationException {
        if (StringUtils.isEmpty(date)) {
            throw new ValidationException("Appointment date cannot be empty");
        }
        try {
            // just check if we are able to successfully parse the date
            Constants.DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new ValidationException("Invalid date format");
        }
    }

    private void validateTime(String time) throws ValidationException {
        if (StringUtils.isEmpty(time)) {
            throw new ValidationException("Appointment time cannot be empty");
        }
        try {
            // just check if we are able to successfully parse the time
            Constants.TIME_FORMAT.parse(time);
        } catch (ParseException e) {
            throw new ValidationException("Invalid time format");
        }
    }

    private void validatePersonId(Integer personId) throws ValidationException {
        if (personId != null && !personDao.findById(personId).isPresent()) {
            throw new ValidationException("Invalid selected person");
        }
    }

    private void validateLocationId(Integer locationId) throws ValidationException {
        if(locationId != null && !locationDao.findById(locationId).isPresent()) {
            throw new ValidationException("Invalid selected location");
        }
    }

    private void validateAlphaNumericPunctuation(String value, String fieldName) throws ValidationException {
        if (StringUtils.isEmpty(value)) {
            throw new ValidationException(fieldName + " cannot be empty");
        } else if (!StringUtils.isAlphanumericSpace(value)) { //TODO add punctuation validation
            throw new ValidationException("Appointment " + fieldName + " can only contain letters and/or numbers");
        }
    }

    private void checkDtoFieldSize(String field, int maxSize, String fieldName) {
        if (field.length() > maxSize) {
            log.warn("Users are entering long {} with character count {}. Consider updating the database field size", fieldName, field.length());
        }
    }
}
