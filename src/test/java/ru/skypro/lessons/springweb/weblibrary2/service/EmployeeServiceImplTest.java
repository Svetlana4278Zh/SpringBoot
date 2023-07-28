package ru.skypro.lessons.springweb.weblibrary2.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO;
import ru.skypro.lessons.springweb.weblibrary2.exceptions.EmployeeNotFoundException;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Position;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;
import ru.skypro.lessons.springweb.weblibrary2.repository.EmployeeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO.fromEmployee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository mockedEmployeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getAllEmployees_ShouldReturnEmployeeList(){
        List<EmployeeDTO> expected = List.of(
                new EmployeeDTO(1, "Ivan", 100000),
                new EmployeeDTO(2, "Anna", 120000)
        );

        when(mockedEmployeeRepository.findAllEmployeesDTO())
                .thenReturn(expected);

        List<EmployeeDTO> actual = employeeService.getAllEmployees();

        assertEquals(expected, actual);
        verify(mockedEmployeeRepository, times(1)).findAllEmployeesDTO();
    }

    @Test
    void addEmployee_CorrectEmployee_ShouldCallRepository(){
        Employee employee = new Employee(1, "Ivan", 100000, new Position(1, "Manager"));
        employeeService.addEmployee(employee);

        verify(mockedEmployeeRepository, times(1)).save(employee);
    }

    @Test
    void editEmployee_ShouldCallRepository(){
        Employee expected = new Employee(1, "Ivan", 100000, new Position(1, "Manager"));
        int input = 1;

        when(mockedEmployeeRepository.findById(input)).thenReturn(Optional.of(expected));

        employeeService.editEmployee(input,expected);

        verify(mockedEmployeeRepository, times(1)).save(expected);
        verify(mockedEmployeeRepository, times(1)).findById(input);
    }

    @Test
    void editEmployee_IncorrectId_ThrowsException(){
        Employee employee = new Employee(1, "Ivan", 100000, new Position(1, "Manager"));
        int input = 1;

        when(mockedEmployeeRepository.findById(input)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> employeeService.editEmployee(input, employee));
    }

    @Test
    void getEmployeeById_ValidId_ShouldReturnEmployee(){
        Employee employee = new Employee(1, "Ivan", 100000, new Position(1, "Manager"));
        int input = 1;
        EmployeeDTO expected = fromEmployee(employee);

        when(mockedEmployeeRepository.findById(input)).thenReturn(Optional.of(employee));

        EmployeeDTO actual = employeeService.getEmployeeById(input);

        assertEquals(expected, actual);
        verify(mockedEmployeeRepository, times(1)).findById(input);
    }

    @Test
    void deleteEmployeeById_ValidId_ShouldCallRepository(){
        Employee employee = new Employee(1, "Ivan", 100000, new Position(1, "Manager"));
        int input = 1;
        when(mockedEmployeeRepository.findById(input)).thenReturn(Optional.of(employee));
        employeeService.deleteEmployeeById(input);

        verify(mockedEmployeeRepository, times(1)).deleteById(input);
    }

    @Test
    void getEmployeesWithSalaryHigherThan_ShouldCallRepository() {
        List<EmployeeDTO> employees = List.of(
                new EmployeeDTO(1, "Ivan", 100000),
                new EmployeeDTO(2, "Anna", 120000)
        );
        int input = 110000;

        when(mockedEmployeeRepository.findAllEmployeesDTO())
                .thenReturn(employees);

        employeeService.getEmployeesWithSalaryHigherThan(input);

        verify(mockedEmployeeRepository, times(1)).findAllEmployeesDTO();
    }

    @Test
    void getEmployeeswithHighestSalary_ShouldCallRepository() {
        List<EmployeeDTO> employees = List.of(
                new EmployeeDTO(1, "Ivan", 100000),
                new EmployeeDTO(2, "Anna", 120000)
        );

        when(mockedEmployeeRepository.findAllEmployeesDTO())
                .thenReturn(employees);

        employeeService.getEmployeeswithHighestSalary();

        verify(mockedEmployeeRepository, times(2)).findAllEmployeesDTO();
    }

    @ParameterizedTest
    @MethodSource("getEmployeeswithPositionTestParam")
    void getEmployeeswithPositionTest(Position position){
        List<Employee> employees = List.of(
                new Employee(1, "Ivan", 100000, position),
                new Employee(2, "Anna", 120000, position),
                new Employee(3, "Oleg", 130000, position)
        );

        when(mockedEmployeeRepository.findAllEmployees())
                .thenReturn(employees);

        List<EmployeeDTO> expected = employees
                .stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());

        List<EmployeeDTO> actual = employeeService.getEmployeeswithPosition(position.getNamePosition());

        assertEquals(expected, actual);
        verify(mockedEmployeeRepository, times(1)).findAllEmployees();
    }

    @Test
    void getEmployeeByIdFullInfo_ValidId_ShouldReturnEmployee(){
        int input = 1;
        EmployeeFullInfo expected = new EmployeeFullInfo("Ivan", 100000,"Manager");

        when(mockedEmployeeRepository.findByIdFullInfo(input)).thenReturn(expected);

        EmployeeFullInfo actual = employeeService.getEmployeeByIdFullInfo(input);

        assertEquals(expected, actual);
        verify(mockedEmployeeRepository, times(1)).findByIdFullInfo(input);
    }

    @Test
    void getEmployeeWithPaging_ShouldCallRepository(){
        List<Employee> employees = List.of(
                new Employee(1, "Ivan", 100000, new Position(1, "Manager")),
                new Employee(2, "Anna", 90000, new Position(2, "Assistant"))

        );
        Page<Employee> employeePage = new PageImpl<Employee>(employees);

        when(mockedEmployeeRepository.findAll(PageRequest.of(0, 10))).thenReturn(employeePage);

        employeeService.getEmployeeWithPaging(0,10);

        verify(mockedEmployeeRepository, times(1)).findAll(PageRequest.of(0,10));
    }

    public static Stream<Arguments> getEmployeeswithPositionTestParam() {
        return Stream.of(
                Arguments.of(new Position(1, "position1")),
                Arguments.of(new Position(2, "position2")),
                Arguments.of(new Position(3, "position3"))

        );
    }
}
