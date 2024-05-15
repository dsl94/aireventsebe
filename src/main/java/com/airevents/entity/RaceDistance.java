package com.airevents.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class RaceDistance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String distance;
    @ManyToOne
    @JoinColumn(name = "race_type_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RaceType raceType;
}
