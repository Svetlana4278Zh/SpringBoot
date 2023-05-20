package ru.skypro.lessons.springweb.weblibrary2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(of = "salary")
@Data
@AllArgsConstructor
public class Employee {
    private int id;
    private String name;
    private int salary;
}
