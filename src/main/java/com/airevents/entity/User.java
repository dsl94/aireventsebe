package com.airevents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
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
    @Column( name = "vatsim_id")
    private String vatsimId;
    @Column( name = "ivao_id")
    private String ivaoId;
    @Column( name = "poscon_id")
    private String posconId;
    @JsonIgnore
    private String password;
    private boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> flights;
    private Long minutes;

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
        this.minutes = 0L;
    }
}
