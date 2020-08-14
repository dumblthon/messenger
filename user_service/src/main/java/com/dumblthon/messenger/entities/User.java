package com.dumblthon.messenger.entities;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private @Id Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean active;

}