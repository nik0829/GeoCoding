package com.example.demo.service;

import com.example.demo.model.CityDetails;
import com.example.demo.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

import com.google.gson.Gson;

@Slf4j
@Component
public class GeoLocation {

    private static final String GEOCODING_RESOURCE = "http://api.positionstack.com/v1/forward";
    private static final String API_KEY = "caca92c644e869352e9830f9b75df329";
    private static RestTemplate restTemplate = new RestTemplate();

    @Cacheable(cacheNames = "coordinates", key = "#query")
    public Data getCoordinates(String query) throws IOException {

        log.info("Entering ...");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
        String encodedQuery = URLEncoder.encode(query,"UTF-8");

        String requestUri = GEOCODING_RESOURCE + "?access_key=" + API_KEY + "&query=" + encodedQuery;
        log.info("Calling Endpoint to get coordinates");
        ResponseEntity<String> result = restTemplate.exchange(requestUri, HttpMethod.GET,entity,String.class);
        log.info("Endpoint call successfully");
        boolean flag = result.getBody().length() > 11;
        CityDetails cityDetailsList = flag ? new Gson().fromJson(result.getBody(), CityDetails.class) : null;

        log.info("Exiting ...");
        return cityDetailsList == null ? null : cityDetailsList.getData().get(0);
    }

}
