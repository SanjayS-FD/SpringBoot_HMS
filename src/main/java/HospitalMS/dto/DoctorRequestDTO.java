package HospitalMS.dto;

import jakarta.validation.constraints.*;

public class DoctorRequestDTO {


    @NotBlank(message = "Doctor name cannot be empty")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain 10 digits"
    )
    private String phoneNumber;

    public DoctorRequestDTO() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}