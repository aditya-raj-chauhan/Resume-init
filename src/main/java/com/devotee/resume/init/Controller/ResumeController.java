package com.devotee.resume.init.Controller;

import com.devotee.resume.init.document.Resume;
import com.devotee.resume.init.dto.CreateResumeRequest;
import com.devotee.resume.init.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.devotee.resume.init.util.AppConstants.*;

@RestController
@RequestMapping(RESUME)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;
    @PostMapping
    public ResponseEntity<?>createResume( @Valid @RequestBody CreateResumeRequest request){

    }

    @GetMapping
    public ResponseEntity<?>getUserResumes(){

    }

    @GetMapping(ID)
    public ResponseEntity<?>getUserResumeById(@PathVariable String id){

    }

    @PutMapping(ID)
    public ResponseEntity<?>updateResume(@PathVariable String id ,@RequestBody Resume newData){

    }

    @PutMapping(UPLOAD_IMAGES)

    public ResponseEntity<?>uploadResumeImages(@PathVariable String id , @RequestPart(value = "thumbnail" ,required = true)MultipartFile thumbnail, @RequestPart(value = "profileImage",required = false)MultipartFile profileImage, HttpServletRequest request){

    }

    @DeleteMapping(ID)
    public ResponseEntity<?>deleteResume(@PathVariable String id){


    }

}
