package com.example.filestorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

//downloads files
@RestController
@RequestMapping("/get")
public class DownloadController {

    private final String ROOT = "C:\\Users\\Иван\\Desktop\\Уник\\КСИС\\testFolder\\";

    @GetMapping("/**")
    private void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filePath = ROOT + request.getServletPath().replace("/get/", "");
        File fileToDownload = new File(filePath);
        if (fileToDownload.exists() && fileToDownload.isFile()) {
            String mimeType = URLConnection.guessContentTypeFromName(fileToDownload.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileToDownload.getName() + "\""));
            response.setContentLength((int) fileToDownload.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(fileToDownload));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            System.out.println("File downloaded successfully from " + filePath);
        }
    }

}
