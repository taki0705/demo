package com.example.demo.controller;
import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.StudentEntity;
import com.example.demo.exception.EmptyFileException;
import com.example.demo.reposiroty.StudentRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.FileSerive;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private final StudentService studentService;
    @Autowired
    private final StudentRepository studentRepository;
    public StudentController(StudentService studentService, StudentRepository studentRepository, CourseService courseService, FileSerive fileSerive) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.fileSerive = fileSerive;
    }
    private final CourseService courseService;


    @GetMapping
    public List<StudentDTO> getAllStudents() {

        return studentService.allStudent();
    }
    @GetMapping("/{id}")
    public StudentDTO getAllStudents(@PathVariable("id") int id) {
        return studentService.getOneStudent(id);
    }
    @PostMapping
        public ResponseEntity<StudentDTO> addStudent(
            @RequestPart MultipartFile file,
            @RequestPart String studentDTOobj) throws IOException, EmptyFileException {
        if(file.isEmpty()){
            throw new EmptyFileException("File is empty ! Please send another file");
        }
        StudentDTO studentDTO = converttostudentDTO(studentDTOobj);
        StudentDTO savedStudentDTO = studentService.saveStudent(studentDTO, file);
        return ResponseEntity.ok(savedStudentDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable("id") int id,
                                    @RequestPart MultipartFile file,
                                    @RequestPart String studentDTOobj) throws JsonProcessingException {
        StudentDTO studentDTO = converttostudentDTO(studentDTOobj);
        return ResponseEntity.ok(studentService.updateStudent(id, file, studentDTO));
    }
    private StudentDTO converttostudentDTO(String studentDTOobj) throws JsonProcessingException {
        ObjectMapper objectMapper =new ObjectMapper();
        return objectMapper.readValue(studentDTOobj,StudentDTO.class);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }
    @GetMapping("/all")
    public ResponseEntity<Page<StudentEntity>> getPage(Pageable pageable){
        return new ResponseEntity<>(studentRepository.findAll(pageable), HttpStatus.OK);
    }
    @Autowired
    private final FileSerive fileSerive;
    @Value("${project.poster}")
    private String path;
    @PostMapping("/upload")
    public ResponseEntity<String> upLoadFile(@RequestPart MultipartFile file) throws IOException {
         String uploadedFileName=fileSerive.uploadFile(path , file);
        return ResponseEntity.ok("File Uploaded:" +uploadedFileName);

    }
    @GetMapping("/upload/{fileName}")
      public void serviceFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException
    {
        InputStream resourceFile=fileSerive.getResourceFile(path,fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourceFile,response.getOutputStream());
    }
    @GetMapping("/course")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }



}

