package com.airevents.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "race_report")
public class RaceReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "race_title")
    private String raceTitle;
    @Column(name = "race_date")
    private LocalDateTime dateOfRace;
    private String distance;
    @Column(name = "additional_info")
    private String info;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updateDateTime;
}
