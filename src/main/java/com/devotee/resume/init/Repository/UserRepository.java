package com.devotee.resume.init.Repository;

import com.devotee.resume.init.document.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User>findByEmail(@NotBlank(message = "email is required") @Email(message = "email should be valid ") String email);
    boolean existsByEmail(@Email(message = "email should be valid") @NotBlank(message = "email is required") String email);

    Optional<User>findByVerificationToken(String verificationToken);
}
