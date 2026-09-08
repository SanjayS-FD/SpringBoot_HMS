package HospitalMS.service;

import java.util.List;

import HospitalMS.exception.AppointmentConflictException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  HospitalMS.exception.*;
import HospitalMS.model.Appointment;
import HospitalMS.repository.AppointmentRepository;
import HospitalMS.repository.DoctorRepository;
import HospitalMS.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public Appointment bookAppointment(Appointment app){
        if (!patientRepository.existsById(app.getPatientId())){
            throw new PatientNotFoundException("Patient not found");
        }

        if (!doctorRepository.existsById(app.getDoctorId())){
            throw new DoctorNotFoundException("Doctor not found");
        }

        boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                app.getDoctorId(),
                app.getAppointmentDate(),
                app.getAppointmentTime());

        if (alreadyBooked) {
            throw new AppointmentConflictException("Doctor already booked for this slot");
        }

        app.setStatus("BOOKED");
        System.out.println("Appointment Booked Successfully!");
        return appointmentRepository.save(app);
    }

    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointment(String id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment updateAppointment(String id, @NonNull Appointment app){
        app.setAppointmentId(id);
        return appointmentRepository.save(app);
    }

    public Appointment completeAppointment(String appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return null;
        }
        appointment.setStatus("COMPLETED");
        return appointmentRepository.save(appointment);
    }


    public Appointment cancelAppointment(String id) {

        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            return null;
        }
        if ("COMPLETED".equals(appointment.getStatus())) {
            throw new RuntimeException("Cannot Cancel for a completed appointment");

        }
        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }


}