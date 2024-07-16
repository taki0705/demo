package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class StudentEntity {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    @Column
    private String img;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private String address;
    @Column
    private String imgurl;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<CourseEntity> courses  ;
    public List<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
    }

    public String getImg() {
        return img;
    }

    public String setImg(String img) {
        this.img = img;
        return img;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    
}
