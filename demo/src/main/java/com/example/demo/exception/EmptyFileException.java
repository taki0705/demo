package com.example.demo.exception;

public class EmptyFileException extends RuntimeException  {
    public EmptyFileException(String messege) {
        super(messege);
    }
}
