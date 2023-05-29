package ru.skypro.lessons.springweb.weblibrary2.projections;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeFullInfo {
    private String name;
    private Integer salary;
    private String positionName;
}
