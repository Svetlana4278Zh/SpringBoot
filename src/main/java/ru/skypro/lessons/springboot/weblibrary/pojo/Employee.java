package ru.skypro.lessons.springboot.weblibrary.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "salary")
@Data
@AllArgsConstructor
public class Employee {
    private String name;
    private int salary;

}
