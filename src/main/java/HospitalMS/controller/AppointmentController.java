package HospitalMS.controller;

import java.util.List;

import HospitalMS.dto.AppointmentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import HospitalMS.model.Appointment;
import HospitalMS.serviceInterfaces.AppointmentServiceInterface;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentServiceInterface service;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return service.getAllAppointments();
    }

    @PostMapping
    public Appointment bookAppointment(@RequestBody AppointmentRequestDTO appointment) {
        return service.bookAppointment(appointment);
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable String id) {
        return service.getAppointmentById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable String id) {

        Appointment appointment = service.getAppointmentById(id);

        if (appointment == null) {
            return "Appointment Not Found";
        }

        service.deleteAppointment(id);
        return "Appointment Deleted Successfully";
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable String id, @RequestBody AppointmentRequestDTO dto) {
        return service.updateAppointment(id, dto);
    }

    @PutMapping("/{id}/complete")
    public Appointment completeAppointment(@PathVariable String id) {
        return service.completeAppointment(id);
    }

    @PutMapping("/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable String id) {
        return service.cancelAppointment(id);
    }
}