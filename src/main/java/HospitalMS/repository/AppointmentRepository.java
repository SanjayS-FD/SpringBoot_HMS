package HospitalMS.repository;

import HospitalMS.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HospitalMS.model.Appointment;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, String> {

    boolean existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
            String doctorId,
            String appointmentDate,
            String appointmentTime
    );
}