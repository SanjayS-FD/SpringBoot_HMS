package HospitalMS.service;

import java.util.List;

import HospitalMS.dto.PatientRequestDTO;
import HospitalMS.serviceInterfaces.PatientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HospitalMS.model.Patient;
import HospitalMS.repository.PatientRepository;

@Service
public class PatientService implements PatientServiceInterface {

    @Autowired
    private PatientRepository repository;

    public Patient savePatient(PatientRequestDTO dto) {

        Patient patient = new Patient();
        patient.setPatientId(dto.getPatientId());
        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setPhoneNum(dto.getPhoneNum());
        patient.setAge(dto.getAge());
        return repository.save(patient);
    }

    public Patient getPatientById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    public void deletePatient(String id) {
        repository.deleteById(id);
    }
}