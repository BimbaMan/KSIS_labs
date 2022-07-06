package com.example.filestorage.controllers;

import com.example.filestorage.controllers.utils.WorkWithFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

//returns file or folder info in HTTP headers
@RestController
@RequestMapping("/getInfo")
public class FileInfoController {

    private final String ROOT = "C:\\Users\\Иван\\Desktop\\Уник\\КСИС\\testFolder";

    @Autowired
    public FileInfoController(WorkWithFiles workWithFiles) {
        this.workWithFiles = workWithFiles;
    }

    private final WorkWithFiles workWithFiles;

    @GetMapping("/**")
    private void getFileInfo(HttpServletRequest request, HttpServletResponse response) {
        String folderPath = ROOT + request.getServletPath().replace("/getInfo", "\\");
        File file = new File(folderPath);
        workWithFiles.getFileInfo(file, response);
    }
}
