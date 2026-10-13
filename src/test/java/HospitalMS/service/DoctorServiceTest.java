package HospitalMS.service;

import HospitalMS.dto.DoctorRequestDTO;
import HospitalMS.model.Doctor;
import HospitalMS.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {


    @Mock
    private DoctorRepository repository;

    @InjectMocks
    private DoctorService service;

    @Test
    void shouldReturnDoctorWhenDoctorExists() {

        Doctor doctor = new Doctor();

        doctor.setDoctorId("DOC-1001");
        doctor.setName("Dr Test");

        when(repository.findByDoctorId("DOC-1001"))
                .thenReturn(Optional.of(doctor));

        Doctor result =
                service.getDoctorById("DOC-1001");

        assertNotNull(result);

        assertEquals(
                "DOC-1001",
                result.getDoctorId());

        assertEquals(
                "Dr Test",
                result.getName());
    }

    @Test
    void shouldReturnNullWhenDoctorDoesNotExist() {

        when(repository.findByDoctorId("DOC-9999"))
                .thenReturn(Optional.empty());

        Doctor result =
                service.getDoctorById("DOC-9999");

        assertNull(result);
    }

    @Test
    void shouldReturnAllDoctors() {

        Doctor d1 = new Doctor();
        d1.setDoctorId("DOC-1001");

        Doctor d2 = new Doctor();
        d2.setDoctorId("DOC-1002");

        List<Doctor> doctors =
                List.of(d1, d2);

        when(repository.findAll())
                .thenReturn(doctors);

        List<Doctor> result =
                service.getAllDoctors();

        assertEquals(
                2,
                result.size());
    }

    @Test
    void shouldSaveDoctor() {

        DoctorRequestDTO dto =
                new DoctorRequestDTO();

        dto.setName("Dr Test");
        dto.setPhoneNumber("9876543210");
        dto.setSpecialization("Cardiology");

        Doctor savedDoctor =
                new Doctor();

        savedDoctor.setId(1L);

        when(repository.save(any(Doctor.class)))
                .thenReturn(savedDoctor);

        Doctor result =
                service.saveDoctor(dto);

        assertNotNull(result);
    }

    @Test
    void shouldUpdateDoctor() {

        Doctor doctor =
                new Doctor();

        doctor.setDoctorId("DOC-1001");
        doctor.setName("Old Name");

        DoctorRequestDTO dto =
                new DoctorRequestDTO();

        dto.setName("New Name");
        dto.setPhoneNumber("1234567890");
        dto.setSpecialization("General");

        when(repository.findByDoctorId("DOC-1001"))
                .thenReturn(Optional.of(doctor));

        when(repository.save(any(Doctor.class)))
                .thenReturn(doctor);

        Doctor result =
                service.updateDoctor(
                        "DOC-1001",
                        dto);

        assertNotNull(result);

        assertEquals(
                "New Name",
                doctor.getName());
    }

    @Test
    void shouldReturnNullWhenUpdatingMissingDoctor() {

        DoctorRequestDTO dto =
                new DoctorRequestDTO();

        when(repository.findByDoctorId("DOC-9999"))
                .thenReturn(Optional.empty());

        Doctor result =
                service.updateDoctor(
                        "DOC-9999",
                        dto);

        assertNull(result);
    }

    @Test
    void shouldDeleteDoctor() {

        Doctor doctor =
                new Doctor();

        doctor.setDoctorId("DOC-1001");

        when(repository.findByDoctorId("DOC-1001"))
                .thenReturn(Optional.of(doctor));

        service.deleteDoctor("DOC-1001");

        verify(repository)
                .delete(doctor);
    }

    @Test
    void shouldNotDeleteWhenDoctorMissing() {

        when(repository.findByDoctorId("DOC-9999"))
                .thenReturn(Optional.empty());

        service.deleteDoctor("DOC-9999");

        verify(repository, never())
                .delete(any());
    }

}