package HospitalMS.serviceInterfaces;

import java.util.List;

import HospitalMS.dto.BillRequestDTO;
import HospitalMS.model.Bill;

public interface BillServiceInterface {

    Bill generateBill(
            BillRequestDTO dto);

    Bill getBillById(
            String id);

    List<Bill> getAllBills();

    void deleteBill(
            String id);
}