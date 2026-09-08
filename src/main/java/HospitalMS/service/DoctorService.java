package HospitalMS.service;

import java.util.List;

import HospitalMS.dto.DoctorRequestDTO;
import HospitalMS.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HospitalMS.model.Doctor;
import HospitalMS.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository repository;

    public Doctor saveDoctor(DoctorRequestDTO dto) {

        Doctor doc = new Doctor();
        doc.setDoctorId(dto.getDoctorId());
        doc.setName(dto.getName());
        doc.setPhoneNumber(dto.getPhoneNumber());
        doc.setSpecialization(dto.getSpecialization());
        return repository.save(doc);
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