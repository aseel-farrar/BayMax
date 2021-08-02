package com.example.bayMax.Domain;

import javax.persistence.*;

@Entity
public class Requests {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
@ManyToOne
    @JoinColumn(name = "patient_id")
    Users patient;


@ManyToOne
    @JoinColumn(name = "doctor_id")
    Users doctor;

boolean isAccepted;

    public Requests() {
    }

    public Requests(Users patient, Users doctor, boolean isAccepted) {
        this.patient = patient;
        this.doctor = doctor;
        this.isAccepted = isAccepted;
    }

    public long getId() {
        return id;
    }

    public Users getPatient() {
        return patient;
    }

    public Users getDoctor() {
        return doctor;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
