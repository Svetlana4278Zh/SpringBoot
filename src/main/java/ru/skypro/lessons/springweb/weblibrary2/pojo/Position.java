package ru.skypro.lessons.springweb.weblibrary2.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String namePosition;
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<Employee> employeeList;

    public Position(Integer id, String namePosition) {
        this.id = id;
        this.namePosition = namePosition;
    }
}
