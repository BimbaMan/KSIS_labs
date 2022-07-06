package com.example.filestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageApplication.class, args);
    }

}

//http://localhost:8080/delete/home/konstantin/Work/testFolder/
//http://localhost:8080/save/home/konstantin/Work/testFolder/file
    