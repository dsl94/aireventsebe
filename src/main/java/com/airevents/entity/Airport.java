package com.airevents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Airport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "departure", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> departureFlights;
    @OneToMany(mappedBy = "arrival", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> arrivalFlights;
}
