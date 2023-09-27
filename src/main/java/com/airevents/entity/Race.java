package com.airevents.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Race implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "race_title")
    private String raceTitle;
    @Column(name = "date_of_race")
    private LocalDateTime dateOfRace;
    private String distances;

    @ManyToMany(mappedBy = "races")
    private Set<User> users = new HashSet<>();
}
