package HospitalMS.controller;

import java.util.List;

import HospitalMS.dto.DoctorRequestDTO;
import HospitalMS.model.Patient;
import HospitalMS.service.PatientService;
import org.hibernate.annotations.NamedEntityGraphs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import HospitalMS.model.Doctor;
import HospitalMS.service.DoctorService;

@RestController
@RequestMapping("/doctors")


public class DoctorController {

    @Autowired
    private DoctorService service;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return service.getAllDoctors();
    }

    @PostMapping
    public Doctor saveDoctor(@RequestBody DoctorRequestDTO doctor){
        return service.saveDoctor(doctor);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable String id) {
        return service.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable String id) {

        Doctor p = service.getDoctorById(id);
        if (p == null){
            return "Doctor Not Found";
        }

        service.deleteDoctor(id);
        return "\n Doctor Deleted";
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable String id, @RequestBody DoctorRequestDTO doctor) {
        doctor.setDoctorId(id);
        return service.saveDoctor(doctor);
    }

}
