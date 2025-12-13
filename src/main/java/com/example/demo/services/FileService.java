package com.example.demo.services;

import com.example.demo.dto.file.FileCreateDto;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {


    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    @Autowired
    private final WebClient webclient;

    @Autowired
    private final RestTemplate restTemplate;

    public FileService(WebClient webclient, RestTemplate restTemplate) {
        this.webclient = webclient;
        this.restTemplate = restTemplate;
    }

    public String callHi(FileCreateDto data, MultipartFile file) {
//        try{
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
//
            builder.part("file", file.getResource())
                            .filename(file.getOriginalFilename())
                                    .contentType(MediaType.parseMediaType(file.getContentType()));

            builder.part("data", data)
                    .contentType(MediaType.APPLICATION_JSON);


            log.info("Sending body{}", builder);
            Mono<ResponseEntity<String>> response = webclient.
                    post().
                    uri("/api/fileService/save")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(builder.build())
                    .retrieve()
                    .toEntity(String.class)
                    .doOnSubscribe(sub -> log.info("Subscribe to the list"))
                    .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occured"));

            log.info("Response{}",response);

            ResponseEntity<String> responseEntity = response.block(); // block() waits for the response
            return responseEntity != null ? responseEntity.getBody() : "Error occurred";
//            String url = "http://localhost:8088/file-service/hi";
//            log.info("url is {}", url);

            // Use getForEntity to capture the response status
//            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Log the status code!
//            log.info("Status Code from 8088: {}", response.getHeaders());

//            // If you see 401/403, that's your problem.
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return response.getBody();
//            } else {
//
//                // Handle the error (e.g., return a default or rethrow a custom exception)
//                throw new RuntimeException("Call to 8088 failed with status: " + response.getStatusCode());
//            }
//        } catch (Exception e){
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error request");
//        }

    }
}
