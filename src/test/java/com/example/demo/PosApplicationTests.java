package com.example.demo;

import com.example.demo.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PosApplicationTests {
    private WebClient webclient;
    private RestTemplate restTemplate;
    private final FileService fileService = new FileService(webclient, restTemplate);

//    @Test
//	void shouldReturnHelloWorld() {
//        ResponseEntity<String> response = fileService.hello();


//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Hello World", response.getBody());
//    }

}
