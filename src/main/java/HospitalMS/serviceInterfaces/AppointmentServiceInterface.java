package HospitalMS.serviceInterfaces;

import java.util.List;

import HospitalMS.dto.AppointmentRequestDTO;
import HospitalMS.model.Appointment;

public interface AppointmentServiceInterface {

    Appointment bookAppointment(AppointmentRequestDTO dto);

    Appointment getAppointmentById(String id);

    List<Appointment> getAllAppointments();

    void deleteAppointment(String id);

    Appointment updateAppointment(String id, AppointmentRequestDTO dto);

    Appointment completeAppointment(String id);

    Appointment cancelAppointment(String id);
}