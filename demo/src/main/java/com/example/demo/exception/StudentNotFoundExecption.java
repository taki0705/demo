package com.example.demo.exception;

public class StudentNotFoundExecption extends RuntimeException{
    public StudentNotFoundExecption(String messege){
        super(messege);
    }
}
