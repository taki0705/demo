package com.example.demo.exception;

public class FileExistsExeception extends RuntimeException{
    public FileExistsExeception(String messege){
        super(messege);
    }
}
