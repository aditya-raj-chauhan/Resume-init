package com.devotee.resume.init.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final Cloudinary cloudinary;

    public Map<String, String> uploadSingleImage(
            MultipartFile file
    ) throws IOException {

        log.info("Inside FileUploadService : uploadSingleImage()");

        if (file.isEmpty()) {

            log.error("Uploaded file is empty");

            throw new RuntimeException(
                    "File cannot be empty"
            );
        }

        log.info(
                "Uploading image : {}",
                file.getOriginalFilename()
        );

        Map<String, Object> imageUploadResult =
                cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "image",
                                "folder", "resume-init"
                        )
                );

        log.info("Image uploaded successfully to Cloudinary");

        String imageUrl =
                imageUploadResult
                        .get("secure_url")
                        .toString();

        log.info(
                "Generated image URL : {}",
                imageUrl
        );

        return Map.of(
                "imageUrl",
                imageUrl
        );
    }
}