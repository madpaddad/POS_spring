package com.example.demo.controllers;


import com.example.demo.dto.file.Create;
import com.example.demo.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/file-service")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


//    public FileService
//    @PostMapping
//    public String get(){
//        log.info("calling to 8088");
//        return fileService.callHi();
//    }



//    public ResponseEntity<String> callFileService(
//            @RequestPart("file") MultipartFile file,
//            @RequestPart("data") Create data
//    ) {
//        return ResponseEntity.ok(fileService.create(data, file));
//    }
}
