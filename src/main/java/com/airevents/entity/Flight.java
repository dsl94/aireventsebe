package com.airevents.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_id")
    private Airport departure;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrivall_id")
    private Airport arrival;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double flightLength;
    private Double startFuel;
    private Double endFuel;
    private Double landingRate;
    private Long earning;
    private Long fuelCost;
    private Long profit;
    private String info;
}
