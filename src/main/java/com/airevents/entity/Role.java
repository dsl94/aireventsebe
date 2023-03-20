package com.airevents.entity;
import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String role;
}
