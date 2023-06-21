package ru.skypro.lessons.springweb.weblibrary2.security;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true, length = 16)
    private String role;
}
