package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.BasicHealthMetrics;

import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasicHealthMetricsRepository extends JpaRepository<BasicHealthMetrics, Long> {
    List<BasicHealthMetrics> findByPatient(User patient);
}
