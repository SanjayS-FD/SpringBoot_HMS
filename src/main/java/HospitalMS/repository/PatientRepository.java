package HospitalMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HospitalMS.model.Patient;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, String> {

}
