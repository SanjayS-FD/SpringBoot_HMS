package HospitalMS.serviceInterfaces;

import java.util.List;

import HospitalMS.dto.DoctorRequestDTO;
import HospitalMS.model.Doctor;

public interface DoctorServiceInterface {

    Doctor saveDoctor(DoctorRequestDTO dto);

    Doctor getDoctorById(String id);

    Doctor updateDoctor(String id, DoctorRequestDTO dto);

    List<Doctor> getAllDoctors();

    void deleteDoctor(String id);
}