package ru.skypro.lessons.springweb.weblibrary2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Report;

public interface ReportRepository extends JpaRepository <Report, Integer> {
}
