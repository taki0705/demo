package com.example.demo.service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.StudentEntity;
import com.example.demo.exception.FileExistsExeception;
import com.example.demo.reposiroty.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Value("${project.poster}")
    private String path;
    private final FileSerive fileService;

    @Autowired
    public StudentService(StudentRepository studentRepository, FileSerive fileService) {
        this.studentRepository = studentRepository;
        this.fileService = fileService;
    }

    public List<StudentDTO> allStudent() {
        List<StudentEntity> students = studentRepository.findAll();
        List<StudentDTO> studentDTOs = new ArrayList<>();

        for (StudentEntity student : students) {
            studentDTOs.add(convertToDTO(student));
        }

        return studentDTOs;
    }
    public StudentDTO getOneStudent(int id) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById((long) id);
        if (optionalStudent.isPresent()) {
            return convertToDTO(optionalStudent.get());
        } else {
            throw new RuntimeException("Student not found with id " + id);
        }
    }

    public String deleteStudent(Integer id) throws IOException {

        StudentEntity studentEntity = studentRepository.findById(id);
        Integer stid=studentEntity.getId();
             Files.deleteIfExists(Paths.get(path + File.separator+studentEntity.getImg()));
             studentRepository.delete(studentEntity);

        return "deleted with id "+stid;
    }
    public StudentDTO saveStudent(StudentDTO studentDTO, MultipartFile file) throws IOException, FileExistsExeception {
                if(Files.exists(Paths.get(path+File.separator+file.getOriginalFilename()))){
                    throw new FileExistsExeception("File already exists!");
                }
                String newFilename = fileService.uploadFile(path, file);
                studentDTO.setImg(newFilename);
                StudentEntity studentEntity = convertToEntity(studentDTO);
                StudentEntity savedStudent = studentRepository.save(studentEntity);
                String porturl = "http://localhost:8081/student/upload/" + savedStudent.getImg();
                savedStudent.setImgurl(porturl);
                savedStudent = studentRepository.save(savedStudent);
                StudentDTO updatedStudentDTO = convertToDTO(savedStudent);
                updatedStudentDTO.setImgurl(porturl);
                Files.deleteIfExists(Paths.get(path + File.separator + newFilename));
                return updatedStudentDTO;
            }
    public StudentDTO convertToDTO(StudentEntity studentEntity) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setImg(studentEntity.getImg());
        studentDTO.setId(studentEntity.getId());
        studentDTO.setName(studentEntity.getName());
        studentDTO.setAge(studentEntity.getAge());
        studentDTO.setAddress(studentEntity.getAddress());
        studentDTO.setImgurl(studentEntity.getImgurl());
        return studentDTO;
    }
    public StudentEntity convertToEntity(StudentDTO studentDTO) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setImg(studentDTO.getImg());
        studentEntity.setName(studentDTO.getName());
        studentEntity.setAge(studentDTO.getAge());
        studentEntity.setAddress(studentDTO.getAddress());
        studentEntity.setImgurl(studentDTO.getImgurl());
        return studentEntity;
    }
    public StudentDTO updateStudent(int id, MultipartFile file, StudentDTO studentDTO) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById((long) id);
        if (optionalStudent.isPresent()) {
            StudentEntity studentEntity = optionalStudent.get();
            String oldFilename = studentEntity.getImg();  // Get current image filename

            studentEntity.setName(studentDTO.getName());
            studentEntity.setAge(studentDTO.getAge());
            studentEntity.setAddress(studentDTO.getAddress());
            studentEntity.setImg(studentDTO.getImg());
            if (file != null && !file.isEmpty()) {
                try {
                    if (oldFilename != null && !oldFilename.isEmpty()) {
                        Files.deleteIfExists(Paths.get(path + File.separator + oldFilename));
                    }
                    String newFilename = fileService.uploadFile(path, file);
                    studentEntity.setImg(newFilename);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload file for student with id " + id, e);
                }
            }

            StudentEntity updatedStudent = studentRepository.save(studentEntity);
            String porturl = "http://localhost:8081/student/upload/" + studentEntity.getImg();
            updatedStudent.setImgurl(porturl);
            updatedStudent = studentRepository.save(updatedStudent);
            StudentDTO updatedStudentDTO = convertToDTO(updatedStudent);
            updatedStudentDTO.setImgurl(porturl);
            return updatedStudentDTO;
        } else {
            throw new RuntimeException("Student not found with id " + id);
        }
    }
    public List<StudentDTO> findByKeyword(String keyword) {
        List<StudentEntity> students = studentRepository.findByKeyword(keyword);
        List<StudentDTO> studentDTOs = new ArrayList<>();
        for (StudentEntity student : students) {
            studentDTOs.add(convertToDTO(student));
        }
        return studentDTOs;
    }
    }





