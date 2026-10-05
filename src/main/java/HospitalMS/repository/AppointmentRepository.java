package HospitalMS.repository;

import HospitalMS.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HospitalMS.model.Appointment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long>{

    Optional<Appointment> findByAppointmentId(String appointmentId);

    boolean existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
            String doctorId,
            String appointmentDate,
            String appointmentTime
    );

    List<Appointment> findByDoctor_DoctorIdAndAppointmentDate(
            String doctorId,
            String appointmentDate);
}