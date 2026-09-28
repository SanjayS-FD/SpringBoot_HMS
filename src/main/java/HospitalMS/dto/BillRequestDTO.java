package HospitalMS.dto;

import jakarta.validation.constraints.*;

public class BillRequestDTO {



    @NotBlank(message = "Appointment ID is required")
    private String appointmentId;

    @NotNull(message = "Medicine cost is required")
    @Min(value = 0,
            message = "Medicine cost cannot be negative")
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