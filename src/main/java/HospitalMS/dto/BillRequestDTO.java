package HospitalMS.dto;

public class BillRequestDTO {



    private String appointmentId;

    private Double medicineCost;

    public BillRequestDTO() {
    }


    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Double getMedicineCost() {
        return medicineCost;
    }

    public void setMedicineCost(Double medicineCost) {
        this.medicineCost = medicineCost;
    }
}