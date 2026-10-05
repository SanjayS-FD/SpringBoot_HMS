package HospitalMS.serviceInterfaces;

import java.util.List;

import HospitalMS.dto.AppointmentRequestDTO;
import HospitalMS.model.Appointment;
import org.springframework.web.bind.annotation.PathVariable;

public interface AppointmentServiceInterface {

    Appointment bookAppointment(AppointmentRequestDTO dto);

    Appointment getAppointmentById(String id);

    List<Appointment> getAllAppointments();

    void deleteAppointment(String id);

    Appointment updateAppointment(String id, AppointmentRequestDTO dto);

    Appointment completeAppointment(String id);

    Appointment cancelAppointment(String id);

    String checkAvailability(String doctorId, String date, String time);

    List<Appointment> getTodaysAppointments(String doctorId);
}