package ru.skypro.lessons.springweb.weblibrary2.security;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String role;
}
