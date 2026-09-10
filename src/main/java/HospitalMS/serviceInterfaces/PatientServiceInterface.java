package HospitalMS.serviceInterfaces;

import java.util.List;

import HospitalMS.dto.PatientRequestDTO;
import HospitalMS.model.Patient;

public interface PatientServiceInterface {

    Patient savePatient(PatientRequestDTO dto);

    Patient getPatientById(String id);

    List<Patient> getAllPatients();

    void deletePatient(String id);
}