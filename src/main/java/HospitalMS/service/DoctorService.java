package HospitalMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HospitalMS.model.Doctor;
import HospitalMS.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository repository;

    public Doctor saveDoctor(Doctor doctor) {
        return repository.save(doctor);
    }

    public Doctor getDoctorById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<Doctor> getAllDoctors() {
        return repository.findAll();
    }

    public void deleteDoctor(String id) {
        repository.deleteById(id);
    }
}