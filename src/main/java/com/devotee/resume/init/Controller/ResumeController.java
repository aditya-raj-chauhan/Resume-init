package com.devotee.resume.init.Controller;

import com.devotee.resume.init.document.Resume;
import com.devotee.resume.init.dto.CreateResumeRequest;
import com.devotee.resume.init.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<?>createResume(@Valid @RequestBody CreateResumeRequest request, Authentication authentication){

//        call the service method to create a resume
       Resume newResume= resumeService.createResume(request,authentication.getPrincipal());

        //then simply return the response
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);

    }

    @GetMapping
    public ResponseEntity<?>getUserResumes(){
        return null;

    }

    @GetMapping(ID)
    public ResponseEntity<?>getUserResumeById(@PathVariable String id){
        return null;

    }

    @PutMapping(ID)
    public ResponseEntity<?>updateResume(@PathVariable String id ,@RequestBody Resume newData){
        return null;

    }

    @PutMapping(UPLOAD_IMAGES)

    public ResponseEntity<?>uploadResumeImages(@PathVariable String id , @RequestPart(value = "thumbnail" ,required = true)MultipartFile thumbnail, @RequestPart(value = "profileImage",required = false)MultipartFile profileImage, HttpServletRequest request){
        return null;

    }

    @DeleteMapping(ID)
    public ResponseEntity<?>deleteResume(@PathVariable String id){
        return null;


    }

}
