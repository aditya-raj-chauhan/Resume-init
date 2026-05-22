package com.devotee.resume.init.Controller;

import com.devotee.resume.init.Repository.UserRepository;
import com.devotee.resume.init.document.User;
import com.devotee.resume.init.dto.AuthResponse;
import com.devotee.resume.init.dto.LoginRequest;
import com.devotee.resume.init.dto.RegisterRequest;
import com.devotee.resume.init.service.AuthService;
import com.devotee.resume.init.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static com.devotee.resume.init.util.AppConstants.*;

@RestController
@RequestMapping(AUTH_CONTROLLER)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final FileUploadService fileUploadService;

    @PostMapping(REGISTER)

    public ResponseEntity<?>register(@Valid @RequestBody RegisterRequest request){
        System.out.println("into the auth controller");
        try{
            AuthResponse response =authService.register(request);
            log.info("response from service:{}",response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message",e.getMessage()));
        }

    }
    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?>verifyEmail(@RequestParam String token){
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","email verified successfully"));

    }
    @PostMapping(UPLOAD_IMAGE)
    public ResponseEntity<?>uploadImage(@RequestPart("image")MultipartFile file) throws IOException {
        log.info("Inside Auth Controller-uploadImage()");
       Map<String,String>response= fileUploadService.uploadSingleImage(file);
       return ResponseEntity.ok(response);


    }

    @PostMapping(LOGIN)
    public ResponseEntity<?>login(@Valid  @RequestBody LoginRequest loginRequest){
      AuthResponse response=  authService.login(loginRequest);
      return ResponseEntity.ok(response);


    }
    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?>resendVerification(@RequestBody Map<String,String>body){
       String email= body.get("email");
       if (Objects.isNull(email)){
           return ResponseEntity.badRequest().body(Map.of("message","email is required"));
       }
       authService.resendVerification(email);
       return ResponseEntity.ok(Map.of("success",true,"message","verification email sent "));

    }
    @GetMapping(GET_PROFILE)
 public ResponseEntity<?>getProfile(Authentication authentication){
        // from this auth object get the principle object and convert it into the user object
        Object principleObject=authentication.getPrincipal();


        //call the service method
        AuthResponse response=authService.getProfile(principleObject);

        //return the responses
        return ResponseEntity.ok(response);

 }


}
