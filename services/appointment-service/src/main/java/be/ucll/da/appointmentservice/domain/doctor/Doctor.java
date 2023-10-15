package be.ucll.da.appointmentservice.domain.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Doctor {

    @Id
    @GeneratedValue
    private Long id;

    private String foe;//fieldOfExpertise

    protected Doctor() {}

    public Doctor(Long id,String foe) {
        this.id = id;
        this.foe = foe;
    }

    public Long getId() {
        return id;
    }

    public String getFoe() {
        return foe;
    }
}
