package HospitalMS.service;

import java.time.LocalDate;
import java.util.List;

import HospitalMS.dto.BillRequestDTO;
import HospitalMS.enums.AppointmentStatus;
import HospitalMS.exception.AppointmentConflictException;
import HospitalMS.model.Appointment;
import HospitalMS.model.Bill;
import HospitalMS.model.Doctor;
import HospitalMS.repository.AppointmentRepository;
import HospitalMS.repository.BillRepository;
import HospitalMS.repository.DoctorRepository;

import HospitalMS.serviceInterfaces.BillServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService implements BillServiceInterface {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Bill generateBill(BillRequestDTO bill) {

        if (billRepository.existsByAppointmentId(bill.getAppointmentId())) {
            throw new AppointmentConflictException("Bill already generated for this appointment");
        }


        Appointment appointment =
                appointmentRepository.findByAppointmentId(
                                bill.getAppointmentId())
                        .orElseThrow(() ->
                                new AppointmentConflictException(
                                        "Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppointmentConflictException("Bill can only be generated for completed appointments");
        }

        Doctor doctor = doctorRepository.findByDoctorId(
                        appointment.getDoctor().getDoctorId())
                .orElseThrow(() ->
                        new AppointmentConflictException(
                                "Doctor not found"));


        double consultationFee = getConsultationFee(doctor.getSpecialization());

        double totalAmount = consultationFee + bill.getMedicineCost();

        Bill generatedBill = new Bill();


        generatedBill.setAppointmentId(bill.getAppointmentId());
        generatedBill.setMedicineCost(bill.getMedicineCost());
        generatedBill.setConsultationFee(consultationFee);
        generatedBill.setTotalAmount(totalAmount);
        generatedBill.setBillDate(LocalDate.now().toString());

        Bill savedBill =
                billRepository.save(generatedBill);

        savedBill.setBillId(
                "BILL-" + (1000 + savedBill.getId())
        );

        return billRepository.save(savedBill);
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

    @Override
    public Bill getBillById(String id) {
        return billRepository.findByBillId(id).orElse(null);
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public void deleteBill(String id) {
        Bill bill =
                billRepository.findByBillId(id)
                        .orElse(null);

        if (bill != null) {
            billRepository.delete(bill);
        }
    }
}