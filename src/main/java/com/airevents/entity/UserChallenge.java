package com.airevents.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_challenge")
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Challenge challenge;
    private double distance;
}
