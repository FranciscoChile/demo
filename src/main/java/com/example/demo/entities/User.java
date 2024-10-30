package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;
    private String email;
    private String password;
    
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String token;
    private boolean isActive;


    @OneToMany(fetch = FetchType.EAGER , mappedBy = "user")
    private Set<Phone> phones;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    

}