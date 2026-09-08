package HospitalMS.controller;

import java.util.List;

import HospitalMS.dto.PatientRequestDTO;
import org.hibernate.annotations.NamedEntityGraphs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import HospitalMS.model.Patient;
import HospitalMS.service.PatientService;

@RestController
@RequestMapping("/patients")

public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping
    public List<Patient> getAllPatients() {
        return service.getAllPatients();
    }

    @PostMapping
    public Patient savePatient(@RequestBody PatientRequestDTO patient){
        return service.savePatient(patient);
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable String id) {
        return service.getPatientById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable String id) {

        Patient p = service.getPatientById(id);
        if (p == null){
            return "Patient Not Found";
        }

        service.deletePatient(id);
        return "\n Patient Deleted";
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable String id, @RequestBody PatientRequestDTO patient) {
        patient.setPatientId(id);
        return service.savePatient(patient);
    }


}