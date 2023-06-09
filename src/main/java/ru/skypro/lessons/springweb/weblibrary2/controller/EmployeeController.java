package ru.skypro.lessons.springweb.weblibrary2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;
import ru.skypro.lessons.springweb.weblibrary2.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

//    @GetMapping
//    public List<EmployeeDTO> getAllEmployees(){
//        return employeeService.getAllEmployees();
//    }
    @PostMapping("/")
    public void addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }

    @PutMapping("/{id}")
    public void editEmployee(@PathVariable(value = "id") Integer id, @RequestBody Employee employee){
        employeeService.editEmployee(id, employee);
    }
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable(value = "id") Integer id){
        return employeeService.getEmployeeById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable(value = "id") Integer id){
        employeeService.deleteEmployeeById(id);
    }
    @GetMapping("/salaryHigherThan")
    public List<EmployeeDTO> getEmployeesWithSalaryHigherThan(@RequestParam("salary") Integer salary) {
        return employeeService.getEmployeesWithSalaryHigherThan(salary);
    }
    @GetMapping("/withHighestSalary")
    public List<EmployeeDTO> getEmployeeswithHighestSalary(){
        return employeeService.getEmployeeswithHighestSalary();
    }
    @GetMapping()
    public List<EmployeeDTO> getEmployeeswithPosition(@RequestParam(value = "position", required = false) String position){
        return employeeService.getEmployeeswithPosition(position);
    }
    @GetMapping("/{id}/fullInfo")
    public EmployeeFullInfo getEmployeeByIdFullInfo (@PathVariable(value = "id") Integer id) {
        return employeeService.getEmployeeByIdFullInfo(id);
    }
    @GetMapping("/page")
    public List<EmployeeDTO> getEmployeeWithPaging(@RequestParam(value = "page",required = false) Integer page){
        Integer unitPerPage = 10;
        return employeeService.getEmployeeWithPaging(page,unitPerPage);
    }
    @PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam("file") MultipartFile file) {

        System.out.println("Размер файла: " + file.getSize() + " байт");
        employeeService.addEmployeeFromFile(file);
    }
}
