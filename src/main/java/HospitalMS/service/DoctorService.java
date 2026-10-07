package HospitalMS.service;

import java.util.List;

import HospitalMS.dto.DoctorRequestDTO;
import HospitalMS.model.Patient;
import HospitalMS.serviceInterfaces.DoctorServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import HospitalMS.model.Doctor;
import HospitalMS.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;


@Slf4j
@Service
public class DoctorService implements DoctorServiceInterface {

    @Autowired
    private DoctorRepository repository;

    @Override
    public Doctor saveDoctor(DoctorRequestDTO dto) {

        Doctor doc = new Doctor();

        doc.setName(dto.getName());
        doc.setPhoneNumber(dto.getPhoneNumber());
        doc.setSpecialization(dto.getSpecialization());

        Doctor savedDoctor = repository.save(doc);

        savedDoctor.setDoctorId(
                "DOC-" + (1000 + savedDoctor.getId())
        );

        log.info(
                "Doctor created successfully with ID {}",
                savedDoctor.getDoctorId());

        return repository.save(savedDoctor);
    }

    @Override
    @Cacheable(
            value = "doctors",
            key = "#id")
    public Doctor getDoctorById(String id) {

        log.info(
                "Fetching doctor {} from DATABASE",
                id);

        return repository.findByDoctorId(id).orElse(null);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return repository.findAll();
    }

    @Override
    @CacheEvict(
            value = "doctors",
            key = "#id")
    public void deleteDoctor(String id) {
        Doctor doctor =
                repository.findByDoctorId(id)
                        .orElse(null);

        log.warn(
                "Deleting doctor with ID {}",
                id);

        if (doctor != null) {
            repository.delete(doctor);
        }

    }

    @Override
    @CachePut(
            value = "doctors",
            key = "#id")
    public Doctor updateDoctor(
            String id,
            DoctorRequestDTO dto) {

        log.info("Updating doctor {} and cache", id);

        Doctor doctor =
                repository.findByDoctorId(id)
                        .orElse(null);

        if (doctor == null) {
            return null;
        }

        doctor.setName(dto.getName());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setSpecialization(dto.getSpecialization());

        return repository.save(doctor);
    }
}