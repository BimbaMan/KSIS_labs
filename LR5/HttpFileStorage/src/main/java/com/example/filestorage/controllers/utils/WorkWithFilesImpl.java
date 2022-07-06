package com.example.filestorage.controllers.utils;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

@Service
public class WorkWithFilesImpl  implements WorkWithFiles {

    @Override
    public HttpServletResponse getFileInfo(File file, HttpServletResponse response) {
        response.addHeader("NAME", file.getName());
        response.addHeader("SIZE", String.valueOf(file.length()));
        if (file.isDirectory()) {
            response.addHeader("TYPE", "DIRECTORY");
        }
        else {
            response.addHeader("TYPE", "FILE");
        }
        return response;
    }

    @Override
    public ArrayList<String> getFileNames(File[] listOfFiles) {
        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileNames.add("File: " + file.getName());
            }
            if (file.isDirectory()) {
                fileNames.add("Directory: " + file.getName());
            }
        }
        Collections.sort(fileNames);
        return fileNames;
    }

    @Override
    public void saveFile(String filePath, String fileContent) throws IOException {
        Path path = Paths.get(filePath);
        try {
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent);
            fileWriter.close();
        }
        catch(Exception e) {
            String[] pathParts = filePath.split("/");
            pathParts = Arrays.copyOf(pathParts, pathParts.length - 1);
            String pathToCreateFolders = "";
            for (int i = 0; i < pathParts.length; i++) {
                 pathToCreateFolders = pathToCreateFolders.concat(pathParts[i] + "\\");
                //check if folder exists
                createFolderIfNotExists(pathToCreateFolders);
            }

            Files.createFile(path);
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent);
            fileWriter.close();
        }
    }

    private void createFolderIfNotExists(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void delete(String path, HttpServletResponse response) throws IOException {
        File fileToDelete = new File(path);
        if (fileToDelete.exists()) {
            if (fileToDelete.isFile()) {
                fileToDelete.delete();
                response.getWriter().write(fileToDelete.getName() + ": File deleted");
            }
            else {
                //delete folder
                Files.walk(fileToDelete.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);

                response.getWriter().write(fileToDelete.getName() + ": Folder deleted");;
            }
        }
        else {
            response.getWriter().write(fileToDelete.getName() + ": Failed to delete file");
        }
    }
}
