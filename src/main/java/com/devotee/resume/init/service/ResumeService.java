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
import java.util.List;

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

    public List<Resume> getUserResumes(Object principal) {
//        step 1 : get the current profile
       AuthResponse response= authService.getProfile(principal);

//        step 2 : call the repo finder method and return the response
       return resumeRepository.findByUserIdOrderByUpdatedAtDesc(response.getId());

//
    }

    public Resume getResumeById(String id, Object principal) {
//        step 1 : get the current profile
      AuthResponse response=  authService.getProfile(principal);
//        step 2: call the repository finder method
        Resume existingResume=resumeRepository.findByUserIdAndId(response.getId(),id)
                .orElseThrow(()->new RuntimeException("resume not found "));
//        step 3: return response
        return existingResume;

    }

    public Resume updateResume(String id, Resume newData, Object principal) {
        //step 1: we need to get the current profile
        AuthResponse response=authService.getProfile(principal);
        //step 2: call the repo finder method by userId and resume id
      Resume existingResume=  resumeRepository.findByUserIdAndId(response.getId(),id).orElseThrow(()->new RuntimeException("resume not found "));
        //step 3: update the new data
        existingResume.setTitle(newData.getTitle());
        existingResume.setThumbnailLink(newData.getThumbnailLink());
        existingResume.setTemplate(newData.getTemplate());
        existingResume.setProfileInfo(newData.getProfileInfo());
        existingResume.setContactInfo(newData.getContactInfo());
        existingResume.setWorkExperiences(newData.getWorkExperiences());
        existingResume.setEducation(newData.getEducation());
        existingResume.setSkills(newData.getSkills());
        existingResume.setProjects(newData.getProjects());
        existingResume.setCertifications(newData.getCertifications());
        existingResume.setLanguages(newData.getLanguages());
        existingResume.setIntrest(newData.getIntrest());


        //step 4: save in db
        resumeRepository.save(existingResume);
        //step 5: return the response
        return existingResume;
    }

    public void deleteResume(String id, Object principal) {
        // get the current profile
       AuthResponse response= authService.getProfile(principal);
        // call the repo  finder method
       Resume existingResume= resumeRepository.findByUserIdAndId(response.getId(),id).orElseThrow(()->new RuntimeException("not found"));
       resumeRepository.delete(existingResume);
        //

    }
}
