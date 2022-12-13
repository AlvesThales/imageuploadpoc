package com.thales.springstudy;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageDataController {

    @Autowired
    private ImageDataService imageDataService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {

        try {
            String response = imageDataService.uploadImage(file);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } catch (FileSizeLimitExceededException exception) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(exception);
        }
    }

    @GetMapping("/info/{name}")
    public ResponseEntity<?> getImageInfoByName(@PathVariable("name") String name) {

        // Can throw a jakarta.persistence.NonUniqueResultException !!

        try {
            ImageData image = imageDataService.getInfoByImageByName(name);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(image);
        } catch (ImageNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found!");
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getImageByName(@PathVariable("name") String name) {

        // Can throw a jakarta.persistence.NonUniqueResultException !!

        try {
            byte[] image = imageDataService.getImage(name);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(image);
        } catch (ImageNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found!");
        }
    }


}