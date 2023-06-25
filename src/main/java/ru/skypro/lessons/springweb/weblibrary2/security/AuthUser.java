package ru.skypro.lessons.springweb.weblibrary2.security;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "auth_user")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "is_enabled",nullable = false)
    private boolean isEnabled;

    @JoinColumn(name = "user_id")
    @OneToMany(fetch = FetchType.EAGER)
    private List<Authority> authorityList;

}
