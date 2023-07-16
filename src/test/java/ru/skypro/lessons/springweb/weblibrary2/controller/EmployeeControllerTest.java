package ru.skypro.lessons.springweb.weblibrary2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Position;
import ru.skypro.lessons.springweb.weblibrary2.repository.EmployeeRepository;
import ru.skypro.lessons.springweb.weblibrary2.repository.PositionRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = {"ADMIN"})
public class EmployeeControllerTest {

    private static String EMPLOYEE_URL = "/employees";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void cleanData(){
        employeeRepository.deleteAll();
        positionRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void addEmployeeAndGetEmployeeByIdTest(){
        final String name = "Ivan";
        final int salary = 100_000;
        addEmployee(name, salary);

        int id = employeeRepository.findByName(name).get(0).getId();

        mockMvc.perform(get(EMPLOYEE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(name));
    }

    @SneakyThrows
    @Test
    void editEmployee_ValidId_ShouldEditEmployee(){
        final String name = "Ivan";
        final int salary = 100_000;
        addEmployee(name, salary);
        int id = employeeRepository.findByName(name).get(0).getId();

        final String newName = "Oleg";
        final int newSalary = 120_000;
        String jsonEmployee = getEmployeeAsJsonString(newName, newSalary);

        mockMvc.perform(put(EMPLOYEE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmployee))
                .andExpect(status().isOk());

        mockMvc.perform(get(EMPLOYEE_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(newName))
                .andExpect(jsonPath("$[0].salary").value(newSalary));
    }

    @SneakyThrows
    @Test
    void editEmployee_InvalidId_ThrowsException(){
        final String newName = "Oleg";
        final int newSalary = 120_000;
        String jsonEmployee = getEmployeeAsJsonString(newName, newSalary);
        int id = 0;

        mockMvc.perform(put(EMPLOYEE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmployee))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void deleteEmployee_ValidId_ShouldDeleteEmployee(){
        final String name = "Ivan";
        final int salary = 100_000;
        addEmployee(name, salary);
        int id = employeeRepository.findByName(name).get(0).getId();

        mockMvc.perform(delete(EMPLOYEE_URL + "/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get(EMPLOYEE_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @SneakyThrows
    @Test
    void deleteEmployee_InvalidId_ThrowsException(){
        int id = 0;

        mockMvc.perform(delete(EMPLOYEE_URL + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getEmployeesWithSalaryHigherThanTest(){
        final String name1 = "Ivan";
        final int salary1 = 100_000;
        addEmployee(name1, salary1);
        final String name2 = "Oleg";
        final int salary2 = 120_000;
        addEmployee(name2, salary2);
        final int salaryTest = 110_000;

        mockMvc.perform(get(EMPLOYEE_URL + "/salaryHigherThan?salary=" + salaryTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(name2));
    }

    @SneakyThrows
    @Test
    void getEmployeeswithHighestSalaryTest(){
        final String name1 = "Ivan";
        final int salary1 = 100_000;
        addEmployee(name1, salary1);
        final String name2 = "Oleg";
        final int salary2 = 120_000;
        addEmployee(name2, salary2);

        mockMvc.perform(get(EMPLOYEE_URL + "/withHighestSalary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(name2));
    }

    @SneakyThrows
    @Test
    void getEmployeeswithPositionTest(){
        final Position position = new Position("Manager");
        positionRepository.save(position);
        final String name = "Ivan";
        final int salary = 100_000;
        Employee employee = new Employee(name,salary,position);
        String jsonEmployee = objectMapper.writeValueAsString(employee);

        mockMvc.perform(post(EMPLOYEE_URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmployee))
                .andExpect(status().isOk());

        mockMvc.perform(get(EMPLOYEE_URL + "?position=" + position.getNamePosition()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @SneakyThrows
    @Test
    void getEmployeeByIdFullInfoTest(){
        final Position position = new Position("Manager");
        positionRepository.save(position);
        final String name = "Ivan";
        final int salary = 100_000;
        Employee employee = new Employee(name,salary,position);
        String jsonEmployee = objectMapper.writeValueAsString(employee);

        mockMvc.perform(post(EMPLOYEE_URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmployee))
                .andExpect(status().isOk());

        int id = employeeRepository.findByName(name).get(0).getId();

        mockMvc.perform(get(EMPLOYEE_URL + "/{id}/fullInfo", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @SneakyThrows
    @Test
    void getEmployeeWithPagingTest(){
        final String name1 = "Ivan";
        final int salary1 = 100_000;
        addEmployee(name1, salary1);
        final String name2 = "Oleg";
        final int salary2 = 120_000;
        addEmployee(name2, salary2);

        mockMvc.perform(get(EMPLOYEE_URL + "/page?page="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[1].name").value(name2));
    }

    private void addEmployee(String name, int salary) throws Exception{
        String jsonEmployee = getEmployeeAsJsonString(name, salary);

        mockMvc.perform(post(EMPLOYEE_URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmployee))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    private String getEmployeeAsJsonString(String name, int salary){
        Employee employee = new Employee(name,salary);
        return objectMapper.writeValueAsString(employee);
    }
}
