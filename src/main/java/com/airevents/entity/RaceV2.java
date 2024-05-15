package com.airevents.entity;

import com.airevents.repository.RaceDistanceRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "race_v2")
public class RaceV2 implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "race_title")
    private String raceTitle;
    @Column(name = "date_of_race")
    private LocalDateTime dateOfRace;
    private String country;
    private String city;
    @Column(name = "custom_distance")
    private String customDistance;
    @ManyToOne
    @JoinColumn(name = "race_type_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RaceType raceType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "race_distance_join",
            joinColumns = {@JoinColumn(name = "race_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "distance_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Set<RaceDistance> distances = new HashSet<>();
}
