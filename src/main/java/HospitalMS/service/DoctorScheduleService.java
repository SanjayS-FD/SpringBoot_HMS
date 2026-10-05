package HospitalMS.service;

import HospitalMS.model.Appointment;
import HospitalMS.model.Doctor;
import HospitalMS.repository.AppointmentRepository;
import HospitalMS.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class DoctorScheduleService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Scheduled(cron = "*/30 * * * * *") // testing
    // @Scheduled(cron = "0 0 9 * * *") // production
    public void generateDailyDoctorSchedule() {

        log.info("Daily doctor schedule job started");

        String today = LocalDate.now().toString();

        try {

            File folder = new File("reports");

            if (!folder.exists()) {
                folder.mkdir();
            }

            String fileName =
                    "reports/doctor_schedule_" +
                            today +
                            ".txt";

            FileWriter writer =
                    new FileWriter(fileName);

            writer.write(
                    "Doctor Schedule Report\n");
            writer.write(
                    "Date : " + today + "\n\n");

            List<Doctor> doctors =
                    doctorRepository.findAll();

            for (Doctor doctor : doctors) {

                List<Appointment> appointments =
                        appointmentRepository
                                .findByDoctor_DoctorIdAndAppointmentDate(
                                        doctor.getDoctorId(),
                                        today);

                writer.write(
                        "Doctor : "
                                + doctor.getDoctorId()
                                + " ("
                                + doctor.getName()
                                + ")\n");

                writer.write(
                        "Appointments : "
                                + appointments.size()
                                + "\n");

                for (Appointment appointment :
                        appointments) {

                    writer.write(
                            "   Appointment ID : "
                                    + appointment.getAppointmentId()
                                    + "\n");

                    writer.write(
                            "   Patient ID     : "
                                    + appointment.getPatient()
                                    .getPatientId()
                                    + "\n");

                    writer.write(
                            "   Time           : "
                                    + appointment.getAppointmentTime()
                                    + "\n\n");
                }

                writer.write(
                        "---------------------------------\n");
            }

            writer.close();

            log.info(
                    "Doctor schedule report generated successfully");

        } catch (IOException e) {

            log.error(
                    "Error generating doctor schedule report",
                    e);
        }
    }
}
