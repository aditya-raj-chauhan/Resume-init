package com.devotee.resume.init.Controller;

import com.devotee.resume.init.document.Resume;
import com.devotee.resume.init.dto.CreateResumeRequest;
import com.devotee.resume.init.service.FileUploadService;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.devotee.resume.init.util.AppConstants.*;

@RestController
@RequestMapping(RESUME)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;
    @PostMapping
    public ResponseEntity<?>createResume(@Valid @RequestBody CreateResumeRequest request, Authentication authentication){

//        call the service method to create a resume
       Resume newResume= resumeService.createResume(request,authentication.getPrincipal());

        //then simply return the response
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);

    }

    @GetMapping
    public ResponseEntity<?>getUserResumes(Authentication authentication){
        //step 1 : call the service method
       List<Resume> resumes= resumeService.getUserResumes(authentication.getPrincipal());


        //step 2 : return the response
        return ResponseEntity.ok(resumes);

    }

    @GetMapping(ID)
    public ResponseEntity<?>getUserResumeById(@PathVariable String id
            ,Authentication authentication){
//        step 1 ; call the service method
       Resume existingResume= resumeService.getResumeById(id,authentication.getPrincipal());
//        step 2 ; return the response

        return ResponseEntity.ok(existingResume);

    }

    @PutMapping(ID)
    public ResponseEntity<?>updateResume(@PathVariable String id ,@RequestBody Resume newData,Authentication authentication
    ){

        //step 1 : call the service method
       Resume updatedresume= resumeService.updateResume(id,newData,authentication.getPrincipal());
        //step 2 : return the result
        return ResponseEntity.ok(updatedresume);

    }

    @PutMapping(UPLOAD_IMAGES)

    public ResponseEntity<?>uploadResumeImages(@PathVariable String id , @RequestPart(value = "thumbnail" ,required = false)MultipartFile thumbnail, @RequestPart(value = "profileImage",required = false)MultipartFile profileImage, HttpServletRequest request,Authentication authentication) throws IOException {


        //call the service method
       Map<String,String>response= fileUploadService.uploadResumeImages(id,authentication.getPrincipal(),thumbnail,profileImage);
        //return the response
        return ResponseEntity.ok(response);

    }

    @DeleteMapping(ID)
    public ResponseEntity<?>deleteResume(@PathVariable String id,Authentication authentication){

//        step 1 : call the service method
        resumeService.deleteResume(id,authentication.getPrincipal());
//        step 2 : return the response
        return ResponseEntity.ok(Map.of("message","resume deleted successfully"));


    }

}
