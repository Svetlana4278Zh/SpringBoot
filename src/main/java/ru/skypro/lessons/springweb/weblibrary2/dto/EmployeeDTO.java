package ru.skypro.lessons.springweb.weblibrary2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;

@Getter
@Setter
public class EmployeeDTO {
    private Integer id;
    private String name;
    private Integer salary;

    public static EmployeeDTO fromEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSalary(employee.getSalary());
        return employeeDTO;
    }

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setId(this.getId());
        employee.setName(this.getName());
        employee.setSalary(this.getSalary());
        return employee;
    }
}
