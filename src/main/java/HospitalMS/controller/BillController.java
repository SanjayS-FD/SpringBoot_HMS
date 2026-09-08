package HospitalMS.controller;

import java.util.List;

import HospitalMS.dto.BillRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import HospitalMS.model.Bill;
import HospitalMS.service.BillService;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService service;

    @PostMapping
    public Bill generateBill(@RequestBody BillRequestDTO bill) {
        return service.generateBill(bill);
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return service.getAllBills();
    }

    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable String id) {
        return service.getBillById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteBill(@PathVariable String id) {

        Bill bill = service.getBillById(id);
        if (bill == null) {
            return "Bill not found";
        }
        service.deleteBill(id);
        return "Bill deleted successfully";
    }
}