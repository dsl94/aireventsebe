package com.airevents.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AircraftMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "sim_key")
    private String simKey;
    private String icao;
}
