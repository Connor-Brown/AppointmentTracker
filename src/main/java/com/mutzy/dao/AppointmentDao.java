package com.mutzy.dao;

import com.mutzy.domain.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao extends CrudRepository<Appointment, Integer> {
}
