package ru.skypro.lessons.springweb.weblibrary2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springweb.weblibrary2.service.EmployeeService;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final EmployeeService employeeService;

    @PostMapping
    public int report(){
        return employeeService.report();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable int id){
        Resource resource = employeeService.downloadReport(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"report.json\"")
                .body(resource);
    }
}
