package ru.skypro.lessons.springweb.weblibrary2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO;
import ru.skypro.lessons.springweb.weblibrary2.dto.ReportDTO;
import ru.skypro.lessons.springweb.weblibrary2.exceptions.InternalServerError;
import ru.skypro.lessons.springweb.weblibrary2.exceptions.ReportNotFoundException;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Report;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;
import ru.skypro.lessons.springweb.weblibrary2.repository.EmployeeRepository;
import ru.skypro.lessons.springweb.weblibrary2.repository.ReportRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO.fromEmployee;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReportRepository reportRepository;

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

    public void addEmployeeFromFile(MultipartFile file){
        File fileEmployee = new File(file.getOriginalFilename());
        try {
            List<Employee> employeeList = objectMapper.readValue(fileEmployee, new TypeReference<>(){});
            for (Employee value : employeeList) {
                employeeRepository.save(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int report(){
        try{
            Report report = new Report();
            report.setReport(buildReport());
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e){
            e.printStackTrace();
            throw new InternalServerError();
        }
    }
    public String buildReport() throws JsonProcessingException {
        List<ReportDTO> reports = employeeRepository.buildReportsDTO();
        return objectMapper.writeValueAsString(reports);
    }

    public Resource downloadReport(int id){
        return new ByteArrayResource(reportRepository.findById(id)
                .orElseThrow(ReportNotFoundException::new)
                .getReport()
                .getBytes(StandardCharsets.UTF_8)
        );
    }
}
