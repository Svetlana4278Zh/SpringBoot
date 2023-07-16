package ru.skypro.lessons.springweb.weblibrary2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Position;

public interface PositionRepository extends JpaRepository<Position,Integer> {
}
