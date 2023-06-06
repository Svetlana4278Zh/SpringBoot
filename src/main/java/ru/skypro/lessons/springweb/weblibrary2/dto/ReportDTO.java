package ru.skypro.lessons.springweb.weblibrary2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String position;
    private long count;
    private int maxSalary;
    private int minSalary;
    private double averageSalary;

}
