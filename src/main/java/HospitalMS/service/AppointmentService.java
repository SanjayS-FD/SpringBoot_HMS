package HospitalMS.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import HospitalMS.dto.AppointmentRequestDTO;
import HospitalMS.enums.AppointmentStatus;
import HospitalMS.exception.AppointmentConflictException;
import HospitalMS.serviceInterfaces.AppointmentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
    public Appointment bookAppointment(
            AppointmentRequestDTO dto) {

        Patient patient =
                patientRepository.findByPatientId(
                                dto.getPatientId())
                        .orElseThrow(() ->
                                new PatientNotFoundException(
                                        "Patient not found"));

        Doctor doctor =
                doctorRepository.findByDoctorId(
                                dto.getDoctorId())
                        .orElseThrow(() ->
                                new DoctorNotFoundException(
                                        "Doctor not found"));

        LocalDate appointmentDate;
        LocalTime appointmentTime;

        try {

            appointmentDate =
                    LocalDate.parse(
                            dto.getAppointmentDate());

            appointmentTime =
                    LocalTime.parse(
                            dto.getAppointmentTime());

        } catch (DateTimeParseException e) {

            throw new AppointmentConflictException(
                    "Date must be in yyyy-MM-dd and time must be in HH:mm format");
        }

        if (appointmentDate.isBefore(
                LocalDate.now())) {

            throw new AppointmentConflictException(
                    "Appointment date cannot be in the past");
        }

        if (appointmentDate.isAfter(
                LocalDate.now().plusMonths(6))) {

            throw new AppointmentConflictException(
                    "Appointments can only be booked up to 6 months in advance");
        }

        LocalTime startTime =
                LocalTime.of(9, 0);

        LocalTime endTime =
                LocalTime.of(17, 0);

        if (appointmentTime.isBefore(startTime)
                || appointmentTime.isAfter(endTime)) {

            throw new AppointmentConflictException(
                    "Appointments can only be booked between 09:00 AM and 05:00 PM");
        }

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
                                dto.getDoctorId(),
                                dto.getAppointmentDate(),
                                dto.getAppointmentTime());

        if (alreadyBooked) {

            throw new AppointmentConflictException(
                    "Doctor already booked for this slot");
        }

        Appointment appointment =
                new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(
                dto.getAppointmentDate());
        appointment.setAppointmentTime(
                dto.getAppointmentTime());
        appointment.setStatus(
                AppointmentStatus.BOOKED);

        Appointment savedAppointment =
                appointmentRepository.save(
                        appointment);

        savedAppointment.setAppointmentId(
                "APP-" +
                        (1000 + savedAppointment.getId())
        );

        return appointmentRepository.save(
                savedAppointment);
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findByAppointmentId(id).orElse(null);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void deleteAppointment(String id) {
        Appointment appointment =
                appointmentRepository.findByAppointmentId(id)
                        .orElse(null);

        if (appointment != null) {
            appointmentRepository.delete(appointment);
        }
    }

    @Override
    public Appointment updateAppointment(String id, AppointmentRequestDTO dto) {

        Patient patient = patientRepository.findByPatientId(dto.getPatientId())
                        .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findByDoctorId(dto.getDoctorId())
                .orElseThrow(() ->
                        new DoctorNotFoundException("Doctor not found"));

        Appointment appointment =
                appointmentRepository.findByAppointmentId(id)
                        .orElseThrow(() ->
                                new AppointmentConflictException(
                                        "Appointment not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());

        appointment.setAppointmentTime(dto.getAppointmentTime());
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment completeAppointment(String appointmentId) {

        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId).orElse(null);
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

        Appointment appointment = appointmentRepository.findByAppointmentId(id).orElse(null);
        if (appointment == null) {
            return null;
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentConflictException("Cannot cancel a completed appointment");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public String checkAvailability(String doctorId, String date, String time) {

        boolean booked = appointmentRepository.existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
                                doctorId,
                                date,
                                time);

        if (booked) {
            return "Doctor Not Available";
        }

        return "Doctor Available";
    }

}