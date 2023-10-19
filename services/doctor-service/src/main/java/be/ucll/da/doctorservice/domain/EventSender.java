package be.ucll.da.doctorservice.domain;

import org.springframework.stereotype.Component;

@Component
public interface EventSender {
    void sendDoctorCreatedEvent(Doctor doctor);
}
