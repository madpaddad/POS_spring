package com.example.demo.services;

import com.example.demo.dto.file.Create;
import com.example.demo.dto.file.CreateResponse;
import com.example.demo.dto.file.UpdateFileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    /**********************************************************************
     Create Product File:
     - it should return back a path to save on model attributes
     ***********************************************************************/


    public Mono<String> create(Create data, MultipartFile file) {

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", file.getResource())
                            .filename(file.getOriginalFilename())
                                    .contentType(MediaType.parseMediaType(file.getContentType()));

            builder.part("data", data)
                    .contentType(MediaType.APPLICATION_JSON);


            log.info("Sending body{}", builder);
            Mono<String> response = webclient.
                    post().
                    uri("/api/fileService/product")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(CreateResponse.class) // define the Response DTO that it returns for type safe
                    .map(CreateResponse::getPath); // map the value and return back to the method


            return response;
    }

    public Mono<String> update(UpdateFileDTO updateFileDTO, MultipartFile file){

        /*
        After Updating:
        - Return back a path to omit
        */

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        // DTO part
        builder.part("data", updateFileDTO)
                .contentType(MediaType.APPLICATION_JSON);

        // File part
        if (file != null && !file.isEmpty()) {
            builder.part("file", file.getResource());
        }

        return webclient
                .put()
                .uri("/api/fileService/product")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(CreateResponse.class)
                .map(CreateResponse::getPath);
    }
}
