package ru.skypro.lessons.springweb.weblibrary2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public List<EmployeeDTO> getAllEmployees() {
        logger.info("Was invoked method for get a list of employees");
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllEmployees()
                .stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
        logger.debug("Successful access to the database");
        return employeeDTOList;
    }

    @Override
    public void addEmployee(Employee employee) {
        logger.info("Was invoked method for add a employee");
        employeeRepository.save(employee);
        logger.debug("Successful access to the database");
    }

    @Override
    public void editEmployee(int id, Employee employee) {
        logger.info("Was invoked method for edit a employee with id = {}",id);
        Employee employeeEdit = employeeRepository.findById(id);
        employeeEdit.setName(employee.getName());
        employeeEdit.setSalary(employee.getSalary());
        employeeEdit.setPosition(employee.getPosition());
        employeeRepository.save(employeeEdit);
        logger.debug("Successful access to the database");
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        logger.info("Was invoked method for get a employee with id = {}",id);
        EmployeeDTO employeeDTO = fromEmployee(employeeRepository.findById(id));
        logger.debug("Successful access to the database");
        return employeeDTO;
    }

    @Override
    public void deleteEmployeeById(int id) {
        logger.info("Was invoked method for delete a employee with id = {}",id);
        employeeRepository.deleteById(id);
        logger.debug("Successful access to the database");
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithSalaryHigherThan(int salary) {
        logger.info("Was invoked method for get a list of employees with salary higher than {}",salary);
        List<EmployeeDTO> employeeDTOList = getAllEmployees().stream()
                .filter(employee -> employee.getSalary() > salary)
                .collect(Collectors.toList());
        logger.debug("Successful access to the database");
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO getEmployeeWithMaxSalary() {
        logger.info("Was invoked method for get a employee with max salary");
        EmployeeDTO employeeDTO = getAllEmployees().stream()
                .max((o1, o2) -> o1.getSalary()-o2.getSalary())
                .get();
        logger.debug("Successful access to the database");
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeeswithHighestSalary() {
        logger.info("Was invoked method for get a list of employees with highest salary");
        int maxSalary = getEmployeeWithMaxSalary().getSalary();
        List<EmployeeDTO> employeeDTOList = getAllEmployees().stream()
                .filter(employee -> employee.getSalary() == maxSalary)
                .collect(Collectors.toList());
        logger.debug("Successful access to the database");
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> getEmployeeswithPosition(String position) {
        logger.info("Was invoked method for get a list of employees with position {}",position);
        try {
            position.isEmpty();
            List<EmployeeDTO> employeeDTOList = employeeRepository.findAllEmployees().stream()
                    .filter(employee -> employee.getPosition().getNamePosition().equals(position))
                    .map(EmployeeDTO::fromEmployee)
                    .collect(Collectors.toList());
            logger.debug("Successful access to the database");
            return employeeDTOList;
        } catch (NullPointerException e){
            logger.error(e.getMessage(),e);
            return getAllEmployees();
        }
    }

    @Override
    public List<EmployeeFullInfo> findAllEmployeeFullInfo() {
        logger.info("Was invoked method for get a list of employees, full info");
        List<EmployeeFullInfo> employeeFullInfoList = employeeRepository.findAllEmployeeFullInfo();
        logger.debug("Successful access to the database");
        return employeeFullInfoList;
    }

    @Override
    public EmployeeFullInfo getEmployeeByIdFullInfo(int id) {
        logger.info("Was invoked method for get a employee with id = {}, full info", id);
        EmployeeFullInfo employeeFullInfo = employeeRepository.findByIdFullInfo(id);
        logger.debug("Successful access to the database");
        return employeeFullInfo;
    }

    @Override
    public List<EmployeeDTO> getEmployeeWithPaging(Integer pageIndex, Integer unitPerPage) {
        logger.info("Was invoked method for get a list of employees, page {}", pageIndex);
        if (pageIndex == null){
            pageIndex = 0;
        }
        Pageable employeeOfConcretePage = PageRequest.of(pageIndex, unitPerPage);
        Page<Employee> page = employeeRepository.findAll(employeeOfConcretePage);

        List<EmployeeDTO> employeeDTOList = page.stream()
                .map(EmployeeDTO::fromEmployee)
                .toList();
        logger.debug("Successful access to the database");
        return employeeDTOList;
    }

    public void addEmployeeFromFile(MultipartFile file){
        logger.info("Was invoked method for add employees from file \"{}\"", file.getOriginalFilename());
        File fileEmployee = new File(file.getOriginalFilename());
        try {
            List<Employee> employeeList = objectMapper.readValue(fileEmployee, new TypeReference<>(){});
            for (Employee value : employeeList) {
                employeeRepository.save(value);
            }
            logger.debug("Successful access to the database");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public int report(){
        logger.info("Was invoked method for save the report and get its id");
        try{
            Report report = new Report();
            report.setReport(buildReport());
            int reportId = reportRepository.save(report).getId();
            logger.debug("Successful access to the database");
            return reportId;
        } catch (JsonProcessingException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new InternalServerError();
        }
    }
    public String buildReport() throws JsonProcessingException {
        logger.info("Was invoked method for generating the report");
        List<ReportDTO> reports = employeeRepository.buildReportsDTO();
        return objectMapper.writeValueAsString(reports);
    }

    public Resource downloadReport(int id){
        logger.info("Was invoked method for download the report");
        return new ByteArrayResource(reportRepository.findById(id)
                .orElseThrow(ReportNotFoundException::new)
                .getReport()
                .getBytes(StandardCharsets.UTF_8)
        );
    }
}
