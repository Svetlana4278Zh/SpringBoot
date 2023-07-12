package ru.skypro.lessons.springweb.weblibrary2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO;
import ru.skypro.lessons.springweb.weblibrary2.dto.ReportDTO;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = "SELECT * FROM employee",
            nativeQuery = true)
    List<Employee> findAllEmployees();

    @Query(value = "SELECT * FROM employee",
            nativeQuery = true)
    List<EmployeeDTO> findAllEmployeesDTO();

    Employee findById(int id);

    List<Employee> findByName(String name);

    @Query("SELECT new ru.skypro.lessons.springweb.weblibrary2.projections." +
            "EmployeeFullInfo(e.name , e.salary , p.namePosition) " +
            "FROM Employee e join fetch Position p " +
            "WHERE e.position = p")
    List<EmployeeFullInfo> findAllEmployeeFullInfo();
    @Query("SELECT new ru.skypro.lessons.springweb.weblibrary2.projections." +
            "EmployeeFullInfo(e.name , e.salary , p.namePosition) " +
            "FROM Employee e join fetch Position p " +
            "WHERE e.position = p AND e.id = ?1")
    EmployeeFullInfo findByIdFullInfo(int id);

    Page<Employee> findAll(Pageable employeeOfConcretePage);

    @Query("SELECT new ru.skypro.lessons.springweb.weblibrary2.dto."+
            "ReportDTO(e.position.namePosition, COUNT(e.id), MAX(e.salary), MIN(e.salary), AVG(e.salary))"+
            " FROM Employee e GROUP BY e.position.namePosition")
    List<ReportDTO> buildReportsDTO();
}
