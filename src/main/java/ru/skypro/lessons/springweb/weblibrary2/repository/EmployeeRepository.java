package ru.skypro.lessons.springweb.weblibrary2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.repository.CrudRepository;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Query(value = "SELECT * FROM employee",
            nativeQuery = true)
    List<Employee> findAllEmployees();
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

}
