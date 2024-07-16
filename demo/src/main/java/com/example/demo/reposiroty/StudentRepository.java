package com.example.demo.reposiroty;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Long> {
    @Query(value = "SELECT * FROM student_entity WHERE name LIKE %:keyword%", nativeQuery = true)
    List<StudentEntity> findByKeyword(@Param("keyword") String keyword);
    StudentEntity findById(Integer id);
}
