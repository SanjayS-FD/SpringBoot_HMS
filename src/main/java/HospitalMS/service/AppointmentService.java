package HospitalMS.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

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


@Slf4j
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

        log.info(
                "Appointment booking request received for patient {} and doctor {}",
                dto.getPatientId(),
                dto.getDoctorId());

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

        /*
         * Appointments must be booked at least
         * one day in advance
         */
        if (!appointmentDate.isAfter(LocalDate.now())) {

            log.warn(
                    "Attempted same-day or past booking for {}",
                    appointmentDate);

            throw new AppointmentConflictException(
                    "Appointments must be booked at least one day in advance");
        }

        /*
         * Maximum booking window = 3 months
         */
        if (appointmentDate.isAfter(
                LocalDate.now().plusMonths(3))) {

            log.warn(
                    "Appointment date {} exceeds booking window",
                    appointmentDate);

            throw new AppointmentConflictException(
                    "Appointments can only be booked up to 3 months in advance");
        }

        /*
         * Doctor working hours
         */
        LocalTime startTime =
                LocalTime.of(9, 0);

        LocalTime endTime =
                LocalTime.of(17, 0);

        if (appointmentTime.isBefore(startTime)
                || appointmentTime.isAfter(endTime)) {

            log.warn(
                    "Invalid appointment time {}",
                    appointmentTime);

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

            log.warn(
                    "Doctor {} already booked on {} at {}",
                    dto.getDoctorId(),
                    dto.getAppointmentDate(),
                    dto.getAppointmentTime());

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

        log.info(
                "Appointment booked successfully with ID {}",
                savedAppointment.getAppointmentId());

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

            log.warn(
                    "Deleting appointment {}",
                    id);

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

        log.info(
                "Completing appointment {}",
                appointmentId);

        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId).orElse(null);
        if (appointment == null) {
            return null;
        }
        if (AppointmentStatus.CANCELLED == appointment.getStatus()) {
            throw new AppointmentConflictException("Cancelled appointments cannot be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        log.info(
                "Appointment {} marked as COMPLETED",
                appointmentId);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(String id) {

        log.warn(
                "Cancelling appointment {}",
                id);

        Appointment appointment = appointmentRepository.findByAppointmentId(id).orElse(null);
        if (appointment == null) {
            return null;
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentConflictException("Cannot cancel a completed appointment");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);

        log.info(
                "Appointment {} marked as CANCELLED",
                id);
        return appointmentRepository.save(appointment);
    }

    @Override
    public String checkAvailability(String doctorId, String date, String time) {

        log.info(
                "Availability check for doctor {}",
                doctorId);

        boolean booked = appointmentRepository.existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
                                doctorId,
                                date,
                                time);

        if (booked) {

            log.warn(
                    "Doctor {} unavailable on {} at {}",
                    doctorId,
                    date,
                    time);

            return "Doctor Not Available";
        }

        return "Doctor Available";
    }

    public List<Appointment> getTodaysAppointments(
            String doctorId) {

        return appointmentRepository
                .findByDoctor_DoctorIdAndAppointmentDate(
                        doctorId,
                        LocalDate.now().toString());
    }

}