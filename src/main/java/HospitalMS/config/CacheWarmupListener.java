package HospitalMS.config;

import HospitalMS.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheWarmupListener {

    private final DoctorService doctorService;

    public CacheWarmupListener(
            DoctorService doctorService) {

        this.doctorService = doctorService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmDoctorCache() {

        log.info(
                "Doctor cache warm-up started");

        doctorService.getAllDoctors()
                .forEach(doctor ->
                        doctorService.getDoctorById(
                                doctor.getDoctorId()));

        log.info(
                "Doctor cache warm-up completed");
    }
}