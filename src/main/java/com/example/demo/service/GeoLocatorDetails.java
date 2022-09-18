package com.example.demo.service;


import com.example.demo.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Component
public class GeoLocatorDetails {

    @Autowired
    public  GeoLocation getLocation;

    public List<String> invokeMethod(String path) throws IOException, InterruptedException {

        log.info("File Path :"+path);
        Scanner s = new Scanner(new File(path));
        ArrayList<String> CityList = new ArrayList<>();
        while (s.hasNext()){
            CityList.add(s.next());
        }
        s.close();

        List<String> result = new ArrayList<>();
        for(String str : CityList) {
            Data response = getLocation.getCoordinates(str);
            String coordinates = response == null ? "No Coordinates Found" : response.getLatitude()+","+response.getLongitude();
            result.add(coordinates);
        }

        return result;
    }
}
