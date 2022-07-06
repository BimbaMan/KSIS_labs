package com.example.filestorage.controllers;

import com.example.filestorage.controllers.utils.WorkWithFiles;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


//returns list of files and folder in JSON
@RestController
@RequestMapping("/getFiles")
public class GetFilesController {

    private final String ROOT = "C:\\Users\\Иван\\Desktop\\Уник\\КСИС\\testFolder";

    @Autowired
    public GetFilesController(WorkWithFiles workWithFiles) {
        this.workWithFiles = workWithFiles;

    }
    private final WorkWithFiles workWithFiles;

    @GetMapping("/**")
    private void getDirContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String folderPath = ROOT + request.getServletPath().replace("/getFiles", "");
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            ArrayList<String> fileNames = workWithFiles.getFileNames(listOfFiles);

            String json = new Gson().toJson(fileNames);
            response.getWriter().write(json);
        }
        else {
            response.getWriter().write("Not a directory");
        }
    }
}
