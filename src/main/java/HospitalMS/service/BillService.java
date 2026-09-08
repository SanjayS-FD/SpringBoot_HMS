package HospitalMS.service;

import java.time.LocalDate;
import java.util.List;

import HospitalMS.exception.AppointmentConflictException;
import HospitalMS.model.Appointment;
import HospitalMS.model.Bill;
import HospitalMS.model.Doctor;
import HospitalMS.repository.AppointmentRepository;
import HospitalMS.repository.BillRepository;
import HospitalMS.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public Bill generateBill(Bill bill) {

        if (billRepository.existsByAppointmentId(bill.getAppointmentId())) {
            throw new AppointmentConflictException("Bill already generated for this appointment");
        }


        Appointment appointment = appointmentRepository.findById(bill.getAppointmentId())
                        .orElseThrow(() -> new AppointmentConflictException("Appointment not found"));

        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new AppointmentConflictException("Bill can only be generated for completed appointments");
        }

        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getDoctorId())
                              .orElseThrow(() -> new AppointmentConflictException("Doctor not found"));


        double consultationFee = getConsultationFee(doctor.getSpecialization());

        double totalAmount = consultationFee + bill.getMedicineCost();

        bill.setConsultationFee(consultationFee);

        bill.setTotalAmount(totalAmount);

        bill.setBillDate(LocalDate.now().toString());
        return billRepository.save(bill);
    }

    private double getConsultationFee(String specialization) {

        return switch (specialization) {
            case "General Physician" -> 500;
            case "Cardiologist" -> 1000;
            case "Neurologist" -> 1500;
            case "Orthopedic" -> 800;
            default -> 500;
        };
    }

    public Bill getBillById(String id) {
        return billRepository.findById(id).orElse(null);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public void deleteBill(String id) {
        billRepository.deleteById(id);
    }
}