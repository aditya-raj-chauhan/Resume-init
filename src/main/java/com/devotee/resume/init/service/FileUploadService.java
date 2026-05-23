package com.devotee.resume.init.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.devotee.resume.init.Repository.ResumeRepository;
import com.devotee.resume.init.document.Resume;
import com.devotee.resume.init.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;

    public Map<String, String> uploadSingleImage(
            MultipartFile file
    ) throws IOException {

        log.info("Inside FileUploadService : uploadSingleImage()");

        if (file == null || file.isEmpty()) {

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
    public Map<String, String> uploadResumeImages(
            String resumeId,
            Object principal,
            MultipartFile thumbnail,
            MultipartFile profileImage
    ) throws IOException {

        // Step 1: get current profile
        AuthResponse response =
                authService.getProfile(principal);

        // Step 2: get existing resume
        Resume existingResume =
                resumeRepository
                        .findByUserIdAndId(
                                response.getId(),
                                resumeId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"
                                )
                        );

        // Step 3: upload thumbnail
        Map<String, String> uploadResult =
                uploadSingleImage(thumbnail);

        String thumbnailUrl =
                uploadResult.get("imageUrl");

        // Step 4: upload profile image
        uploadResult =
                uploadSingleImage(profileImage);

        String profilePreviewUrl =
                uploadResult.get("imageUrl");

        // Step 5: set values matching your JSON structure
        existingResume.setThumbnailLink(
                thumbnailUrl
        );

        existingResume
                .getProfileInfo()
                .setProfilePreviewUrl(
                        profilePreviewUrl
                );

        // Step 6: save updated resume
        resumeRepository.save(existingResume);

        // Step 7: return response
        return Map.of(
                "thumbnailLink",
                thumbnailUrl,

                "profilePreviewUrl",
                profilePreviewUrl
        );
    }
}