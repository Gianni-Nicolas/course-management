package com.project.course.managment.repositories;

import com.project.course.managment.models.database.Course;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @EntityGraph(attributePaths = {"documents"})
    Optional<Course> findByName(String userName);

    @EntityGraph(attributePaths = {"documents"})
    Page<Course> findByCategory(String category, Pageable pageable);
}
