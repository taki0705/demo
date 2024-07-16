package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="course")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int course_id;
    @Column(name="course_name")
    private String courseName;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="student_id")
    private StudentEntity student;
    public CourseEntity() {
    }

    public int getCourseId() {
        return course_id;
    }

    public void setCourseId(int course_id) {
        this.course_id = course_id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }
}

