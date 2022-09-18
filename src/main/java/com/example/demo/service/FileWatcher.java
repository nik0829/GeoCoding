package com.example.demo.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Component
public class FileWatcher {

    @Autowired
    GeoLocatorDetails geoLocatorDetails;

    public void watchFolder(String path) {
        String FOLDER_PATH = path;
        String PROCESSED_PATH = path + "\\Output_folder";
        String COMPLETE_FILE_PATH = "";

        File PROCESSED_PATH_DIRECTORY = new File(PROCESSED_PATH);
        if (! PROCESSED_PATH_DIRECTORY.exists()){
            PROCESSED_PATH_DIRECTORY.mkdir();
        }

        try {
            log.info("Watching directory for changes :" + path);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path directory = Paths.get(FOLDER_PATH);
            WatchKey watchKey = directory.register(watchService, ENTRY_CREATE);
            while (true) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path fileName = pathEvent.context();
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == ENTRY_CREATE ) {
                        log.info("New file found :" + fileName);
                        COMPLETE_FILE_PATH = FOLDER_PATH + "\\" + fileName;
                        List<String> coordinatesList = geoLocatorDetails.invokeMethod(COMPLETE_FILE_PATH);
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                        String Complete_Output_file = PROCESSED_PATH+ "\\"+fileName+ timeStamp + ".txt";
                        fileWriter(Complete_Output_file,coordinatesList);
                        log.info("File Processed Successfully");
                    }
                }
                boolean valid = watchKey.reset();
                if (!valid) break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fileWriter(String outputPath, List<String> stringList){
        try {
            log.info("writing Coordinates to file");
            FileWriter writer = new FileWriter(outputPath);
            for(String str : stringList){
                writer.write( str + System.lineSeparator());
            }
            log.info("Coordinates added to file successfully");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}