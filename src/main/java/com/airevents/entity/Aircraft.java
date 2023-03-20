package com.airevents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String icao;
    private String name;
    private Long price;
    private Long maxPassengers;
    private Long maxCargo;
    private boolean cargo;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> flights;
}
