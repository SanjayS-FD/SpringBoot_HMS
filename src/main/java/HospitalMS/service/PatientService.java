package HospitalMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HospitalMS.model.Patient;
import HospitalMS.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository repository;

    public Patient savePatient(Patient patient) {
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