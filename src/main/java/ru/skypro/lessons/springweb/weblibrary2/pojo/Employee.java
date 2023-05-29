package ru.skypro.lessons.springweb.weblibrary2.pojo;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.stereotype.Component;

@EqualsAndHashCode(of = "salary")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int salary;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
}
