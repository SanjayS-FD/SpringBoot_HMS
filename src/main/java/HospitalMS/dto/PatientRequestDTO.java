package HospitalMS.dto;

import jakarta.validation.constraints.*;

public class PatientRequestDTO {


    @NotBlank(
            message = "Patient name cannot be empty"
    )
    private String name;

    @NotBlank(
            message = "Gender cannot be empty"
    )
    private String gender;

    @NotBlank(
            message = "Address cannot be empty"
    )
    private String address;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain 10 digits"
    )
    private String phoneNum;

    @NotNull(
            message = "Age is mandatory"
    )
    @Min(
            value = 1,
            message = "Age must be greater than 0"
    )
    @Max(
            value = 120,
            message = "Age cannot exceed 120"
    )
    private Integer age;

    public PatientRequestDTO() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}