package HospitalMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HospitalMS.model.Patient;

import java.util.Optional;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientId(
            String patientId);

    boolean existsByPatientId(
            String patientId);
}
