package com.example.filestorage.controllers;

import com.example.filestorage.controllers.utils.WorkWithFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//saves file and create folders if they dont exist
@RestController
@RequestMapping("/save")
public class SaveFileController {

    private final String ROOT = "C:\\Users\\Иван\\Desktop\\Уник\\КСИС\\testFolder";

    @Autowired
    public SaveFileController(WorkWithFiles workWithFiles) {
        this.workWithFiles = workWithFiles;
    }
    private final WorkWithFiles workWithFiles;

    @PutMapping("/**")
    private void saveFile(HttpServletRequest request) throws IOException {
        String filePath = ROOT + request.getServletPath().replace("/save", "\\");
        //get request body
        String fileContent = request.getParameter("FILE_CONTENT");
        System.out.println(fileContent);
        workWithFiles.saveFile(filePath, fileContent);
    }
}
