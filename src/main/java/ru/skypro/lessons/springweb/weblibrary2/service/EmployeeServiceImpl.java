package ru.skypro.lessons.springweb.weblibrary2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;
import ru.skypro.lessons.springweb.weblibrary2.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO.fromEmployee;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAllEmployees()
                .stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void editEmployee(int id, Employee employee) {
        Employee employeeEdit = employeeRepository.findById(id);
        employeeEdit.setName(employee.getName());
        employeeEdit.setSalary(employee.getSalary());
        employeeEdit.setPosition(employee.getPosition());
        employeeRepository.save(employeeEdit);
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        return fromEmployee(employeeRepository.findById(id));
    }

    @Override
    public void deleteEmployeeById(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithSalaryHigherThan(int salary) {
        return getAllEmployees().stream()
                .filter(employee -> employee.getSalary() > salary)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeWithMaxSalary() {
        return getAllEmployees().stream()
                .max((o1, o2) -> o1.getSalary()-o2.getSalary())
                .get();
    }

    @Override
    public List<EmployeeDTO> getEmployeeswithHighestSalary() {
        int maxSalary = getEmployeeWithMaxSalary().getSalary();
        return getAllEmployees().stream()
                .filter(employee -> employee.getSalary() == maxSalary)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeeswithPosition(String position) {
        try {
            position.isEmpty();
            return employeeRepository.findAllEmployees().stream()
                    .filter(employee -> employee.getPosition().getNamePosition().equals(position))
                    .map(EmployeeDTO::fromEmployee)
                    .collect(Collectors.toList());
        } catch (NullPointerException e){return getAllEmployees();}
    }

    @Override
    public List<EmployeeFullInfo> findAllEmployeeFullInfo() {
        return employeeRepository.findAllEmployeeFullInfo();
    }

    @Override
    public EmployeeFullInfo getEmployeeByIdFullInfo(int id) {
        return employeeRepository.findByIdFullInfo(id);
    }

    @Override
    public List<EmployeeDTO> getEmployeeWithPaging(Integer pageIndex, Integer unitPerPage) {
        if (pageIndex == null){
            pageIndex = 0;
        }
        Pageable employeeOfConcretePage = PageRequest.of(pageIndex, unitPerPage);
        Page<Employee> page = employeeRepository.findAll(employeeOfConcretePage);

        return page.stream()
                .map(EmployeeDTO::fromEmployee)
                .toList();
    }

    //    public int getIndexById(int id){
//        return getAllEmployees().indexOf(getEmployeeById(id));
//    }
}
