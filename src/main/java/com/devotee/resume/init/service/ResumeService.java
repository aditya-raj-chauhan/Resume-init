package com.devotee.resume.init.service;

import com.devotee.resume.init.Repository.ResumeRepository;
import com.devotee.resume.init.document.Resume;
import com.devotee.resume.init.dto.AuthResponse;
import com.devotee.resume.init.dto.CreateResumeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    public Resume createResume(CreateResumeRequest request , Object principle) {
//        step 1 : create a resume object
             Resume newResume = new Resume();


//        Step 2 : get the current profile
       AuthResponse response= authService.getProfile(principle);
//        step 3 : update the resume object
        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());
//        step 4 set default data for resume
        setDefaultResumeData(newResume);
//        step 5 save the resume data

       return resumeRepository.save(newResume);


    }

    private void setDefaultResumeData(Resume newResume) {
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducation(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setIntrest(new ArrayList<>());
    }
}
