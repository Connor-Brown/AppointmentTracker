package com.mutzy.utils;

import com.mutzy.TestHelper;
import com.mutzy.domain.Appointment;
import com.mutzy.domain.Location;
import com.mutzy.domain.Person;
import com.mutzy.dto.AppointmentRequestDto;
import com.mutzy.dto.LocationDto;
import com.mutzy.dto.PersonDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ObjectMapperTest {

    @Test
    void testMapAppointmentDtoToDomain() throws Exception {
        AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        Appointment mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);

        Assertions.assertEquals(dto.getPersonId(), mappedDomain.getPersonId());
        Assertions.assertEquals(dto.getLocationId(), mappedDomain.getLocationId());
        Assertions.assertEquals(dto.getDescription(), mappedDomain.getDescription());
        Assertions.assertEquals(Constants.DATE_TIME_FORMAT.parse(dto.getDate() + " " + dto.getTime()), mappedDomain.getDate());
    }

    @Test
    void testMapAppointmentWithTrimmedValue() {
        AppointmentRequestDto dto = TestHelper.createAppointmentDto();
        dto.setDescription(RandomStringUtils.randomAlphanumeric(Constants.MAX_APPOINTMENT_DESCRIPTION_LENGTH + 10));
        Appointment mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);
        Assertions.assertEquals(dto.getDescription().substring(0, Constants.MAX_APPOINTMENT_DESCRIPTION_LENGTH), mappedDomain.getDescription());
    }

    @Test
    void testMapPersonDtoToDomain() {
        PersonDto dto = TestHelper.createPersonDto();
        Person mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);

        Assertions.assertEquals(dto.getName(), mappedDomain.getName());
        Assertions.assertEquals(dto.getAffiliation(), mappedDomain.getAffiliation());
    }

    @Test
    void testMapPersonWithTrimmedValue() {
        PersonDto dto = TestHelper.createPersonDto();
        dto.setName(RandomStringUtils.randomAlphanumeric(Constants.MAX_PERSON_NAME_LENGTH + 10));
        Person mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);
        Assertions.assertEquals(dto.getName().substring(0, Constants.MAX_PERSON_NAME_LENGTH), mappedDomain.getName());

        dto = TestHelper.createPersonDto();
        dto.setAffiliation(RandomStringUtils.randomAlphanumeric(Constants.MAX_PERSON_AFFILIATION_LENGTH + 10));
        mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);
        Assertions.assertEquals(dto.getAffiliation().substring(0, Constants.MAX_PERSON_AFFILIATION_LENGTH), mappedDomain.getAffiliation());
    }

    @Test
    void testMapLocationDtoToDomain() {
        LocationDto dto = TestHelper.createLocationDto();
        Location mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);

        Assertions.assertEquals(dto.getName(), mappedDomain.getName());
        Assertions.assertEquals(dto.getDescription(), mappedDomain.getDescription());
    }

    @Test
    void testMapLocationWithTrimmedValue() {
        LocationDto dto = TestHelper.createLocationDto();
        dto.setName(RandomStringUtils.randomAlphanumeric(Constants.MAX_LOCATION_NAME_LENGTH + 10));
        Location mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);
        Assertions.assertEquals(dto.getName().substring(0, Constants.MAX_LOCATION_NAME_LENGTH), mappedDomain.getName());

        dto = TestHelper.createLocationDto();
        dto.setDescription(RandomStringUtils.randomAlphanumeric(Constants.MAX_LOCATION_DESCRIPTION_LENGTH + 10));
        mappedDomain = ObjectMapper.getInstance().mapDtoToDomain(dto);
        Assertions.assertEquals(dto.getDescription().substring(0, Constants.MAX_LOCATION_DESCRIPTION_LENGTH), mappedDomain.getDescription());
    }

    @Test
    void testMapPersonDomainToDto() {
        Person domain = TestHelper.createPerson();
        PersonDto dto = ObjectMapper.getInstance().mapDomainToDto(domain);
        Assertions.assertEquals(domain.getId(), dto.getId());
        Assertions.assertEquals(domain.getName(), dto.getName());
        Assertions.assertEquals(domain.getAffiliation(), dto.getAffiliation());
    }

    @Test
    void testMapLocationDomainToDto() {
        Location domain = TestHelper.createLocation();
        LocationDto dto = ObjectMapper.getInstance().mapDomainToDto(domain);
        Assertions.assertEquals(domain.getId(), dto.getId());
        Assertions.assertEquals(domain.getName(), dto.getName());
        Assertions.assertEquals(domain.getDescription(), dto.getDescription());
    }

}