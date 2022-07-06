package com.example.filestorage.controllers.utils;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface WorkWithFiles {

    HttpServletResponse getFileInfo(File file, HttpServletResponse response);

    ArrayList<String> getFileNames (File[] listOfFiles);

    void saveFile(String filePath, String fileContent) throws IOException;

    void delete (String path, HttpServletResponse response) throws IOException;
}
