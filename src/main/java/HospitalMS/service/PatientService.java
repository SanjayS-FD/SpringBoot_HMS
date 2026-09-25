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

    @Override
    public Patient savePatient(PatientRequestDTO dto) {

        Patient patient = new Patient();

        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setPhoneNum(dto.getPhoneNum());
        patient.setAge(dto.getAge());

        Patient savedPatient = repository.save(patient);
        savedPatient.setPatientId("PAT-" + (1000 + savedPatient.getId()));

        return repository.save(savedPatient);
    }

    @Override
    public Patient getPatientById(String id) {
        return repository.findByPatientId(id).orElse(null);
    }


    @Override
    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    @Override
    public void deletePatient(String id) {
        Patient patient = repository.findByPatientId(id).orElse(null);

        if (patient != null) {
            repository.delete(patient);
        }
    }

    @Override
    public Patient updatePatient(
            String id,
            PatientRequestDTO dto) {

        Patient patient =
                repository.findByPatientId(id)
                        .orElse(null);

        if (patient == null) {
            return null;
        }

        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setPhoneNum(dto.getPhoneNum());
        patient.setAge(dto.getAge());

        return repository.save(patient);
    }
}