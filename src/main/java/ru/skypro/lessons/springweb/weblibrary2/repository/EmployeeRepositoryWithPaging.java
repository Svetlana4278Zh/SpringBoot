package ru.skypro.lessons.springweb.weblibrary2.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;

public interface EmployeeRepositoryWithPaging extends PagingAndSortingRepository<Employee, Integer> {

}
