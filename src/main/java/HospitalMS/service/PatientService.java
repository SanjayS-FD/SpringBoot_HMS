package HospitalMS.service;

import java.util.List;

import HospitalMS.dto.PatientRequestDTO;
import HospitalMS.serviceInterfaces.PatientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import HospitalMS.model.Patient;
import HospitalMS.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
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


        log.info(
                "Patient registered successfully with ID {}",
                savedPatient.getPatientId());

        return repository.save(savedPatient);

    }

    @Override
    @Cacheable(
            value = "patients",
            key = "#id")
    public Patient getPatientById(String id) {

        log.info(
                "Fetching patient {} from DATABASE",
                id);

        return repository
                .findByPatientId(id)
                .orElse(null);
    }


    @Override
    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    @Override
    @CacheEvict(
            value = "patients",
            key = "#id")
    public void deletePatient(String id) {
        Patient patient = repository.findByPatientId(id).orElse(null);

        log.warn(
                "Deleting patient with ID {}",
                id);

        if (patient != null) {
            repository.delete(patient);
        }
    }

    @Override
    @CachePut(
            value = "patients",
            key = "#id")
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

        log.info("Updating patient {} and cache",
                        id);

        return repository.save(patient);
    }
}