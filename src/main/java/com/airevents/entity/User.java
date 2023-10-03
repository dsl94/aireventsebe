package com.airevents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String fullName;
    private String email;
    @Column( name = "strava_id")
    private String stravaId;
    @Column( name = "shirt_size")
    private String shirtSize;
    private String gender;
    private String info;
    private String phone;
    @Column( name = "strava_token")
    private String stravaToken;
    @Column( name = "strava_refresh_token")
    private String stravaRefreshToken;
    @JsonIgnore
    private String password;
    private boolean isActive;

    @Column(name = "membership_until")
    private LocalDateTime membershipUntil;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_race",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "race_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Race> races = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RaceReport> raceReports = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updateDateTime;

    @Column(name = "first_login_date")
    private LocalDateTime firstLoginDate;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    public User() {
        this.isActive = true;
    }

    public void addRace(Race race) {
        this.races.add(race);
    }

    public void removeRace(Race race) {
        this.races.remove(race);
    }
}
