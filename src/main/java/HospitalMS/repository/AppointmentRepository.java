package HospitalMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HospitalMS.model.Appointment;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, String> {

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            String doctorId,
            String appointmentDate,
            String appointmentTime
    );
}