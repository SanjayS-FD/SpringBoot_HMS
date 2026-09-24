package HospitalMS.service;

import java.util.List;

import HospitalMS.dto.AppointmentRequestDTO;
import HospitalMS.enums.AppointmentStatus;
import HospitalMS.exception.AppointmentConflictException;
import HospitalMS.serviceInterfaces.AppointmentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  HospitalMS.exception.*;
import HospitalMS.model.Appointment;
import HospitalMS.repository.AppointmentRepository;
import HospitalMS.repository.DoctorRepository;
import HospitalMS.repository.PatientRepository;
import HospitalMS.model.Patient;
import HospitalMS.model.Doctor;

@Service
public class AppointmentService implements AppointmentServiceInterface {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public Appointment bookAppointment(AppointmentRequestDTO dto) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                        .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                        .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        boolean alreadyBooked = appointmentRepository.existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
                                dto.getDoctorId(),
                                dto.getAppointmentDate(),
                                dto.getAppointmentTime());

        if (alreadyBooked) {
            throw new AppointmentConflictException("Doctor already booked for this slot");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(dto.getAppointmentId());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.BOOKED);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void deleteAppointment(String id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public Appointment updateAppointment(String id, AppointmentRequestDTO dto) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                        .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                        .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        Appointment appointment = appointmentRepository.findById(id)
                        .orElseThrow(() -> new AppointmentConflictException("Appointment not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());

        appointment.setAppointmentTime(dto.getAppointmentTime());
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment completeAppointment(String appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return null;
        }
        if (AppointmentStatus.CANCELLED == appointment.getStatus()) {
            throw new AppointmentConflictException("Cancelled appointments cannot be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(String id) {

        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            return null;
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentConflictException("Cannot cancel a completed appointment");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }


}