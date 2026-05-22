package com.devotee.resume.init.Repository;

import com.devotee.resume.init.document.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends MongoRepository<Resume,String> {
}
