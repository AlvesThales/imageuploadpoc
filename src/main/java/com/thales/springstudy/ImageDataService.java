package com.thales.springstudy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageDataService {

    @Autowired
    private ImageDataRepository imageDataRepository;

    private static final long MAX_FILE_SIZE = 4194304;

    public String uploadImage(MultipartFile file)  throws IllegalStateException {

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalStateException("File size exceeded. Please upload a picture smaller than 4mb.");
        }

        try {
            imageDataRepository.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtil.compressImage(file.getBytes())).build());

            return "Image uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException exception) {
        return "Error uploading file: " + exception;
    }
}

    @Transactional
    public ImageData getInfoByImageByName(String name) throws ImageNotFoundException {
        ImageData dbImage = imageDataRepository.findByName(name).orElseThrow(() -> new ImageNotFoundException(name));

        return ImageData.builder()
                .name(dbImage.getName())
                .type(dbImage.getType())
                .imageData(ImageUtil.decompressImage(dbImage.getImageData()))
                .build();
    }

    @Transactional
    public byte[] getImage(String name) throws ImageNotFoundException {
        ImageData dbImage = imageDataRepository.findByName(name).orElseThrow(() -> new ImageNotFoundException(name));
        return ImageUtil.decompressImage(dbImage.getImageData());
    }
}